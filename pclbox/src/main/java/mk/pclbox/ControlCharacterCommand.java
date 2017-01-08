package mk.pclbox;

/**
 * A {@link ControlCharacterCommand} describes a PCL 5 control character like form feed,
 * line feed, backspace and so on. It contains a single byte.
 */
public final class ControlCharacterCommand extends PrinterCommand {

    /**
     * Backspace. Moves the current active position (CAP) left a distance equal to
     * the width of the last printed symbol or space.
     */
    public static final int BACKSPACE = 8;

    /**
     * Moves the current active position (CAP) to the next tab stop on the current line.
     */
    public static final int HORIZONTAL_TAB = 9;

    /**
     * Line feed. Advances the current active position (CAP) to the same horizontal
     * position on the next line.
     */
    public static final int LINE_FEED = 10;

    /**
     * Form feed. Advances the current active position (CAP) to the same horizontal
     * position at the top of the text area on the next page.
     */
    public static final int FORM_FEED = 12;

    /**
     * Carriage return. Moves the current active position (CAP) to the left
     * margin on the current line.
     */
    public static final int CARRIAGE_RETURN = 13;

    /**
     * Shift out. Switch from the standard font to the alternate font.
     */
    public static final int SHIFT_OUT = 14;

    /**
     * Shift in. Switch from the alternate font to the standard font.
     */
    public static final int SHIFT_IN = 15;


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

    /**
     * Returns the control character byte of this {@link ControlCharacterCommand}.
     *
     * @return the control character byte
     */
    public byte getControlCharacter() {
        return this.controlCharacter;
    }

    @Override
    void accept(PrinterCommandVisitor visitor) {
        visitor.handle(this);
    }

    @Override
    public int hashCode() {
        return this.getControlCharacter() ^ this.getOffsetHash();
    }

    @Override
    public boolean equals(final Object other) {
        if (other instanceof ControlCharacterCommand) {
            final ControlCharacterCommand o = (ControlCharacterCommand) other;
            return o.getControlCharacter() == this.getControlCharacter() && o.getOffset() == this.getOffset();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.format("<0x%02X>@%d", this.getControlCharacter(), this.getOffset());
    }
}
