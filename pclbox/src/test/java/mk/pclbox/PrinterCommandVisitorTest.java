package mk.pclbox;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * JUnit-Tests for {@link PrinterCommandVisitor}.
 */
public final class PrinterCommandVisitorTest extends TestCase implements PrinterCommandHandler {

    private static final List<PrinterCommand> COMMANDS = new ArrayList<PrinterCommand>();
    private static final CountingVisitor VISITOR = new CountingVisitor();

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
        command.accept(VISITOR);
    }

    /**
     * Simple visitor that just counts the invocations.
     */
    private static class CountingVisitor implements PrinterCommandVisitor {

        private int textCommandCounter = 0;
        private int controlCharacterCounter = 0;
        private int twoBytePclCommandCounter = 0;
        private int parameterizedPclCommand = 0;
        private int pjlCommand = 0;

        @Override
        public void handle(TextCommand command) {
            ++this.textCommandCounter;
        }

        @Override
        public void handle(ControlCharacterCommand command) {
            ++this.controlCharacterCounter;
        }

        @Override
        public void handle(TwoBytePclCommand twoBytePclCommand) {
            ++this.twoBytePclCommandCounter;
        }

        @Override
        public void handle(ParameterizedPclCommand parameterizedPclCommand) {
            ++this.parameterizedPclCommand;
        }

        @Override
        public void handle(PjlCommand pjlCommand) {
            ++this.pjlCommand;
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
        final byte[] parameterizedPclCommandContent = { 0x1B, 0x25, 0x2D, 0x31, 0x32, 0x33, 0x34, 0x35, 0x58 };

        final ByteArrayOutputStream work = new ByteArrayOutputStream();
        work.write(parameterizedPclCommandContent);
        work.write("@PJL ENTER LANGUAGE=PCL\r\n".getBytes("iso-8859-1"));
        work.write(twoByteCommandContent);
        work.write(textCommandContent);
        work.write(controlCharacterContent);

        final ByteArrayInputStream data = new ByteArrayInputStream(work.toByteArray());
        final PclInputStreamForInputStream pclStream = new PclInputStreamForInputStream(data);
        new Pcl5Parser(new PclParserContext(pclStream, this)).parse();

        pclStream.close();

        assertEquals(2, VISITOR.textCommandCounter); // TODO Fixme: 1 as soon as we support PJL!
        assertEquals(3, VISITOR.controlCharacterCounter); // TODO Fixme: 1 as soon as we support PJL!
        assertEquals(1, VISITOR.twoBytePclCommandCounter);
        assertEquals(1, VISITOR.parameterizedPclCommand);
        assertEquals(0, VISITOR.pjlCommand); // TODO Fixme: 1 as soon as we support PJL!
    }
}
