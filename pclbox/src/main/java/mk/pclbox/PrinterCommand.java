package mk.pclbox;

/**
 * Abstract class that every {@link PrinterCommand} has to implement.
 */
abstract class PrinterCommand {

    private final long offset;

    /**
     * Constructor. Gets just the offset of the {@link PrinterCommand}.
     *
     * @param offset - offset of this {@link PrinterCommand}, measured from the beginning of the read data stream.
     */
    public PrinterCommand(final long offset) {
        this.offset = offset;
    }

    /**
     * Gets the offset of this {@link PrinterCommand}, measured from the beginning
     * of the read data stream.
     *
     * @return the offset of the {@link PrinterCommand}.
     */
    public long getOffset() {
        return this.offset;
    }

    /**
     * Calculates a hash for the offset.
     *
     * @return the hash code (32bit) for the offset (64 bit).
     */
    protected int getOffsetHash() {
        return (int) (this.offset ^ (this.offset >>> 32));
    }

    /**
     * Delegates the concrete {@link PrinterCommand} to the corresponding handle
     * method of the {@link PrinterCommandVisitor}.
     *
     * @param visitor a {@link PrinterCommandVisitor}.
     */
    abstract void accept(PrinterCommandVisitor visitor);
}
