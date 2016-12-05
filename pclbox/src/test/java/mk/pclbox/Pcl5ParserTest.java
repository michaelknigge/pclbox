package mk.pclbox;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;

/**
 * JUnit-Tests for {@link Pcl5Parser}.
 */
public final class Pcl5ParserTest extends TestCase {

    /**
     * Creates a {@link Pcl5Parser} for parsing the given PCL data stream. The data stream
     * is provided as a string that may contain several "hot characters". The "~" is replaced by
     * the escape character and the "#" is replaced by a form feed.
     */
    private static final Pcl5Parser getPcl5ParserFor(final String data) throws UnsupportedEncodingException {
        final byte[] bytes = data.replace('~', (char) 0x1B).replace('#', (char) 0x0C).getBytes("iso-8859-1");

        final ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        final PclInputStreamForInputStream pclStream = new PclInputStreamForInputStream(input);

        return new Pcl5Parser(pclStream);
    }

    /**
     * Checks parsing of a complete empty stream (zero file size).
     */
    public void testEmptyStream() throws Exception {
        final ByteArrayInputStream data = new ByteArrayInputStream(new byte[0]);
        final PclInputStreamForInputStream pclStream = new PclInputStreamForInputStream(data);
        final Pcl5Parser parser = new Pcl5Parser(pclStream);
        assertNull(parser.parseNextPrinterCommand());
        parser.close();
    }

    /**
     * Checks parsing of a stream that contains just a single control character.
     */
    public void testJustOneControlCharacter() throws Exception {
        final ByteArrayInputStream data = new ByteArrayInputStream(new byte[] { (byte) 0x0C });
        final PclInputStreamForInputStream pclStream = new PclInputStreamForInputStream(data);
        final Pcl5Parser parser = new Pcl5Parser(pclStream);

        final ControlCharacterCommand cc = (ControlCharacterCommand) parser.parseNextPrinterCommand();
        assertEquals(0x0C, cc.getControlCharacter());
        assertEquals(0, cc.getOffset());

        assertNull(parser.parseNextPrinterCommand());
        parser.close();
    }

    /**
     * Checks parsing of a stream that contains only text.
     */
    public void testOnlyText() throws Exception {
        final byte[] text = "123".getBytes("utf-8");
        final ByteArrayInputStream data = new ByteArrayInputStream(text);
        final PclInputStreamForInputStream pclStream = new PclInputStreamForInputStream(data);
        final Pcl5Parser parser = new Pcl5Parser(pclStream);

        final TextCommand tc = (TextCommand) parser.parseNextPrinterCommand();
        assertEquals(0, tc.getOffset());
        assertEquals(text[0], tc.getText()[0]);
        assertEquals(text[1], tc.getText()[1]);
        assertEquals(text[2], tc.getText()[2]);

        assertNull(parser.parseNextPrinterCommand());
        parser.close();
    }

    /**
     * Checks parsing of a stream that contains text and control characters.
     */
    public void testTextAndControlCharacters() throws Exception {
        final byte[] text = { 0x31, 0x32, 0x33, 0x0D, 0x0A, 0x34, 0x35, 0x36, 0x0C };
        final ByteArrayInputStream data = new ByteArrayInputStream(text);
        final PclInputStreamForInputStream pclStream = new PclInputStreamForInputStream(data);
        final Pcl5Parser parser = new Pcl5Parser(pclStream);

        final TextCommand tc1 = (TextCommand) parser.parseNextPrinterCommand();
        assertEquals(0, tc1.getOffset());
        assertEquals(text[0], tc1.getText()[0]);
        assertEquals(text[1], tc1.getText()[1]);
        assertEquals(text[2], tc1.getText()[2]);

        final ControlCharacterCommand cc1 = (ControlCharacterCommand) parser.parseNextPrinterCommand();
        assertEquals(0x0D, cc1.getControlCharacter());
        assertEquals(3, cc1.getOffset());

        final ControlCharacterCommand cc2 = (ControlCharacterCommand) parser.parseNextPrinterCommand();
        assertEquals(0x0A, cc2.getControlCharacter());
        assertEquals(4, cc2.getOffset());

        final TextCommand tc2 = (TextCommand) parser.parseNextPrinterCommand();
        assertEquals(5, tc2.getOffset());
        assertEquals(text[5], tc2.getText()[0]);
        assertEquals(text[6], tc2.getText()[1]);
        assertEquals(text[7], tc2.getText()[2]);

        final ControlCharacterCommand cc3 = (ControlCharacterCommand) parser.parseNextPrinterCommand();
        assertEquals(0x0C, cc3.getControlCharacter());
        assertEquals(8, cc3.getOffset());

        assertNull(parser.parseNextPrinterCommand());
        parser.close();
    }

