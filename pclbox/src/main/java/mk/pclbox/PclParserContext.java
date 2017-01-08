package mk.pclbox;

/**
 * The {@link PclParserContext} contains all information that is required by the
 * concrete {@link DataStreamParser} implementations to do their job.
 */
final class PclParserContext {

    private final PclInputStream stream;
    private final PrinterCommandHandler commandHandler;

    /**
     * Constructor. Gets the {@link PclInputStream} we read from and initializes the
     * parser stack (pushes a {@link Pcl5Parser} on top of the stack).
     *
     * @param stream - the stream that contains the PCL printer data stream.
     */
    PclParserContext(final PclInputStream stream, final PrinterCommandHandler commandHandler) {
        this.stream = stream;
        this.commandHandler = commandHandler;
    }

    /**
     * Gets the {@link PclInputStream} to read from.
     *
     * @return the {@link PclInputStream} to read from.
     */
    PclInputStream getInputStream() {
        return this.stream;
    }

    /**
     * Gets the {@link PrinterCommandHandler} that handles the read {@link PrinterCommand}.
     *
     * @return the {@link PrinterCommandHandler}.
     */
    PrinterCommandHandler getPrinterCommandHandler() {
        return this.commandHandler;
    }
}
