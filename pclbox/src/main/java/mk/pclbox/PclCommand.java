package mk.pclbox;

/**
 * A {@link PclCommand} describes a PCL 5 command.
 */
abstract class PclCommand extends PrinterCommand {

    /**
     * Constructor for the {@link PclCommand}.
     *
     * @param offset - position within the data stream
     */
    public PclCommand(long offset) {
        super(offset);
    }
}
