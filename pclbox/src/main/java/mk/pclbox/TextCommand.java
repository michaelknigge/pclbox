package mk.pclbox;

/**
 * A {@link TextCommand} contains text that has to be printed by the printer. The text
 * is handled as a byte[] and not a string because decoding of the byte[] depends on
 * the text parsing method that (maybe) has been set by a "Text Parsing Method" PCL command
 * in the data stream.
 */
public final class TextCommand extends PrinterCommand {

    final byte[] text;

    public TextCommand(long offset, final byte[] text) {
        super(offset);
        this.text = text.clone();
    }

    /**
     * Gets the text that has to be printed by a printer. Note that the text may be encoded
     * as a multi-byte text, depending on a "Text Parsing Method" that may be present in
     * the PCL printer data stream.
     */
    public byte[] getText() {
        return this.text.clone();
    }

}
