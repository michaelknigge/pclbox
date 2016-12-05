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
     * @param groupCharacter - the group character of the PCL command (ASCII range 96 to 126)
     * @param value - the value string
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
        this.value = value;
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
     * Returns the group character of the PCL command (ASCII range 96 to 126).
     *
     * @return the group character
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
        visitor.accept(this);
    }
}
