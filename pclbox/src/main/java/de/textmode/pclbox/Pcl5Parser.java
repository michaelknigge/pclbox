package de.textmode.pclbox;

/*
 * Copyright 2017 Michael Knigge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;

/**
 * This {@link DataStreamParser} parses PCL5 printer data stream.
 */
final class Pcl5Parser extends DataStreamParser {

    private static final int END_OF_STREAM = -1;

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


    /**
     * Constructor. Just gets the {@link PclParserContext}.
     *
     * @param context   the {@link PclParserContext} that contains all needed stuff.
     */
    Pcl5Parser(final PclParserContext context) {
        super(context);
    }

    @Override
    int parse() throws IOException, PclException {

        int firstByte = this.getInputStream().read();

        while (firstByte != END_OF_STREAM) {

            final long offset = this.getInputStream().tell() - 1;

            if (firstByte == ESCAPE) {
                firstByte = this.parsePclCommand(offset);
            } else {
                if (isControlCharacter(firstByte)) {
                    firstByte = this.handleReadControlCharacter(offset, firstByte);
                } else {
                    firstByte = this.parseText(offset, firstByte);
                }
            }
        }
        return END_OF_STREAM;
    }

    /**
     * Checks if a byte with the given value is a PCL5 control character.
     *
     * @param byteToCheck   the byte to be checked
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
     * Continues parsing of text in the data stream. The first character/byte has already been read and now this
     * method parses up to the end of the text.
     *
     * @param offset   offset of the previous read character/byte, measured from the beginning of the read data stream.
     * @param firstByte   the (already read) first byte of the text
     *
     * @return the next byte after the read text, which is an escape, control character or just the end of the stream.
     */
    private int parseText(final long offset, final int firstByte) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(firstByte);

        int readByte = this.getInputStream().read();
        while (readByte != END_OF_STREAM && readByte != ESCAPE && !isControlCharacter(readByte)) {
            out.write(readByte);
            readByte = this.getInputStream().read();
        }

        this.getPrinterCommandHandler().handlePrinterCommand(new TextCommand(offset, out.toByteArray()));

