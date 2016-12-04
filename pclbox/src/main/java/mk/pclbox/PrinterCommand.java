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
     * Delegates the concrete {@link PrinterCommand} to the corresponding handle
     * method of the {@link PrinterCommandVisitor}.
     *
     * @param visitor a {@link PrinterCommandVisitor}.
     */
    abstract void accept(PrinterCommandVisitor visitor);
}
