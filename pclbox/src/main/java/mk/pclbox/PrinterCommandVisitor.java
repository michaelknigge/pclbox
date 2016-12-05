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

    /**
     * Handle method for {@link TwoBytePclCommand} objects.
     */
    void accept(final TwoBytePclCommand twoBytePclCommand);

    /**
     * Handle method for {@link ParameterizedPclCommand} objects.
     */
    void accept(final ParameterizedPclCommand parameterizedPclCommand);
}
