package mk.pclbox;

/**
 * A {@link ControlCharacterCommand} describes a PCL 5 control character like form feed,
 * line feed, backspace and so on. It contains a single byte.
 */
public final class ControlCharacterCommand extends PrinterCommand {

    private final byte controlCharacter;

    /**
     * Constructor of the {@link ControlCharacterCommand}.
     *
     * @param offset - position within the data stream
     * @param controlCharacter - the control character
     */
    public ControlCharacterCommand(final long offset, final byte controlCharacter) {
        super(offset);
        this.controlCharacter = controlCharacter;
    }

    public byte getControlCharacter() {
        return this.controlCharacter;
    }
}
