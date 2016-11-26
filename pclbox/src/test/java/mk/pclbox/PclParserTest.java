package mk.pclbox;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;

import junit.framework.TestCase;

public final class PclParserTest extends TestCase {

    /**
     * Just invokes every constructor once. This test is more or less useless - it is just
     * here to pimp the code coverage...
     */
    public void testAllConstructors() throws Exception {
        File testFile = File.createTempFile(this.getClass().getSimpleName(), null);
        testFile.deleteOnExit(); // in case this method throws an exception...
        
        new PclParser(testFile.getAbsolutePath()).close();
        new PclParser(testFile).close();
        new PclParser(new FileInputStream(testFile)).close();
        new PclParser(new ByteArrayInputStream(new byte[0])).close();
    }
}
