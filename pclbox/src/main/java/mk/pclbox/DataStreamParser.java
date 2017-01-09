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
 * Superclass of all printer data stream parsers.
 */
abstract class DataStreamParser {

    private final PclParserContext ctx;

    /**
     * Constructor.
     *
     * @param ctx - the {@link PclParserContext} that holds all information that is required
     *     by the concrete {@link DataStreamParser}.
     */
    DataStreamParser(final PclParserContext ctx) {
        this.ctx = ctx;
    }

    /**
     * Gets the {@link PclParserContext}.
     *
     * @return the {@link PclParserContext}.
     */
    PclParserContext getContext() {
        return this.ctx;
    }

    /**
     * Gets the {@link PclInputStream} to read from.
     *
     * @return the {@link PclInputStream} to read from.
     */
    PclInputStream getInputStream() {
        return this.ctx.getInputStream();
    }

    /**
     * Gets the {@link PrinterCommandHandler} that handles the read {@link PrinterCommand}.
     *
     * @return the {@link PrinterCommandHandler}.
     */
    PrinterCommandHandler getPrinterCommandHandler() {
        return this.ctx.getPrinterCommandHandler();
    }

    /**
     * Parses the data stream. For every parsed {@link PrinterCommand} the {@link PrinterCommandHandler} is invoked.
     *
     * @return the last byte read from the data stream that has caused the parser to stop (i. e. the PJL parser
     *     will return 0x1B if it hits the first PCL command). This may also be -1 if the end of the data stream
     *     has been reached.
     */
    abstract int parse() throws IOException, PclException;
}
