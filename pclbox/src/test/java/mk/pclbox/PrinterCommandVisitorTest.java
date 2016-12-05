package mk.pclbox;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import junit.framework.TestCase;

/**
 * JUnit-Tests for {@link PrinterCommandVisitor}.
 */
public final class PrinterCommandVisitorTest extends TestCase {

    /**
     * Simple visitor that just counts the invocations.
     */
    private static class CountingVisitor implements PrinterCommandVisitor {

        private int textCommandCounter = 0;
        private int controlCharacterCounter = 0;
        private int twoBytePclCommandCounter = 0;
        private int parameterizedPclCommand = 0;

        @Override
        public void handle(TextCommand command) {
            ++this.textCommandCounter;
        }

        @Override
        public void handle(ControlCharacterCommand command) {
            ++this.controlCharacterCounter;
        }

        @Override
        public void accept(TwoBytePclCommand twoBytePclCommand) {
            ++this.twoBytePclCommandCounter;
        }

        @Override
        public void accept(ParameterizedPclCommand parameterizedPclCommand) {
            ++this.parameterizedPclCommand;
        }
    }

    /**
     * Parses a PCL data stream that contains just one concrete {@link PclCommand} of every type. Checks if
     * the visitor is invoked for every type once.
     */
    public void testVisitor() throws Exception {

        final byte[] twoByteCommandContent = { 0x1B, 0x45 };
        final byte[] textCommandContent = { 0x31, 0x32, 0x33 };
        final byte[] controlCharacterContent = { 0x0C };
        final byte[] parameterizedPclCommandContent = { 0x1B, 0x26, 0x75, 0x33, 0x30, 0x30, 0x44 };

        final ByteArrayOutputStream work = new ByteArrayOutputStream();
        work.write(twoByteCommandContent);
        work.write(textCommandContent);
        work.write(parameterizedPclCommandContent);
        work.write(controlCharacterContent);

        final ByteArrayInputStream data = new ByteArrayInputStream(work.toByteArray());
        final PclInputStreamForInputStream pclStream = new PclInputStreamForInputStream(data);
        final Pcl5Parser parser = new Pcl5Parser(pclStream);

        final CountingVisitor visitor = new CountingVisitor();
        PrinterCommand cmd;
        while ((cmd = parser.parseNextPrinterCommand()) != null) {
            cmd.accept(visitor);
        }

        parser.close();

        assertEquals(1, visitor.textCommandCounter);
        assertEquals(1, visitor.controlCharacterCounter);
        assertEquals(1, visitor.twoBytePclCommandCounter);
        assertEquals(1, visitor.parameterizedPclCommand);
    }

}
