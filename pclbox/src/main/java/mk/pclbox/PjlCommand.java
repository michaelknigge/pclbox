package mk.pclbox;

/**
 * A {@link PjlCommand} describes a PJL command.
 */
public final class PjlCommand extends PrinterCommand {

    final String commandString;

    /**
     * Constructor for the {@link PjlCommand}.
     *
     * @param offset - position within the data stream
     */
    public PjlCommand(final long offset, final String commandString) {
        super(offset);
        this.commandString = commandString;
    }

    /**
     * Gets the command string, not including the "@PJL " prefix.
     *
     * @return the command string.
     */
    public String getCommand() {
        return this.commandString;
    }

    @Override
    void accept(PrinterCommandVisitor visitor) {
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

}