package mk.pclbox;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

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
 * A {@link HpglCommand} describes a HP/GL command as a simple {@link String}.
 */
public final class HpglCommand extends PrinterCommand {

    private static final Charset ISO_8859_1 = Charset.forName("iso-8859-1");

    private final String command;
    private final String parameters;

    /**
     * Constructor of the {@link HpglCommand}.
     *
     * @param offset - position within the data stream
     * @param command - The two character HP/GL command (upper case)
     * @param parameters - the parameters to this command (or empty string)
     */
    public HpglCommand(final long offset, final String command, final String parameters) {
        super(offset);

        this.command = command;
        this.parameters = parameters;
    }

    /**
     * Returns the two letter command of the HP/GL command.
     *
     * @return the two letter command of the HP/GL command
     */
    public String getCommand() {
        return this.command;
    }

    /**
     * Returns the parameters of thethe HP/GL command.
     *
     * @return the parameters of the HP/GL command or an empty string if the command has no parameters.
     */
    public String getParameters() {
        return this.parameters;
    }

    @Override
    void accept(PrinterCommandVisitor visitor) {
        visitor.handle(this);
    }

    @Override
    public int hashCode() {
        return this.command.hashCode() ^ this.parameters.hashCode() ^ this.getOffsetHash();
    }

    @Override
    public boolean equals(final Object other) {
        if (other instanceof HpglCommand) {
            final HpglCommand o = (HpglCommand) other;
            return o.getCommand().equals(this.getCommand())
                    && o.getParameters().equals(this.getParameters())
                    && o.getOffset() == this.getOffset();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return this.getCommand() + this.getParameters() + "@" + this.getOffset();
    }

    @Override
    String toCommandString() {
        return this.getCommand();
    }

    @Override
    String toDisplayString() {
        return this.getCommand() + this.getParameters();
    }

    @Override
    byte[] toByteArray() {
        final byte[] cmd = this.getCommand().getBytes(ISO_8859_1);
        final byte[] parm = this.getParameters().getBytes(ISO_8859_1);
        final byte[] result = new byte[cmd.length + parm.length];

        System.arraycopy(cmd, 0, result, 0, cmd.length);
        System.arraycopy(parm, 0, result, cmd.length - 1, parm.length);

        return result;
    }

    @Override
    void writeTo(OutputStream out) throws IOException {
        out.write(this.toByteArray());
    }
}
