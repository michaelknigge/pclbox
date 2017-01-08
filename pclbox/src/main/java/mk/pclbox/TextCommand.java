package mk.pclbox;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * A {@link TextCommand} contains text that has to be printed by the printer. The text
 * is handled as a byte[] and not a string because decoding of the byte[] depends on
 * the text parsing method that (maybe) has been set by a "Text Parsing Method" PCL command
 * in the data stream.
 */
public final class TextCommand extends PrinterCommand {

    final byte[] text;

    /**
     * Constructor of the {@link TextCommand}.
     *
     * @param offset - position within the data stream
     * @param text - the text
     */
    public TextCommand(long offset, final byte[] text) {
        super(offset);
        this.text = text.clone();
    }

    /**
     * Gets the text that has to be printed by a printer. Note that the text may be encoded
     * as a multi-byte text, depending on a "Text Parsing Method" that may be present in
     * the PCL printer data stream.
     *
     * @return the text as a byte array.
     */
    public byte[] getText() {
        return this.text.clone();
    }

    @Override
    void accept(PrinterCommandVisitor visitor) {
        visitor.handle(this);
    }

    @Override
    public int hashCode() {
        return this.getText().hashCode() ^ this.getOffsetHash();
    }

    @Override
    public boolean equals(final Object other) {
        if (other instanceof TextCommand) {
            final TextCommand o = (TextCommand) other;
            return Arrays.equals(o.getText(), this.getText()) && o.getOffset() == this.getOffset();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        try {
            return new String(this.getText(), "ISO-8859-1") + "@" + this.getOffset();
        } catch (final UnsupportedEncodingException e) {
            // Well.... this should really never get invoked...
            return Arrays.toString(this.getText()) + "@" + this.getOffset();
        }
    }
}
