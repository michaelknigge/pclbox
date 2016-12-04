package mk.pclbox;

/**
 * Visitor for all concrete implementations of {@link PrinterCommand}.
 */
public interface PrinterCommandVisitor {

    /**
     * Handle method for {@link TextCommand} objects.
     */
    void handle(final TextCommand command);

    /**
     * Handle method for {@link ControlCharacterCommand} objects.
     */
    void handle(final ControlCharacterCommand command);
}
