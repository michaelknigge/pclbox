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

        assert operationCharacter >= 48 && operationCharacter <= 126;
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
        visitor.accept(this);
    }
}