    /**
     * Checks that 0xB (Vertical Tab) is not handled as a control character.
     */
    public void testVerticalTabIsNoControlCharacter() throws Exception {
        final ByteArrayInputStream data = new ByteArrayInputStream(new byte[] { (byte) 0x0B });
        final PclInputStreamForInputStream pclStream = new PclInputStreamForInputStream(data);
        final Pcl5Parser parser = new Pcl5Parser(pclStream);

        final TextCommand tc = (TextCommand) parser.parseNextPrinterCommand();
        assertEquals(0x0B, tc.getText()[0]);
        assertEquals(0, tc.getOffset());

        assertNull(parser.parseNextPrinterCommand());
        parser.close();
    }

    /**
     * Checks that two byte commands are parsed correctly.
     */
    public void testTwoByteCommands() throws Exception {
        final byte[] text = { 0x1B, 0x45, 0x1B, 0x45, 0x20, 0x20, 0x1B, 0x39 };
        final ByteArrayInputStream data = new ByteArrayInputStream(text);
        final PclInputStreamForInputStream pclStream = new PclInputStreamForInputStream(data);
        final Pcl5Parser parser = new Pcl5Parser(pclStream);

        final TwoBytePclCommand cmd1 = (TwoBytePclCommand) parser.parseNextPrinterCommand();
        assertEquals(0, cmd1.getOffset());
        assertEquals(0x45, cmd1.getOperationCharacter());

        final TwoBytePclCommand cmd2 = (TwoBytePclCommand) parser.parseNextPrinterCommand();
        assertEquals(2, cmd2.getOffset());
        assertEquals(0x45, cmd2.getOperationCharacter());

        final TextCommand cmd3 = (TextCommand) parser.parseNextPrinterCommand();
        assertEquals(4, cmd3.getOffset());
        assertEquals(text[4], cmd3.getText()[0]);
        assertEquals(text[5], cmd3.getText()[1]);

        final TwoBytePclCommand cmd4 = (TwoBytePclCommand) parser.parseNextPrinterCommand();
        assertEquals(6, cmd4.getOffset());
        assertEquals(0x39, cmd4.getOperationCharacter());

        assertNull(parser.parseNextPrinterCommand());
        parser.close();
    }

    /**
     * Checks that parameterized PCL commands are parsed correctly.
     */
    public void testParameterizedPclCommands() throws Exception {
        final Pcl5Parser parser = getPcl5ParserFor("~E~&u300D");

        final TwoBytePclCommand cmd1 = (TwoBytePclCommand) parser.parseNextPrinterCommand();
        assertEquals(0, cmd1.getOffset());
        assertEquals('E', cmd1.getOperationCharacter());

        final ParameterizedPclCommand cmd2 = (ParameterizedPclCommand) parser.parseNextPrinterCommand();
        assertEquals(2, cmd2.getOffset());
        assertEquals('&', cmd2.getParameterizedCharacter());
        assertEquals('u', cmd2.getGroupCharacter());
        assertEquals("300", cmd2.getValue());
        assertEquals('D', cmd2.getTerminationCharacter());

        assertNull(parser.parseNextPrinterCommand());
        parser.close();
    }

