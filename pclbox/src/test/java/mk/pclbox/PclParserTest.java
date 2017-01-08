package mk.pclbox;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import junit.framework.TestCase;

/**
 * JUnit-Tests for {@link PclParser}.
 */
public final class PclParserTest extends TestCase {

    /**
     * Just invokes the parse() method. This test is more or less useless - it is just
     * here to pimp the code coverage...
     */
    public void testParser() throws Exception {
        final PclParser pclParser = new PclParser(new ByteArrayInputStream(new byte[0]), null);
        pclParser.parse();
        pclParser.close();
    }

    /**
     * Just invokes every constructor once. This test is more or less useless - it is just
     * here to pimp the code coverage...
     */
    public void testAllConstructors() throws Exception {
        final File testFile = File.createTempFile(this.getClass().getSimpleName(), null);
        testFile.deleteOnExit(); // in case this method throws an exception...

        new PclParser(testFile.getAbsolutePath(), null).close();
        new PclParser(testFile, null).close();
        new PclParser(new FileInputStream(testFile), null).close();
        new PclParser(new ByteArrayInputStream(new byte[0]), null).close();
        new PclParser(new ByteArrayInputStream(new byte[0]), null, true).close();

        new PclParser(new PclInputStream() {

            @Override
            public long tell() throws IOException {
                return 0;
            }

            @Override
            public void seek(long offset) throws IOException {
            }

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                return -1;
            }

            @Override
            public int read(byte[] b) throws IOException {
                return -1;
            }

            @Override
            public int read() throws IOException {
                return -1;
            }

            @Override
            public void close() throws IOException {
            }
        }, null).close();
    }
}
