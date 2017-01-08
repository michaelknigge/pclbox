package mk.pclbox;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * JUnit-Tests for {@link Pcl5Parser}.
 */
public final class Pcl5ParserTest extends TestCase implements PrinterCommandHandler {

    private static final List<PrinterCommand> COMMANDS = new ArrayList<PrinterCommand>();

    @Override
    protected void setUp() {
        COMMANDS.clear();
    }

    @Override
    protected void tearDown() {
        COMMANDS.clear();
    }

    @Override
    public void handlePrinterCommand(PrinterCommand command) {
        assert command != null;
        COMMANDS.add(command);
    }

    private static List<PrinterCommand> buildExpected(final PrinterCommand... commands) {
        final List<PrinterCommand> result = new ArrayList<>();
        for (final PrinterCommand command : commands) {
            result.add(command);
        }
        return result;
    }

    /**
     * Creates a {@link Pcl5Parser} for parsing the given PCL data stream. The data stream
     * is provided as a string that may contain several "hot characters". The "~" is replaced by
     * the escape character and the "#" is replaced by a form feed.
     */
    private final Pcl5Parser getPcl5ParserFor(final String data) throws UnsupportedEncodingException {
        final byte[] bytes = data.replace('~', (char) 0x1B).replace('#', (char) 0x0C).getBytes("iso-8859-1");

        final ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        final PclInputStreamForInputStream pclStream = new PclInputStreamForInputStream(input);

        return new Pcl5Parser(new PclParserContext(pclStream, this));
    }

    /**
     * Checks parsing of a complete empty stream (zero file size).
     */
    public void testEmptyStream() throws Exception {
        final ByteArrayInputStream data = new ByteArrayInputStream(new byte[0]);
        final PclInputStreamForInputStream pclStream = new PclInputStreamForInputStream(data);
        new Pcl5Parser(new PclParserContext(pclStream, this)).parse();

        assertTrue(COMMANDS.isEmpty());
    }

    /**
     * Checks parsing of a stream that contains just a single control character.
     */
    public void testJustOneControlCharacter() throws Exception {
        final ByteArrayInputStream data = new ByteArrayInputStream(new byte[] { (byte) 0x0C });
        final PclInputStreamForInputStream pclStream = new PclInputStreamForInputStream(data);
        new Pcl5Parser(new PclParserContext(pclStream, this)).parse();

        assertEquals(
                buildExpected(
                        new ControlCharacterCommand(0, (byte) 0x0C)),
                COMMANDS);
    }

    /**
     * Checks parsing of a stream that contains only text.
     */
    public void testOnlyText() throws Exception {
        final byte[] text = "123".getBytes("utf-8");
        final ByteArrayInputStream data = new ByteArrayInputStream(text);
        final PclInputStreamForInputStream pclStream = new PclInputStreamForInputStream(data);
        new Pcl5Parser(new PclParserContext(pclStream, this)).parse();

        assertEquals(
                buildExpected(
                        new TextCommand(0, text)),
                COMMANDS);
    }

    /**
     * Checks parsing of a stream that contains text and control characters.
     */
    public void testTextAndControlCharacters() throws Exception {
        final byte[] text = { 0x31, 0x32, 0x33, 0x0D, 0x0A, 0x34, 0x35, 0x36, 0x0C };
        final ByteArrayInputStream data = new ByteArrayInputStream(text);
        final PclInputStreamForInputStream pclStream = new PclInputStreamForInputStream(data);
        new Pcl5Parser(new PclParserContext(pclStream, this)).parse();

        assertEquals(
                buildExpected(
                        new TextCommand(0, new byte[] { 0x31, 0x32, 0x33 }),
                        new ControlCharacterCommand(3, (byte) 0x0D),
                        new ControlCharacterCommand(4, (byte) 0x0A),
                        new TextCommand(5, new byte[] { 0x34, 0x35, 0x36 }),
                        new ControlCharacterCommand(8, (byte) 0x0C)),
                COMMANDS);
    }

    /**
     * Checks that 0xB (Vertical Tab) is not handled as a control character.
     */
    public void testVerticalTabIsNoControlCharacter() throws Exception {
        final ByteArrayInputStream data = new ByteArrayInputStream(new byte[] { (byte) 0x0B });
        final PclInputStreamForInputStream pclStream = new PclInputStreamForInputStream(data);
        new Pcl5Parser(new PclParserContext(pclStream, this)).parse();

        assertEquals(
                buildExpected(
                        new TextCommand(0, new byte[] { 0x0B })),
                COMMANDS);
    }

    /**
     * Checks that two byte commands are parsed correctly.
     */
    public void testTwoByteCommands() throws Exception {
        final byte[] text = { 0x1B, 0x45, 0x1B, 0x45, 0x20, 0x20, 0x1B, 0x39 };
        final ByteArrayInputStream data = new ByteArrayInputStream(text);
        final PclInputStreamForInputStream pclStream = new PclInputStreamForInputStream(data);
        new Pcl5Parser(new PclParserContext(pclStream, this)).parse();

        assertEquals(
                buildExpected(
                        new TwoBytePclCommand(0, 0x45),
                        new TwoBytePclCommand(2, 0x45),
                        new TextCommand(4, new byte[] { 0x20, 0x20 }),
                        new TwoBytePclCommand(6, 0x39)),
                COMMANDS);
    }

    /**
     * Checks that parameterized PCL commands are parsed correctly.
     */
    public void testParameterizedPclCommands() throws Exception {
        this.getPcl5ParserFor("~E~&u300D").parse();

        assertEquals(
                buildExpected(
                        new TwoBytePclCommand(0, 'E'),
                        new ParameterizedPclCommand(2, '&', 'u', "300", 'D')),
                COMMANDS);
    }

