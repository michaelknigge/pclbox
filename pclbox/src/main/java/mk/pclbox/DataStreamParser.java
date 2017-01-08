package mk.pclbox;

import java.io.IOException;

/**
 * Superclass of all printer data stream parsers.
 */
abstract class DataStreamParser {

    private final PclParserContext ctx;

    /**
     * Constructor.
     *
     * @param ctx - the {@link PclParserContext} that holds all information that is required
     *     by the concrete {@link DataStreamParser}.
     */
    DataStreamParser(final PclParserContext ctx) {
        this.ctx = ctx;
    }

    /**
     * Gets the {@link PclInputStream} to read from.
     *
     * @return the {@link PclInputStream} to read from.
     */
    PclInputStream getInputStream() {
        return this.ctx.getInputStream();
    }

    /**
     * Gets the {@link PrinterCommandHandler} that handles the read {@link PrinterCommand}.
     *
     * @return the {@link PrinterCommandHandler}.
     */
    PrinterCommandHandler getPrinterCommandHandler() {
        return this.ctx.getPrinterCommandHandler();
    }

    /**
     * Parses the data stream. For every parsed {@link PrinterCommand} the {@link PrinterCommandHandler} is invoked.
     *
     * @return the last byte read from the data stream that has caused the parser to stop (i. e. the PJL parser
     * will return 0x1B if it hits the first PCL command). This may also be -1 if the end of the data stream
     * has been reached.
     */
    abstract int parse() throws IOException, PclException;
}
