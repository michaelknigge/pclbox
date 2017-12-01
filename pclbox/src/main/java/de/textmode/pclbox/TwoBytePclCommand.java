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
 * The {@link TwoBytePclCommand} is a {@link Pcl5Command} that consists of just two bytes:
 * The introducing escape byte (0x1B), followed by a single byte within the range
 * 48 to 126 decimal. The most known two byte command is probably the command "Printer
 * Reset", which is the escape byte followed by a "E".
 */
public final class TwoBytePclCommand extends Pcl5Command {

    private static final Charset ISO_8859_1 = Charset.forName("iso-8859-1");

    private final int operationCharacter;

    /**
     * Constructor of the {@link TwoBytePclCommand}.
     *
     * @param offset   position within the data stream
     * @param operationCharacter   the operation character (ASCII range 48 to 126, which is "0" to "~").
     */
    public TwoBytePclCommand(long offset, final int operationCharacter) {
        super(offset);
        this.operationCharacter = operationCharacter;
    }

    /**
     * Gets the operation character, which is in the range 48 to 126 (which is "0" to "~").
     *
     * @return the operation character.
     */
    public int getOperationCharacter() {
        return this.operationCharacter;
    }

    @Override
    public void accept(PrinterCommandVisitor visitor) {
        visitor.handle(this);
    }

    @Override
    public int hashCode() {
        return this.getOperationCharacter() ^ this.getOffsetHash();
    }

    @Override
    public boolean equals(final Object other) {
        if (other instanceof TwoBytePclCommand) {
            final TwoBytePclCommand o = (TwoBytePclCommand) other;
            return o.getOperationCharacter() == this.getOperationCharacter() && o.getOffset() == this.getOffset();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("<esc>");
        sb.append((char) this.getOperationCharacter());
        sb.append("@");
        sb.append(this.getOffset());
        return sb.toString();
    }

    @Override
    public String toCommandString() {
        return new String(new byte[] { (byte) this.getOperationCharacter() }, ISO_8859_1);
    }

    @Override
    public String toDisplayString() {
        return new String(new byte[] { (byte) this.getOperationCharacter() }, ISO_8859_1);
    }

    @Override
    public byte[] toByteArray() {
        return new byte[] { (byte) 0x1B, (byte) this.getOperationCharacter() };
    }

    @Override
    public void writeTo(OutputStream out) throws IOException {
        out.write((char) 0x1B);
        out.write(this.getOperationCharacter());
    }
}
