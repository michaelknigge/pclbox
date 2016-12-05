package mk.pclbox;

/**
 * Exception for errors during parsing of PCL data streams.
 */
public final class PclException extends Exception {

    private static final long serialVersionUID = -8507460325800008788L;

    /**
     * Constructs an {@code PclException} with the specified detail message.
     *
     * @param message - the detail message.
     */
    public PclException(final String message) {
        super(message);
    }

}
