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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * A {@link PjlCommand} describes a PJL command.
 */
public final class PjlCommand extends PrinterCommand {

    private static final Charset ISO_8859_1 = Charset.forName("iso-8859-1");

    final String commandString;

    /**
     * Constructor for the {@link PjlCommand}.
     *
     * @param offset   position within the data stream
     * @param commandString   the complete PJL command including the "@PJL" prefix
     */
    public PjlCommand(final long offset, final String commandString) {
        super(offset);
        this.commandString = commandString;
    }

    /**
     * Gets the command string including the "@PJL" prefix.
     *
     * @return the command string.
     */
    public String getCommand() {
        return this.commandString;
    }

    @Override
    public String getTextualDescription() {
        return "PJL Command";
    }

    @Override
    public void accept(PrinterCommandVisitor visitor) throws IOException {
        visitor.handle(this);
    }

    @Override
    public int hashCode() {
        return this.getCommand().hashCode() ^ this.getOffsetHash();
    }

    @Override
    public boolean equals(final Object other) {
        if (other instanceof PjlCommand) {
            final PjlCommand o = (PjlCommand) other;
            return o.getCommand().equals(this.getCommand()) && o.getOffset() == this.getOffset();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return this.getCommand() + "@" + this.getOffset();
    }

    @Override
    public String toCommandString() {
        // We currently do not parse the PJL-Commands, so we use a static "command string" for all
        // PJL commands.... Remember: The primary use of the toCommandString() method is to provide a
        // "key" for HashMaps....
        return "PJL";
    }

    @Override
    public String toDisplayString() {
        return this.getCommand();
    }

    @Override
    public byte[] toByteArray() {
        return this.getCommand().getBytes(ISO_8859_1);
    }

    @Override
    public void writeTo(OutputStream out) throws IOException {
        out.write(this.toByteArray());
    }

}