        return readByte;
    }

    /**
     * Handles the already read control character.
     *
     * @param offset   offset of the previous read control character, measured from the beginning of the data stream.
     * @param firstByte   the (already read) control character
     *
     * @return the next byte after the control character, which is an escape, control character, some text
     *     or just the end of the stream.
     */
    private int handleReadControlCharacter(final long offset, final int controlCharacter) throws IOException {
        this.getPrinterCommandHandler().handlePrinterCommand(
                new ControlCharacterCommand(offset, (byte) controlCharacter));

        return this.getInputStream().read();
    }

    /**
     * Continues parsing of a PCL command . The first character/byte has already been read (the escape byte) and now
     * this method parses the rest of the command or sequence.
     *
     * @param offset   offset of the previous read escape byte , measured from the beginning of the read data stream.
     *
     * @return the next byte after the read text, which is an escape, control character or just the end of the stream.
     */
    private int parsePclCommand(final long offset) throws IOException, PclException {

        // The first byte after the escape byte is the parameterized character.
        final int parameterizedCharacter = this.getInputStream().read();
        if (parameterizedCharacter == END_OF_STREAM) {
            throw this.createEndOfFileException();
        }

        if (isOperationCharacter(parameterizedCharacter)) {
            this.getPrinterCommandHandler().handlePrinterCommand(
                    new TwoBytePclCommand(offset, parameterizedCharacter));

            return this.getInputStream().read();
        }

        if (!isParameterizedCharacter(parameterizedCharacter)) {
            throw new PclException(String.format(
                    "The byte value of the parameterized character at offset %1$d is invalid",
                    this.getInputStream().tell() - 1));
        }

        // After the parameterized character the group character follows...
        int readByte = this.getInputStream().read();
        if (readByte == END_OF_STREAM) {
            throw this.createEndOfFileException();
        }

        // But note that the group character (and even the "value") is optional - depending on the command!
        // Most common example is the command "<escape>%-12345X" that does not have a group character...
        final int groupCharacter = isGroupCharacter(readByte) ? readByte : 0x00;

        // Now parse the rest of the command or sequence... Note that the value may be omitted (according to the
        // PCL specification, the value is optional). One of the most common commands that has no value is the
        // command "<escape>&d@" (Disable Underline).
        if (isGroupCharacter(readByte)) {
            readByte = this.getInputStream().read();
            if (readByte == END_OF_STREAM) {
                throw this.createEndOfFileException();
            }
        }

        if (readByte == ESCAPE) {
            throw new PclException(String.format(
                    "The byte at offset %1$d is invalid (unexpected escape)", this.getInputStream().tell() - 1));
        }

        //
        if (parameterizedCharacter == '&' && groupCharacter == 'p' && readByte == '<') {
            this.getPrinterCommandHandler().handlePrinterCommand(parseOceImageStreamDataContainer(offset));

            return this.getInputStream().read();
        }

        final StringBuilder sb = new StringBuilder();
        long currentCommandOffset = offset;
        while (readByte != ESCAPE && readByte != END_OF_STREAM) {

            // If we encounter a termination character, we've reached the end of the PCL sequence or command...
            if (isTerminationCharacter(readByte)) {
                final byte[] dataSection;
                // Some PCL commands have a data section (means, some binary data that follows immediately
                // the termination character....
                if (isCommandWithDataSection(parameterizedCharacter, groupCharacter, readByte)) {
                    final int dataSectionSize;
                    try {
                        dataSectionSize = Integer.parseInt(sb.toString());
                    } catch (final NumberFormatException e) {
                        throw new PclException(String.format(
                                "The PCL command at offset %1$d contains the invalid value %2$s.",
                                currentCommandOffset,
                                sb.toString()));
                    }

                    if (dataSectionSize < 0 || dataSectionSize > 32767) {
                        throw new PclException(String.format(
                                "The PCL command at offset %1$d contains the invalid value %2$s.",
                                currentCommandOffset,
                                sb.toString()));
                    }

                    dataSection = new byte[dataSectionSize];
                    final int read = this.getInputStream().read(dataSection);

                    if (read != dataSectionSize) {
                        throw this.createEndOfFileException();
                    }
                } else {
                    dataSection = null;
                }

                final ParameterizedPclCommand command = new ParameterizedPclCommand(
                        currentCommandOffset,
                        parameterizedCharacter,
                        groupCharacter,
                        sb.toString(),
                        readByte,
                        dataSection);

                this.getPrinterCommandHandler().handlePrinterCommand(command);

                // If we've read a "Universal Exit Language Command", we have to switch to PJL...
                if (this.isUniversalExitLanguageCommand(command)) {
                    return new PjlParser(this.getContext()).parse();
                } else if (this.isEnterHpglModeCommand(command)) {
                    return new HpglParser(this.getContext()).parse();
                } else {
                    return this.getInputStream().read();
                }
            }

            // If we encounter a parameter character, we've parsed one part of a PCL escape sequence...
            if (isParameterCharacter(readByte)) {
                this.getPrinterCommandHandler().handlePrinterCommand(
                        new ParameterizedPclCommand(
                                currentCommandOffset,
                                parameterizedCharacter,
                                groupCharacter,
                                sb.toString(),
                                parameterCharacterToTerminationCharacter(readByte)));

                sb.setLength(0);
                currentCommandOffset = this.getInputStream().tell();
            } else {
                if (!isValidValueCharacter(readByte)) {
                    throw new PclException(String.format(
                            "The byte value of the character at offset %1$d is invalid.",
                            this.getInputStream().tell() - 1));
                }

                sb.append((char) readByte);
            }

            readByte = this.getInputStream().read();
        }

        if (sb.length() != 0) {
            throw this.createEndOfFileException();
        }

        return readByte;
    }

    /**
     * Parses the proprietary PCL-Command ImageStream Data Container from Oce.
     */
    private PrinterCommand parseOceImageStreamDataContainer(final long offset) throws IOException, PclException {
        // "&", "p" and "<" are already consumed. Now we need to read the remaining
        // part of this command (which is up to ">A").
        final StringBuilder sb = new StringBuilder();
        sb.append("<");

        int readByte;
        do {
            readByte = this.getInputStream().read();
            sb.append(Character.toString((char) readByte));
        } while (readByte != '>');

        readByte = this.getInputStream().read();
        if (readByte != 'A') {
            throw new PclException(String.format(
                    "The byte value of the character at offset %1$d is invalid.",
                    this.getInputStream().tell() - 1));
        }

        return new ParameterizedPclCommand(
                offset,
                '&',
                'p',
                sb.toString(),
                'A');
    }

    /**
     * Returns a new {@link EOFException} that contains the offset at which the data stream
     * hits unexpectedly the end.
     *
     * @return a new {@link EOFException}.
     */
    private EOFException createEndOfFileException() throws IOException {
        return new EOFException(String.format(
                "The PCL data stream unexpectedly ends at offset %1$d. The data stream may be corrupted.",
                this.getInputStream().tell()));
    }

    /**
     * Transforms the given parameter character to a termination character.
     *
     * @param parameterCharacter   the value of the parameter character
     *
     * @return the termination character that corresponds to the parameter character
     */
    private static int parameterCharacterToTerminationCharacter(final int parameterCharacter) {
        assert isParameterCharacter(parameterCharacter);
        return parameterCharacter - 32;
    }

    /**
     * Returns true if a PCL command with the specified parameterizedCharacter, groupCharacter and terminationCharacter
     * is a command that has a "data section" (means, that there is data immediately following the PCL command that
     * belongs to the PCL command (i. e. the binary data that follows the PCL command "Font Header")...
     *
     * @param parameterizedCharacter   the parameterized character of the PCL command (ASCII range 33 to 47)
     * @param groupCharacter   the group character of the PCL command (ASCII range 96 to 126)
     * @param terminationCharacter   the termination character of the PCL command (ASCII range 64 to 94)
     *
     * @return true if a PCL command with the specified characteristics contains a data section.
     */
    private static boolean isCommandWithDataSection(
            final int parameterizedCharacter,
            final int groupCharacter,
            final int terminationCharacter) {

        // Every PCL-Command that ends with an "W" has a data section (like "Transfer Raster Data (by Row)"
        // or "Font Header"....
        if (terminationCharacter == 'W') {
            return true;
        }

        // Transparent Print Data Command
        if (parameterizedCharacter == '&' && groupCharacter == 'p' && terminationCharacter == 'X') {
            return true;
        }

        // Transfer Raster (by Plane)
        if (parameterizedCharacter == '*' && groupCharacter == 'b' && terminationCharacter == 'V') {
            return true;
        }

        return false;
    }

    /**
     * Returns true if the given {@link ParameterizedPclCommand} is a UEL command.
     *
     * @param command   the {@link ParameterizedPclCommand} to be checked.
     *
     * @return true if the given {@link ParameterizedPclCommand} is a UEL command.
     */
    private boolean isUniversalExitLanguageCommand(ParameterizedPclCommand command) {
        return command.getGroupCharacter() == 0x00
                && command.getParameterizedCharacter() == '%'
                && command.getValue().equals("-12345")
                && command.getTerminationCharacter() == 'X';
    }

    /**
     * Returns true if the given {@link ParameterizedPclCommand} is a "Enter HP/GL Mode" command.
     *
     * @param command   the {@link ParameterizedPclCommand} to be checked.
     *
     * @return true if the given {@link ParameterizedPclCommand} is a "Enter HP/GL Mode" command.
     */
    private boolean isEnterHpglModeCommand(ParameterizedPclCommand command) {
        return command.getGroupCharacter() == 0x00
                && command.getParameterizedCharacter() == '%'
                && command.getTerminationCharacter() == 'B';
    }


    /**
     * Returns true if the given value is valid for a parameter character.
     *
     * @param value   the value to be checked.
     *
     * @return true if the given value is valid for a parameter character.
     */
    private static boolean isParameterCharacter(final int value) {
        return value >= PARAMETER_CHARACTER_MIN && value <= PARAMETER_CHARACTER_MAX;
    }

    /**
     * Returns true if the given value is valid for a parameterized character.
     *
     * @param value   the value to be checked.
     *
     * @return true true if the given value is valid for a parameterized character.
     */
    private static boolean isParameterizedCharacter(final int value) {
        return value >= PARAMETERIZED_CHARACTER_MIN && value <= PARAMETERIZED_CHARACTER_MAX;
    }

    /**
     * Returns true if the given value is valid for a operation character.
     *
     * @param value   the value to be checked.
     *
     * @return true if the given value is valid for a operation character.
     */
    private static boolean isOperationCharacter(final int value) {
        return value >= OPERATION_CHARACTER_MIN && value <= OPERATION_CHARACTER_MAX;
    }

    /**
     * Returns true if the given value is valid for a group character.
     *
     * @param value   the value to be checked.
     *
     * @return true true if the given value is valid for a group character.
     */
    private static boolean isGroupCharacter(final int value) {
        return value >= GROUP_CHARACTER_MIN && value <= GROUP_CHARACTER_MAX;
    }

    /**
     * Returns true if the given value is valid for a termination character.
     *
     * @param value   the value to be checked.
     *
     * @return true if the given value is valid for a termination character.
     */
    private static boolean isTerminationCharacter(final int value) {
        return value >= TERMINATION_CHARACTER_MIN && value <= TERMINATION_CHARACTER_MAX;
    }

    /**
     * Returns true if the given value is valid for a "value", which means that values
     * between '0' (0x30) and '9' (0x39) are valid, as well als the decimal point '.'
     * and "+" and "-".
     *
     * @param value   the value to be checked.
     *
     * @return true if the given value is valid for a "value".
     */
    private static boolean isValidValueCharacter(final int value) {
        if (value >= '0' && value <= '9') {
            return true;
        } else {
            return value == '.' || value == '-' || value == '+';
        }
    }
}
