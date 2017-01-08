package mk.pclbox;

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

    private final int parameterizedCharacter;
    private final int groupCharacter;
    private final int terminationCharacter;
    private final String value;

    /**
     * Constructor of the {@link ParameterizedPclCommand}.
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

        super(offset);

        this.parameterizedCharacter = parameterizedCharacter;
        this.groupCharacter = groupCharacter;
        this.value = value.isEmpty() ? "0" : value;
        this.terminationCharacter = terminationCharacter;
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
                ^ this.getOffsetHash();
    }

    @Override
    public boolean equals(final Object other) {
        if (other instanceof ParameterizedPclCommand) {
            final ParameterizedPclCommand o = (ParameterizedPclCommand) other;
            return o.getValue().equals(this.getValue())
                    && o.getParameterizedCharacter() == this.getParameterizedCharacter()
                    && o.getGroupCharacter() == this.getGroupCharacter()
                    && o.getTerminationCharacter() == this.getTerminationCharacter()
                    && o.getOffset() == this.getOffset();
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
}
