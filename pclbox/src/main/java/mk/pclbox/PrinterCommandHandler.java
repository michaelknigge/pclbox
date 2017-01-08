package mk.pclbox;

/**
 * This is the handler interface for every constructed {@link PrinterCommand}.
 */
public interface PrinterCommandHandler {

    /**
     * Handles the {@link PrinterCommand}.
     */
    void handlePrinterCommand(final PrinterCommand command);
}
