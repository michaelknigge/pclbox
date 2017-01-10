package mk.pclbox;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

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
 * The {@link ParameterizedPclCommand} is a {@link PclCommand} that has a parameterized character
 * (ASCII range 33 to 47 decimal), followed by a group character (ASCII range 96 to 126 decimal).
 * After that two characters a numerical value is followed (a string that may be started with a
 * "+" or "-" and may also contain a decimal "."). After that value a termination character
 * follows (64 to 94 decimal).
 *
 * <p>Note that in the PCL data stream multiple of there PCL commands may be specified in a PCL
 * sequence, but pclbox transforms those sequences into multiple {@link ParameterizedPclCommand}
 * commands.
 */
public final class ParameterizedPclCommand extends PclCommand {

    private static final Charset ISO_8859_1 = Charset.forName("iso-8859-1");

    private final int parameterizedCharacter;
    private final int groupCharacter;
    private final int terminationCharacter;
    private final String value;
    private final byte[] dataSection;

    /**
     * Constructor of a {@link ParameterizedPclCommand}.
     *
     * @param offset - position within the data stream
     * @param parameterizedCharacter - the parameterized character of the PCL command (ASCII range 33 to 47)
     * @param groupCharacter - the group character of the PCL command (ASCII range 96 to 126) or 0 if the
     *     PCL command does not contain a group character.
     * @param value - the value string. If an empty string is given "0" is used as the value
     * @param terminationCharacter - the termination character of the PCL command (ASCII range 64 to 94)
     */
    public ParameterizedPclCommand(
            final long offset,
            final int parameterizedCharacter,
            final int groupCharacter,
            final String value,
            final int terminationCharacter) {

        this(offset, parameterizedCharacter, groupCharacter, value, terminationCharacter, null);
    }

    /**
     * Constructor of a {@link ParameterizedPclCommand} that has a data section.
     *
     * @param offset - position within the data stream
     * @param parameterizedCharacter - the parameterized character of the PCL command (ASCII range 33 to 47)
     * @param groupCharacter - the group character of the PCL command (ASCII range 96 to 126) or 0 if the
     *     PCL command does not contain a group character.
     * @param value - the value string. If an empty string is given "0" is used as the value
     * @param terminationCharacter - the termination character of the PCL command (ASCII range 64 to 94)
     * @param dataSection - some binary data that belongs to the PCL command (i. e. a binary font header)
     */
    public ParameterizedPclCommand(
            final long offset,
            final int parameterizedCharacter,
            final int groupCharacter,
            final String value,
            final int terminationCharacter,
            final byte[] dataSection) {

        super(offset);

        this.parameterizedCharacter = parameterizedCharacter;
        this.groupCharacter = groupCharacter;
        this.value = value.isEmpty() ? "0" : value;
        this.terminationCharacter = terminationCharacter;
        this.dataSection = dataSection == null ? null : dataSection.clone();
    }

    /**
     * Gets the parameterized character of the PCL command (ASCII range 33 to 47).
     *
     * @return the parameterized character
     */
    public int getParameterizedCharacter() {
        return this.parameterizedCharacter;
    }

    /**
     * Returns the group character of the PCL command (ASCII range 96 to 126) or 0 if the
     * PCL command does not contain a group character.
     *
     * @return the group character or 0 if the PCL command does not contain a group character.
     */
    public int getGroupCharacter() {
        return this.groupCharacter;
    }

    /**
     * Returns the termination character of the PCL command (ASCII range 64 to 94).
     *
     * @return the termination character
     */
    public int getTerminationCharacter() {
        return this.terminationCharacter;
    }

    /**
     * Gets the value string.
     *
     * @return the value (as a string).
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Gets the data section of the PCL command if the PCL command contains such a data section.
     *
     * @return the data section or <code>null</code> if the PCL command does not contain a data section.
     */
    public byte[] getDataSection() {
        return this.dataSection == null ? null : this.dataSection.clone();
    }

    @Override
    void accept(PrinterCommandVisitor visitor) {
        visitor.handle(this);
    }

    @Override
    public int hashCode() {
        return this.getValue().hashCode()
                ^ this.getParameterizedCharacter()
                ^ this.getGroupCharacter()
                ^ this.getTerminationCharacter()
                ^ this.getOffsetHash()
                ^ Arrays.hashCode(this.dataSection); //do not use the getter - the getter clones!
    }

    @Override
    public boolean equals(final Object other) {
        if (other instanceof ParameterizedPclCommand) {
            final ParameterizedPclCommand o = (ParameterizedPclCommand) other;
            return o.getValue().equals(this.getValue())
                    && o.getParameterizedCharacter() == this.getParameterizedCharacter()
                    && o.getGroupCharacter() == this.getGroupCharacter()
                    && o.getTerminationCharacter() == this.getTerminationCharacter()
                    && o.getOffset() == this.getOffset()
                    && Arrays.equals(o.dataSection, this.dataSection); //do not use the getter - the getter clones!
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("<esc>");
        sb.append((char) this.getParameterizedCharacter());

        if (this.getGroupCharacter() != 0x00) {
            sb.append((char) this.getGroupCharacter());
        }

        sb.append(this.getValue());
        sb.append((char) this.getTerminationCharacter());
        sb.append("@");
        sb.append(this.getOffset());

        return sb.toString();
    }

    @Override
    String toCommandString() {
        final StringBuilder sb = new StringBuilder();
        sb.append((char) this.getParameterizedCharacter());

        if (this.getGroupCharacter() != 0x00) {
            sb.append((char) this.getGroupCharacter());
        }

        sb.append((char) this.getTerminationCharacter());

        return sb.toString();
    }

    @Override
    String toDisplayString() {
        final StringBuilder sb = new StringBuilder();
        sb.append((char) this.getParameterizedCharacter());

        if (this.getGroupCharacter() != 0x00) {
            sb.append((char) this.getGroupCharacter());
        }

        sb.append(this.getValue());
        sb.append((char) this.getTerminationCharacter());

        return sb.toString();
    }

    @Override
    byte[] toByteArray() {
        final String asString = this.toDisplayString();
        final byte[] asByteArray = asString.getBytes(ISO_8859_1);

        final int resultSize = 1 // for the escape byte
                + asByteArray.length
                + (this.dataSection != null ? this.dataSection.length : 0);

        final byte[] result = new byte[resultSize];

        result[0] = 0x1B;
        System.arraycopy(asByteArray, 0, result, 1, asByteArray.length);

        if (this.dataSection != null) {
            System.arraycopy(this.dataSection, 0, result, asByteArray.length, this.dataSection.length);
        }

        return result;
    }

    @Override
    void writeTo(OutputStream out) throws IOException {
        out.write(this.toByteArray());
    }
}
