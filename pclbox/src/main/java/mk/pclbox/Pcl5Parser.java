package mk.pclbox;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;

/**
 * This {@link DataStreamParser} parses PCL5 printer data stream.
 */
final class Pcl5Parser implements DataStreamParser {

    private enum ParserState {
        /**
         * Start of the stream or of a PCL command.
         */
        START,

        /**
         * we've read the escape byte or control character already.
         */
        ALREADY_READ_BYTE,

        /**
         * we are "in the middle" of a sequence of multiple PCL commands.
         */
        IN_SEQUENCE
    }

    private static final int END_OF_STREAM = -1;
    private static final int UNDEFINED = -1;

    private static final int ESCAPE = 0x1B;
    private static final int VERTICAL_TAB = 0x0B;

    private static final int OPERATION_CHARACTER_MIN = 48;
    private static final int OPERATION_CHARACTER_MAX = 126;

    private static final int PARAMETERIZED_CHARACTER_MIN = 33;
    private static final int PARAMETERIZED_CHARACTER_MAX = 47;

    private static final int GROUP_CHARACTER_MIN = 96;
    private static final int GROUP_CHARACTER_MAX = 126;

    private static final int PARAMETER_CHARACTER_MIN = 96;
    private static final int PARAMETER_CHARACTER_MAX = 126;

    private static final int TERMINATION_CHARACTER_MIN = 64;
    private static final int TERMINATION_CHARACTER_MAX = 94;

    private final PclInputStream stream;

    private ParserState state;
    private int alreadyReadByte;
    private int parameterizedCharacter;
    private int groupCharacter;

    /**
     * Constructor. Just gets the stream.
     *
     * @param stream - the stream that contains the PCL 5 commands.
     */
    Pcl5Parser(final PclInputStream stream) {
        this.stream = stream;
        this.state = ParserState.START;
    }

    @Override
    public PrinterCommand parseNextPrinterCommand() throws IOException, PclException {

        final long offset = this.state == ParserState.ALREADY_READ_BYTE ? this.stream.tell() - 1 : this.stream.tell();

        if (this.state == ParserState.START) {
            final int firstByte = this.stream.read();
            if (firstByte == END_OF_STREAM) {
                return null;
            }

            if (firstByte != ESCAPE) {
                if (isControlCharacter(firstByte)) {
                    return new ControlCharacterCommand(offset, (byte) firstByte);
                } else {
                    return this.parseTextPrinterCommand(offset, firstByte);
                }
            }

            this.alreadyReadByte = firstByte;
            this.parameterizedCharacter = UNDEFINED;
            this.groupCharacter = UNDEFINED;

            this.state = ParserState.ALREADY_READ_BYTE;
        }

        assert this.state == ParserState.ALREADY_READ_BYTE || this.state == ParserState.IN_SEQUENCE;
        if (this.state == ParserState.ALREADY_READ_BYTE) {
            if (isControlCharacter(this.alreadyReadByte)) {
                this.state = ParserState.START;
                return new ControlCharacterCommand(offset, (byte) this.alreadyReadByte);
            }
            return this.parseEscapeSequenceFromTheBeginning(offset);
        } else {
            return this.continueParsingOfEscapeSequence(offset);
        }
    }

    /**
     * Checks if a byte with the given value is a PCL5 control character.
     *
     * @param byteToCheck - the byte to be checked
     *
     * @return true if the given byte is a control character.
     */
    private static boolean isControlCharacter(final int byteToCheck) {
        if (byteToCheck >= ControlCharacterCommand.BACKSPACE && byteToCheck <= ControlCharacterCommand.SHIFT_OUT) {
            return byteToCheck != VERTICAL_TAB;
        } else {
            return false;
        }
    }

    /**
     * Parses a PCL5 command just from the beginning (means, that the previous read byte is the
     * escape byte).
     *
     * @param offset - offset of the previous read escape byte , measured from the beginning of the read data stream.
     *
     * @return a PclCommand object containing the parsed PCL command.
     */
    private PrinterCommand parseEscapeSequenceFromTheBeginning(long offset) throws PclException, IOException {
        assert this.alreadyReadByte == ESCAPE;

        // The first byte after the escape byte is the parameterized character.
        this.parameterizedCharacter = this.stream.read();
        if (this.parameterizedCharacter == END_OF_STREAM) {
            throw new EOFException(String.format(
                    "The PCL data stream unexpectedly ends at offset %1$d. The data stream may be corrupted.", offset));
        }

        if (this.parameterizedCharacter >= OPERATION_CHARACTER_MIN
                && this.parameterizedCharacter <= OPERATION_CHARACTER_MAX) {
            this.resetStateToStart();
            return new TwoBytePclCommand(offset, this.parameterizedCharacter);
        }

        if (this.parameterizedCharacter < PARAMETERIZED_CHARACTER_MIN
                || this.parameterizedCharacter > PARAMETERIZED_CHARACTER_MAX) {
            throw new PclException(String.format(
                    "The byte value of the parameterized character at offset %1$d is invalid", offset + 1));
        }

        // After the parameterized the group character follows...
        this.groupCharacter = this.stream.read();
        if (this.groupCharacter == END_OF_STREAM) {
            throw new EOFException(String.format(
                    "The PCL data stream unexpectedly ends at offset %1$d. The data stream may be corrupted.", offset));
        }

        if (this.groupCharacter < GROUP_CHARACTER_MIN || this.groupCharacter > GROUP_CHARACTER_MAX) {
            throw new PclException(String.format(
                    "The byte value of the group character at offset %1$d is invalid.", offset + 2));
        }

        return this.continueParsingOfEscapeSequence(offset);
    }

