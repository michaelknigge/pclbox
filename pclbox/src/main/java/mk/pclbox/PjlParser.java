package mk.pclbox;

import java.io.IOException;

/**
 * This {@link PjlParser} parses PJL commands. It is constructed from the {@link Pcl5Parser}
 * and will return control to the {@link Pcl5Parser} after an escape bye (0x1B) has been read from
 * the data stream.
 */
final class PjlParser extends DataStreamParser {

    /**
     * Constructor. Just gets the stream.
     *
     * @param stream - the stream that contains the PJL commands.
     */
    PjlParser(final PclParserContext context) {
        super(context);
    }

    @Override
    public int parse() throws IOException, PclException {
        // TODO implement PJL parser!
        return -1;
    }
}