    /**
     * A parameterized PCL command that ends with an lower case letter is treated "ok"...
     */
    public void testUncorrectlyTerminatedParameterizedPclCommands() throws Exception {
        this.getPcl5ParserFor("~E~&u300d~&u600d").parse();

        assertEquals(
                buildExpected(
                        new TwoBytePclCommand(0, 'E'),
                        new ParameterizedPclCommand(2, '&', 'u', "300", 'D'),
                        new ParameterizedPclCommand(9, '&', 'u', "600", 'D')),
                COMMANDS);
    }

    /**
     * Checks that parameterized PCL sequence is parsed correctly.
     */
    public void testParameterizedPclSequence() throws Exception {
        this.getPcl5ParserFor("~E~*p100x200Y").parse();

        assertEquals(
                buildExpected(
                        new TwoBytePclCommand(0, 'E'),
                        new ParameterizedPclCommand(2, '*', 'p', "100", 'X'),
                        new ParameterizedPclCommand(9, '*', 'p', "200", 'Y')),
                COMMANDS);
    }

    /**
     * Checks that an unexpected end of the stream triggers an exception.
     */
    public void testUnexpectedEndOfStreamAfterEscapeByte() throws Exception {
        try {
            this.getPcl5ParserFor("~E~").parse();
            fail("Should fail because the data stream ends after an escape byte.");
        } catch (final EOFException e) {
            assertTrue(e.getMessage().contains("unexpectedly ends"));
        }

        assertEquals(
                buildExpected(
                        new TwoBytePclCommand(0, 'E')),
                COMMANDS);
    }

    /**
     * Checks that an unexpected end of the stream triggers an exception.
     */
    public void testUnexpectedEndOfStreamAfterParameterizedCharacter() throws Exception {
        try {
            this.getPcl5ParserFor("~*").parse();
            fail("Should fail because the data stream ends after an escape byte.");
        } catch (final EOFException e) {
            assertTrue(e.getMessage().contains("unexpectedly ends"));
        }

        assertTrue(COMMANDS.isEmpty());
    }

    /**
     * Checks that an unexpected end of the stream triggers an exception.
     */
    public void testUnexpectedEndOfStreamAfterGroupCharacter() throws Exception {
        try {
            this.getPcl5ParserFor("~*p").parse();
            fail("Should fail because the data stream ends after the group character.");
        } catch (final EOFException e) {
            assertTrue(e.getMessage().contains("unexpectedly ends"));
        }

        assertTrue(COMMANDS.isEmpty());
    }

    /**
     * Checks that an unexpected end of the stream triggers an exception.
     */
    public void testUnexpectedEndOfStreamAfterValueString() throws Exception {
        try {
            this.getPcl5ParserFor("~*p100").parse();
            fail("Should fail because the data stream ends after the value.");
        } catch (final EOFException e) {
            assertTrue(e.getMessage().contains("unexpectedly ends"));
        }

        assertTrue(COMMANDS.isEmpty());
    }

    /**
     * Checks that an invalid parameterized character triggers an exception.
     */
    public void testInvalidParameterizedCharacter() throws Exception {
        try {
            this.getPcl5ParserFor("~E~°").parse();
            fail("Should fail because the parameterized character is invalid.");
        } catch (final PclException e) {
            assertTrue(e.getMessage().contains(" parameterized character "));
        }

        assertEquals(
                buildExpected(
                        new TwoBytePclCommand(0, 'E')),
                COMMANDS);
    }

    /**
     * Checks that an invalid group character triggers an exception.
     */
    public void testInvalidGroupCharacter() throws Exception {
        try {
            this.getPcl5ParserFor("~E~*°").parse();
            fail("Should fail because the group character is invalid.");
        } catch (final PclException e) {
            assertTrue(e.getMessage().equals("The byte value of the character at offset 4 is invalid."));
        }

        assertEquals(
                buildExpected(
                        new TwoBytePclCommand(0, 'E')),
                COMMANDS);
    }

    /**
     * Checks that a missing value is ok (see "Underline disable" PCL command).
     */
    public void testMissingValueIsOk() throws Exception {
        this.getPcl5ParserFor("~&d@~(spH").parse();

        assertEquals(
                buildExpected(
                        new ParameterizedPclCommand(0, '&', 'd', "", '@'),
                        new ParameterizedPclCommand(4, '(', 's', "", 'P'),
                        new ParameterizedPclCommand(8, '(', 's', "", 'H')),
                COMMANDS);
    }

    /**
     * Checks that a value with sign and/or decimal places is is ok (see "Vertical Cursor
     * Positioning (Rows) Command" PCL command).
     */
    public void testSignAndDecimalPlaces() throws Exception {
        this.getPcl5ParserFor("~&a100R~&a+100R~&a-100R~&a100.5R~&a-100.541R~&a+100.001R").parse();

        assertEquals(
                buildExpected(
                        new ParameterizedPclCommand(0, '&', 'a', "100", 'R'),
                        new ParameterizedPclCommand(7, '&', 'a', "+100", 'R'),
                        new ParameterizedPclCommand(15, '&', 'a', "-100", 'R'),
                        new ParameterizedPclCommand(23, '&', 'a', "100.5", 'R'),
                        new ParameterizedPclCommand(32, '&', 'a', "-100.541", 'R'),
                        new ParameterizedPclCommand(44, '&', 'a', "+100.001", 'R')),
                COMMANDS);
    }
}
