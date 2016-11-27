package mk.pclbox;

import java.io.ByteArrayInputStream;

import junit.framework.TestCase;

/**
 * JUnit-Tests for {@link Pcl5Parser}.
 */
public final class Pcl5ParserTest extends TestCase {

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
        assertEquals(0x31, text[0]);
        assertEquals(0x32, text[1]);
        assertEquals(0x33, text[2]);

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
        assertEquals(0x31, text[0]);
        assertEquals(0x32, text[1]);
        assertEquals(0x33, text[2]);

        final ControlCharacterCommand cc1 = (ControlCharacterCommand) parser.parseNextPrinterCommand();
        assertEquals(0x0D, cc1.getControlCharacter());
        assertEquals(3, cc1.getOffset());

        final ControlCharacterCommand cc2 = (ControlCharacterCommand) parser.parseNextPrinterCommand();
        assertEquals(0x0A, cc2.getControlCharacter());
        assertEquals(4, cc2.getOffset());

        final TextCommand tc2 = (TextCommand) parser.parseNextPrinterCommand();
        assertEquals(5, tc2.getOffset());
        assertEquals(0x34, text[5]);
        assertEquals(0x35, text[6]);
        assertEquals(0x36, text[7]);

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
}
