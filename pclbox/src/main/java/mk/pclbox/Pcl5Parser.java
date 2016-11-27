package mk.pclbox;

import java.io.ByteArrayOutputStream;
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

    private static final int ESCAPE = 0x1B;
    private static final int BACKSPACE = 0x08; // The "lowest-value" control character
    private static final int SHIFT_OUT = 0x0F; // The "highest-value" control character
    private static final int VERTICAL_TAB = 0x0B; // Exception - this is NO control character in PCL5!

    private final PclInputStream stream;

    private ParserState state;
    private int alreadyReadByte;

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
    public PrinterCommand parseNextPrinterCommand() throws IOException {

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
     */
    private static boolean isControlCharacter(final int byteToCheck) {
        return byteToCheck != VERTICAL_TAB && byteToCheck >= BACKSPACE && byteToCheck <= SHIFT_OUT;
    }

    private PrinterCommand continueParsingOfEscapeSequence(long offset) {
        return null; // to be implemented... soon....
    }

    private PrinterCommand parseEscapeSequenceFromTheBeginning(long offset) {
        return null; // to be implemented... soon....
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