    /**
     * Continues parsing of a PCL5 command after a group or parameter character.
     *
     * @param offset - offset to the start of the PCL command or of the part of the parameterized escape sequence
     *     that gets parsed now - measured from the beginning of the read data stream.
     *
     * @return a PclCommand object containing the parsed PCL command.
     */
    private PrinterCommand continueParsingOfEscapeSequence(long offset) throws IOException, PclException {
        int readByte = this.stream.read();
        if (readByte == END_OF_STREAM) {
            // If we are "in the middle" of a parameterized escape sequence and we hit EOF here, we assume that
            // the parameterized escape sequence is "just" incorrectly terminated with a lower case letter (this is
            // not unusual "in the wild")...
            if (this.state == ParserState.IN_SEQUENCE) {
                return null;
            }

            throw new EOFException(String.format(
                    "The PCL data stream unexpectedly ends at offset %1$d. The data stream may be corrupted.", offset));
        }

        // If we encounter an escape here, the previous PCL command may not have been ended correctly. "In the wild"
        // it is not unlikely that a parameterized escape sequence is (incorrectly) ended with a lower case character
        // (instead of an upper case character). So if we get an escape here, we assume that a new PCL command begins.
        if (this.state == ParserState.IN_SEQUENCE && readByte == ESCAPE) {
            this.state = ParserState.ALREADY_READ_BYTE;
            this.alreadyReadByte = readByte;
            this.parameterizedCharacter = UNDEFINED;
            this.groupCharacter = UNDEFINED;

            return this.parseEscapeSequenceFromTheBeginning(offset);
        }

        // Now read the "value string". Note that a "value" may be omitted - then "0" is assumed....
        final StringBuilder sb = new StringBuilder();
        if (readByte == '+' || readByte == '-' || isValidValueCharacter(readByte)) {
            sb.append((char) readByte);
        } else {
            // A termination character? Then a value is omitted and we've reached the end of the PCL command.
            if (readByte >= TERMINATION_CHARACTER_MIN && readByte <= TERMINATION_CHARACTER_MAX) {
                this.resetStateToStart();
                return new ParameterizedPclCommand(
                        offset,
                        this.parameterizedCharacter,
                        this.groupCharacter,
                        "",
                        readByte);
            }

            // A parameter character? Then a value is omitted and we're now in the middle of a PCL command.
            if (readByte >= PARAMETER_CHARACTER_MIN && readByte <= PARAMETER_CHARACTER_MAX) {
                this.state = ParserState.IN_SEQUENCE;
                return new ParameterizedPclCommand(
                        offset,
                        this.parameterizedCharacter,
                        this.groupCharacter,
                        "",
                        readByte - 32);
            }

            throw new PclException(String.format(
                    "The PCL data stream contains an invalid value at offset %1$d.", offset));
        }

        readByte = this.stream.read();
        while (isValidValueCharacter(readByte)) {
            sb.append((char) readByte);
            readByte = this.stream.read();
        }

        if (readByte == END_OF_STREAM) {
            throw new EOFException(String.format(
                    "The PCL data stream unexpectedly ends at offset %1$d. The data stream may be corrupted.", offset));
        }

        // A termination character? Then we've reached the end of the PCL command.
        if (readByte >= TERMINATION_CHARACTER_MIN && readByte <= TERMINATION_CHARACTER_MAX) {
            this.resetStateToStart();
            return new ParameterizedPclCommand(
                    offset,
                    this.parameterizedCharacter,
                    this.groupCharacter,
                    sb.toString(),
                    readByte);
        }

        // A parameter character? Then we're now in the middle of a PCL command.
        if (readByte >= PARAMETER_CHARACTER_MIN && readByte <= PARAMETER_CHARACTER_MAX) {
            this.state = ParserState.IN_SEQUENCE;
            return new ParameterizedPclCommand(
                    offset,
                    this.parameterizedCharacter,
                    this.groupCharacter,
                    sb.toString(),
                    readByte - 32);
        }

        throw new PclException(String.format(
                "The PCL data stream contains an invalid value at offset %1$d.", offset));
    }

    /**
     * Returns true if the given value is valid for a "value", which means that values
     * between '0' (0x30) and '9' (0x39) are avlid, as well als the decimal point '.'.
     * Note that "+" and "-" are not considered valid!
     *
     * @param value - the value to be checked.
     *
     * @return true true if the given value is valid for a "value".
     */
    private static boolean isValidValueCharacter(final int value) {
        if (value >= '0' && value <= '9') {
            return true;
        } else {
            return value == '.';
        }
    }

    /**
     * Resets the internal state to "start from the beginning" (of a PCL command).
     */
    private void resetStateToStart() {
        this.state = ParserState.START;
        this.alreadyReadByte = UNDEFINED;
    }

    /**
     * Parses text from the data stream.
     *
     * @param offset - position within the data stream where the text starts
     * @param firstByte - the first byte of the text
     *
     * @return a TextCommand object containing the parsed text
     */
    private PrinterCommand parseTextPrinterCommand(final long offset, final int firstByte)
            throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(firstByte);

        int readByte = this.stream.read();
        while (readByte != END_OF_STREAM && readByte != ESCAPE && !isControlCharacter(readByte)) {
            out.write(readByte);
            readByte = this.stream.read();
        }

        this.alreadyReadByte = readByte;
        this.state = readByte == END_OF_STREAM ? ParserState.START : ParserState.ALREADY_READ_BYTE;

        return new TextCommand(offset, out.toByteArray());
    }

    @Override
    public void close() throws IOException {
        this.stream.close();
    }
}
