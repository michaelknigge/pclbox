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

/**
 * The {@link PclParserContext} contains all information that is required by the
 * concrete {@link DataStreamParser} implementations to do their job.
 */
final class PclParserContext {

    private final PclInputStream stream;
    private final PrinterCommandHandler commandHandler;

    /**
     * Constructor. Gets the {@link PclInputStream} we read from and initializes the
     * parser stack (pushes a {@link Pcl5Parser} on top of the stack).
     *
     * @param stream - the stream that contains the PCL printer data stream.
     */
    PclParserContext(final PclInputStream stream, final PrinterCommandHandler commandHandler) {
        this.stream = stream;
        this.commandHandler = commandHandler;
    }

    /**
     * Gets the {@link PclInputStream} to read from.
     *
     * @return the {@link PclInputStream} to read from.
     */
    PclInputStream getInputStream() {
        return this.stream;
    }

    /**
     * Gets the {@link PrinterCommandHandler} that handles the read {@link PrinterCommand}.
     *
     * @return the {@link PrinterCommandHandler}.
     */
    PrinterCommandHandler getPrinterCommandHandler() {
        return this.commandHandler;
    }
}
