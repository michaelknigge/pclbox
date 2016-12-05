package mk.pclbox;

import java.io.Closeable;
import java.io.IOException;
import java.util.Stack;

/**
 * The {@link PclParserContext} contains all information that is required by the
 * concrete {@link DataStreamParser} implementations to do their job.
 */
final class PclParserContext implements Closeable {

    // The idea is to stack all required parsers. PCL may include HP/GL2, so we just put
    // at first the PCL5 parser on the stack. If this parser encounters HP/GL2, a HP/GL2
    // parser is pushed on the stack. If the HP/GL2 is finished with its work, than the
    // parser is popped from the stack - now the PCL5 parser is the actual parser again...
    //
    // This may sound oversized, but maybe we will support PCL-XL sometimes. Then PCL-XL
    // may contain PCL5 and the contained PCL5 may contain HP/GL2.... So at least then
    // the approach with this stacked parsers may be useful...
    final Stack<DataStreamParser> parsers;

    // This is the first parser on the stack. We use this parser to close the underlying
    // PclInputStream.
    final DataStreamParser baseParser;

    /**
     * Constructor. Gets the {@link PclInputStream} we read from and initializes the
     * parser stack (pushes a {@link Pcl5Parser} on top of the stack).
     *
     * @param stream - the stream that contains the PCL printer data stream.
     */
    PclParserContext(final PclInputStream stream) {
        this.baseParser = new Pcl5Parser(stream);
        this.parsers = new Stack<>();
        this.parsers.push(this.baseParser);
    }

    /**
     * Pushes a new {@link DataStreamParser} on top of the parser stack. The pushed {@link DataStreamParser}
     * becomes the new current parser used for parsing the printer data stream.
     *
     * @param parser - the new {@link DataStreamParser} used for parsing the printer data stream.
     */
    void pushParserOnParserStack(final DataStreamParser parser) {
        this.parsers.push(parser);
    }

    /**
     * Pops (removes) the current {@link DataStreamParser} off the parser stack. The {@link DataStreamParser} that is
     * now on top of the parser stack becomes the new current parser used for parsing the printer data stream.
     *
     * @return the new current parser used for parsing the printer data stream.
     */
    DataStreamParser popParserOffParserStack() {
        this.parsers.pop();
        return this.parsers.peek();
    }

    /**
     * Gets the current {@link DataStreamParser} from the parser stack.
     *
     * @return the current {@link DataStreamParser} from the parser stack.
     */
    DataStreamParser getCurrentParser() {
        return this.parsers.peek();
    }

    @Override
    public void close() throws IOException {
        this.baseParser.close();
    }
}
