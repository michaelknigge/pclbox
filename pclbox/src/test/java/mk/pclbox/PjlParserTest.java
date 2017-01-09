package mk.pclbox;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

/**
 * JUnit-Tests for {@link PjlParser}.
 */
public final class PjlParserTest extends DataStreamParserTest {

    /**
     * Creates a {@link PjlParser} for parsing the given PJL data stream.
     */
    private final PjlParser getPjlParserFor(final String data) throws UnsupportedEncodingException {
        final ByteArrayInputStream input = new ByteArrayInputStream(data.getBytes("iso-8859-1"));
        final PclInputStreamForInputStream pclStream = new PclInputStreamForInputStream(input);

        return new PjlParser(new PclParserContext(pclStream, this));
    }

    /**
     * Checks parsing of a complete empty stream (zero file size).
     */
    public void testEmptyStream() throws Exception {
        this.getPjlParserFor("").parse();
        assertTrue(this.getCommands().isEmpty());
    }

    /**
     * Checks parsing of a stream that contains just the PJL prefix without an ending line feed.
     */
    public void testNotTerminatedProperly() throws Exception {
        try {
            this.getPjlParserFor("@PJL").parse();
            fail("Should fail because the PJL command does not end with a line feed");
        } catch (final PclException e) {
            assertTrue(e.getMessage().contains("is not properly terminated"));
        }
    }

    /**
     * Checks parsing of a stream that contains just a (properly ended) PJL prefix.
     */
    public void testSimplePjl() throws Exception {
        this.getPjlParserFor("@PJL\n").parse();

        assertEquals(
                buildExpected(
                        new PjlCommand(0, "@PJL")),
                this.getCommands());
    }

    /**
     * Checks parsing of multiple commands.
     */
    public void testMultipleCommands() throws Exception {
        this.getPjlParserFor("@PJL\n@PJL COMMENT foo\n").parse();

        assertEquals(
                buildExpected(
                        new PjlCommand(0, "@PJL"),
                        new PjlCommand(5, "@PJL COMMENT foo")),
                this.getCommands());
    }

    /**
     * Checks that trailing spaces are removed.
     */
    public void testCommandsAreTrimmed() throws Exception {
        this.getPjlParserFor("@PJL   \n@PJL COMMENT foo   \n").parse();

        assertEquals(
                buildExpected(
                        new PjlCommand(0, "@PJL"),
                        new PjlCommand(8, "@PJL COMMENT foo")),
                this.getCommands());
    }

    /**
     * Checks that carriage return is stripped off.
     */
    public void testCarriageReturnIsIgnored() throws Exception {
        this.getPjlParserFor("@PJL\r\n@PJL COMMENT foo\n@PJL COMMENT ba\r\n").parse();

        assertEquals(
                buildExpected(
                        new PjlCommand(0, "@PJL"),
                        new PjlCommand(6, "@PJL COMMENT foo"),
                        new PjlCommand(23, "@PJL COMMENT ba")),
                this.getCommands());
    }
}
