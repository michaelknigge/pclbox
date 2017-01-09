package mk.pclbox;

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

import java.io.IOException;

/**
 * This {@link PjlParser} parses PJL commands. It is constructed from the {@link Pcl5Parser}
 * and will return control to the {@link Pcl5Parser} after an escape bye (0x1B) has been read from
 * the data stream.
 */
final class PjlParser extends DataStreamParser {

    private static final int END_OF_STREAM = -1;
    private static final int ESCAPE = 0x1B;
    private static final int CARRIAGE_RETURN = 0x0D;
    private static final int LINE_FEED = 0x0A;

    private static final String PJL_PREFIX = "@PJL";

    /**
     * Constructor. Just gets the stream.
     *
     * @param stream - the stream that contains the PJL commands.
     */
    PjlParser(final PclParserContext context) {
        super(context);
    }

    @Override
    public int parse() throws IOException, PclException {
        final StringBuilder sb = new StringBuilder();

        long offset = this.getInputStream().tell();
        int readByte = this.getInputStream().read();

        while (readByte != END_OF_STREAM && readByte != ESCAPE) {

            // we check the first byte to be sure that we start parsing a PJL command...
            if (sb.length() == 0 && readByte != '@') {
                throw new PclException("No PJL command is found at offset " + offset);
            }

            // The carriage return is optional and stripped...
            if (readByte != CARRIAGE_RETURN) {
                if (readByte == LINE_FEED) {
                    this.invokeHandler(offset, sb.toString());
                    offset = this.getInputStream().tell();
                    sb.setLength(0);
                } else {
                    sb.append((char) readByte);
                }
            }

            readByte = this.getInputStream().read();
        }

        if (sb.length() != 0) {
            throw new PclException(String.format(
                    "The PJL command at offset %d is not properly terminated with a line feed", offset));
        }

        return readByte;
    }

    /**
     * Invokes the {@link PrinterCommandHandler}.
     *
     * @param offset - the start offset of the PJL command
     * @param command - the PJL command including the prefix "@PJL"
     */
    private void invokeHandler(final long offset, final String command) throws PclException {
        if (command.startsWith(PJL_PREFIX)) {
            this.getPrinterCommandHandler().handlePrinterCommand(new PjlCommand(offset, command.trim()));
        } else {
            throw new PclException("No PJL command is found at offset " + offset);
        }
    }
}
