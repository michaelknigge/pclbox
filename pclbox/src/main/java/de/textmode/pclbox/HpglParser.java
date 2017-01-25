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

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * This {@link HpglParser} parses HP/GL and HP/GL-2 data stream. This parser is quite simple
 * and does only support HP/GL commands that are terminated by a ";". The HP/GL specification
 * does not require this termiantor in all cases, but nowadays it is a convention to use the
 * terminator.
 */
final class HpglParser extends DataStreamParser {

    private static final Charset ISO_8859_1 = Charset.forName("iso-8859-1");

    private static final int END_OF_STREAM = -1;
    private static final int ESCAPE = 0x1B;
    private static final int TERMINATOR = ';';
    private static final int DOUBLE_QUOTES = '\"';

    /**
     * Constructor. Just gets the {@link PclParserContext}.
     *
     * @param context   the {@link PclParserContext} that contains all needed stuff.
     */
    HpglParser(PclParserContext ctx) {
        super(ctx);
    }

    @Override
    int parse() throws IOException, PclException {

        final byte[] commandBytes = new byte[2];
        int firstByte = this.getInputStream().read();

        while (firstByte != ESCAPE && firstByte != END_OF_STREAM) {
            final int secondByte = this.getInputStream().read();
            if (secondByte == END_OF_STREAM) {
                throw this.createEndOfFileException();
            }

            commandBytes[0] = (byte) (firstByte & 0xFF);
            commandBytes[1] = (byte) (secondByte & 0xFF);

            // HP/GL commands can be upper or lower case - we'll convert to upper case
            // because this is the most common way....
            firstByte = this.parseCommand(
                    this.getInputStream().tell() - 2,
                    new String(commandBytes, ISO_8859_1).toUpperCase());

            if (firstByte == TERMINATOR) {
                firstByte = this.getInputStream().read();
            }
        }

        return firstByte;
    }

    /**
     * Parses the HP/GL command from the data Stream. The first two Bytes of the command have been read
     * already, so this method reads the rest of the command - at least if the command is no "two byte command".
     *
     * @param command   the HP/GL command (in upper case)
     *
     * @return the value of the last read byte that has been read from the stream
     */
    private int parseCommand(final long offset, final String command) throws IOException {

        // HP/GL is somewhat painful. Normally, HP/GL commands do not require a terminator, but some do. Even further
        // there are commands that are terminated by a user defined termination character and not the default ";"
        // terminator..... Then... the HP/GL command "CO" (Comment) requires that the comment is placed in quotes
        // and the last quotes are the terminator). But of course in the wild you'll find HP/GL files where the comment
        // is not put in double quotes....
        //
        // Currently we only support commands that are terminated with the default terminator ";". Nowadays this is
        // by convention the way HP/GL is included in PCL... At least I've never seen HP/GL (embedded in PCL) that
        // does not follow this convention...
        final StringBuilder sb = new StringBuilder();
        boolean inQuotedString = false;
        int readByte = this.getInputStream().read();

        while (readByte != END_OF_STREAM) {

            if (readByte == TERMINATOR && !inQuotedString) {
                this.getPrinterCommandHandler().handlePrinterCommand(
                        new HpglCommand(offset, command, sb.toString().trim()));

                return readByte;
            } else {
                sb.append((char) readByte);
            }

            // There are commands that may contain Strings enclosed in double quotes (i.e. "CO" and "MG").
            // According to the spec, these commands are required to end with double qoutes (and a double
            // qoute within the quoted string is coded as two consecutive quoted strings).
            if (readByte == DOUBLE_QUOTES) {
                if (inQuotedString) {
                    final int nextByte = this.getInputStream().read();
                    if (nextByte != DOUBLE_QUOTES) {
                        this.getPrinterCommandHandler().handlePrinterCommand(
                                new HpglCommand(offset, command, sb.toString().trim()));

                        return nextByte;
                    }
                } else {
                    inQuotedString = true;
                }
            }

            readByte = this.getInputStream().read();
        }

        // If we get here we've hit the end of the stream but we've not reached the end of the HP/GL command.
        // The specification clearly states that the last command must be terminated by the termination character.
        // So the data stream may be truncated....
        throw this.createEndOfFileException();
    }

    /**
     * Returns a new {@link EOFException} that contains the offset at which the data stream
     * hits unexpectedly the end.
     *
     * @return a new {@link EOFException}.
     */
    private EOFException createEndOfFileException() throws IOException {
        return new EOFException(String.format(
                "The HP/GL data stream unexpectedly ends at offset %1$d. The data stream may be corrupted.",
                this.getInputStream().tell()));
    }

}