    /**
     * A parameterized PCL command that ends with an lower case letter is treated "ok"...
     */
    public void testUncorrectlyTerminatedParameterizedPclCommands() throws Exception {
        final Pcl5Parser parser = getPcl5ParserFor("~E~&u300d~&u600d");

        final TwoBytePclCommand cmd1 = (TwoBytePclCommand) parser.parseNextPrinterCommand();
        assertEquals(0, cmd1.getOffset());
        assertEquals('E', cmd1.getOperationCharacter());

        final ParameterizedPclCommand cmd2 = (ParameterizedPclCommand) parser.parseNextPrinterCommand();
        assertEquals(2, cmd2.getOffset());
        assertEquals('&', cmd2.getParameterizedCharacter());
        assertEquals('u', cmd2.getGroupCharacter());
        assertEquals("300", cmd2.getValue());
        assertEquals('D', cmd2.getTerminationCharacter());

        final ParameterizedPclCommand cmd3 = (ParameterizedPclCommand) parser.parseNextPrinterCommand();
        assertEquals(9, cmd3.getOffset());
        assertEquals('&', cmd3.getParameterizedCharacter());
        assertEquals('u', cmd3.getGroupCharacter());
        assertEquals("600", cmd3.getValue());
        assertEquals('D', cmd3.getTerminationCharacter());

        assertNull(parser.parseNextPrinterCommand());
        parser.close();
    }

    /**
     * Checks that parameterized PCL sequence is parsed correctly.
     */
    public void testParameterizedPclSequence() throws Exception {
        final Pcl5Parser parser = getPcl5ParserFor("~E~*p100x200Y");

        final TwoBytePclCommand cmd1 = (TwoBytePclCommand) parser.parseNextPrinterCommand();
        assertEquals(0, cmd1.getOffset());
        assertEquals('E', cmd1.getOperationCharacter());

        final ParameterizedPclCommand cmd2 = (ParameterizedPclCommand) parser.parseNextPrinterCommand();
        assertEquals(2, cmd2.getOffset());
        assertEquals('*', cmd2.getParameterizedCharacter());
        assertEquals('p', cmd2.getGroupCharacter());
        assertEquals("100", cmd2.getValue());
        assertEquals('X', cmd2.getTerminationCharacter());

        final ParameterizedPclCommand cmd3 = (ParameterizedPclCommand) parser.parseNextPrinterCommand();
        assertEquals(9, cmd3.getOffset());
        assertEquals('*', cmd3.getParameterizedCharacter());
        assertEquals('p', cmd3.getGroupCharacter());
        assertEquals("200", cmd3.getValue());
        assertEquals('Y', cmd3.getTerminationCharacter());

        assertNull(parser.parseNextPrinterCommand());
        parser.close();
    }

    /**
     * Checks that an unexpected end of the stream triggers an exception.
     */
    public void testUnexpectedEndOfStreamAfterEscapeByte() throws Exception {
        final Pcl5Parser parser = getPcl5ParserFor("~E~");

        final TwoBytePclCommand cmd1 = (TwoBytePclCommand) parser.parseNextPrinterCommand();
        assertEquals(0, cmd1.getOffset());
        assertEquals('E', cmd1.getOperationCharacter());

        try {
            parser.parseNextPrinterCommand();
            fail("Should fail because the data stream ends after an escape byte.");
        } catch (final EOFException e) {
            assertTrue(e.getMessage().contains("unexpectedly ends"));
        }

        parser.close();
    }

    /**
     * Checks that an unexpected end of the stream triggers an exception.
     */
    public void testUnexpectedEndOfStreamAfterParameterizedCharacter() throws Exception {
        final Pcl5Parser parser = getPcl5ParserFor("~*");

        try {
            parser.parseNextPrinterCommand();
            fail("Should fail because the data stream ends after the parameterized character.");
        } catch (final EOFException e) {
            assertTrue(e.getMessage().contains("unexpectedly ends"));
        }

        parser.close();
    }

