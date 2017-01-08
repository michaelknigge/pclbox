package mk.pclbox;

/**
 * The {@link TwoBytePclCommand} is a {@link PclCommand} that consists of just two bytes:
 * The introducting escape byte (0x1B), followed by a single byte within the range
 * 48 to 126 decimal. The most known two byte command is probably the command "Printer
 * Reset", which is the escape byte followed by a "E".
 */
public final class TwoBytePclCommand extends PclCommand {

    private final int operationCharacter;

    /**
     * Constructor of the {@link TwoBytePclCommand}.
     *
     * @param offset - position within the data stream
     * @param operationCharacter - the operation character (ASCII range 48 to 126, which is "0" to "~").
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
    void accept(PrinterCommandVisitor visitor) {
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
}
