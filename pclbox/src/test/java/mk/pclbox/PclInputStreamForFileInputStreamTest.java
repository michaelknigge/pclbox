package mk.pclbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import junit.framework.TestCase;

public final class PclInputStreamForFileInputStreamTest extends TestCase {
    
    /**
     * Happy-Flow test with an {@link FileInputStream}.
     */
    public void testAllMethodsOfInterface() throws Exception {
        
        File testFile = File.createTempFile(this.getClass().getSimpleName(), null);
        testFile.deleteOnExit(); // in case this method throws an exception...
        
        FileOutputStream out = new FileOutputStream(testFile);
        try {
            out.write("TEST".getBytes("utf-8"));
        } finally {
            out.close();
        }

        PclInputStream pclStream = new PclInputStreamForFileInputStream(new FileInputStream(testFile));
        
        assertEquals(84, pclStream.read());
        
        byte[] buffer = new byte[3];
        pclStream.read(buffer, 0, 3);
        assertEquals(69, buffer[0]);
        assertEquals(83, buffer[1]);
        assertEquals(84, buffer[2]);
        
        pclStream.seek(1);
        assertEquals(69, pclStream.read());
        
        pclStream.close();
        
        assertTrue(testFile.delete());
    }
    
}