    /**
     * Checks that an unexpected end of the stream triggers an exception.
     */
    public void testUnexpectedEndOfStreamAfterGroupCharacter() throws Exception {
        final Pcl5Parser parser = getPcl5ParserFor("~*p");

        try {
            parser.parseNextPrinterCommand();
            fail("Should fail because the data stream ends after the group character.");
        } catch (final EOFException e) {
            assertTrue(e.getMessage().contains("unexpectedly ends"));
        }

        parser.close();
    }

    /**
     * Checks that an unexpected end of the stream triggers an exception.
     */
    public void testUnexpectedEndOfStreamAfterValueString() throws Exception {
        final Pcl5Parser parser = getPcl5ParserFor("~*p100");

        try {
            parser.parseNextPrinterCommand();
            fail("Should fail because the data stream ends after the value.");
        } catch (final EOFException e) {
            assertTrue(e.getMessage().contains("unexpectedly ends"));
        }

        parser.close();
    }

    /**
     * Checks that an invalid parameterized character triggers an exception.
     */
    public void testInvalidParameterizedCharacter() throws Exception {
        final Pcl5Parser parser = getPcl5ParserFor("~E~°");

        final TwoBytePclCommand cmd1 = (TwoBytePclCommand) parser.parseNextPrinterCommand();
        assertEquals(0, cmd1.getOffset());
        assertEquals('E', cmd1.getOperationCharacter());

        try {
            parser.parseNextPrinterCommand();
            fail("Should fail because the parameterized character is invalid.");
        } catch (final PclException e) {
            assertTrue(e.getMessage().contains(" parameterized character "));
        }

        parser.close();
    }

    /**
     * Checks that an invalid group character triggers an exception.
     */
    public void testInvalidGroupCharacter() throws Exception {
        final Pcl5Parser parser = getPcl5ParserFor("~E~*°");

        final TwoBytePclCommand cmd1 = (TwoBytePclCommand) parser.parseNextPrinterCommand();
        assertEquals(0, cmd1.getOffset());
        assertEquals('E', cmd1.getOperationCharacter());

        try {
            parser.parseNextPrinterCommand();
            fail("Should fail because the group character is invalid.");
        } catch (final PclException e) {
            assertTrue(e.getMessage().contains(" group character "));
        }

        parser.close();
    }

    /**
     * Checks that a missing value is ok (see "Underline disable" PCL command).
     */
    public void testMissingValueIsOk() throws Exception {
        final Pcl5Parser parser = getPcl5ParserFor("~&d@~(spH");

        final ParameterizedPclCommand cmd1 = (ParameterizedPclCommand) parser.parseNextPrinterCommand();
        assertEquals(0, cmd1.getOffset());
        assertEquals('&', cmd1.getParameterizedCharacter());
        assertEquals('d', cmd1.getGroupCharacter());
        assertEquals("", cmd1.getValue());
        assertEquals('@', cmd1.getTerminationCharacter());

        final ParameterizedPclCommand cmd2 = (ParameterizedPclCommand) parser.parseNextPrinterCommand();
        assertEquals(4, cmd2.getOffset());
        assertEquals('(', cmd2.getParameterizedCharacter());
        assertEquals('s', cmd2.getGroupCharacter());
        assertEquals("", cmd2.getValue());
        assertEquals('P', cmd2.getTerminationCharacter());

        final ParameterizedPclCommand cmd3 = (ParameterizedPclCommand) parser.parseNextPrinterCommand();
        assertEquals(8, cmd3.getOffset());
        assertEquals('(', cmd3.getParameterizedCharacter());
        assertEquals('s', cmd3.getGroupCharacter());
        assertEquals("", cmd3.getValue());
        assertEquals('H', cmd3.getTerminationCharacter());

        assertNull(parser.parseNextPrinterCommand());
        parser.close();
    }
}
