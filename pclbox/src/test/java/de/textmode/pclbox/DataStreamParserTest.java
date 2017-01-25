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

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * The abstract superclass of all TestCase classes that require a {@link PrinterCommandHandler}.
 */
public abstract class DataStreamParserTest extends TestCase implements PrinterCommandHandler {

    private final List<PrinterCommand> commands = new ArrayList<PrinterCommand>();

    /**
     * Initializes (clears) the {@link List} of all collected {@link PrinterCommand} for the test method.
     */
    @Override
    protected void setUp() {
        this.commands.clear();
    }

    /**
     * Initializes (clears) the {@link List} of all collected {@link PrinterCommand} for the test method.
     */
    @Override
    protected void tearDown() {
        this.commands.clear();
    }

    /**
     * Adds ths {@link PrinterCommand} to the internal {@link List} of collected {@link PrinterCommand}.
     */
    @Override
    public void handlePrinterCommand(PrinterCommand command) {
        assert command != null;
        this.commands.add(command);
    }

    /**
     * Gets all collected {@link PrinterCommand} objects.
     *
     * @return all collected {@link PrinterCommand} objects
     */
    protected List<PrinterCommand> getCommands() {
        return this.commands;
    }

    protected static List<PrinterCommand> buildExpected(final PrinterCommand... commands) {
        final List<PrinterCommand> result = new ArrayList<>();
        for (final PrinterCommand command : commands) {
            result.add(command);
        }
        return result;
    }

}
