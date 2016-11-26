package mk.pclbox;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

/**
 * JUnit-Tests for {@link PclInputStreamForInputStream}.
 */
public final class PclInputStreamForInputStreamTest extends TestCase {

    /**
     * Happy-Flow test with an {@link InputStream} that supports {@link InputStream#mark(int)},
     * {@link InputStream#reset()} and {@link InputStream#skip(long)}.
     */
    public void testWithMarkSupported() throws Exception {
        
        ByteArrayInputStream in = new ByteArrayInputStream("TEST".getBytes("utf-8"));
        PclInputStream pclStream = new PclInputStreamForInputStream(in);
        
        assertEquals(84, pclStream.read());
        
        byte[] buffer = new byte[3];
        pclStream.read(buffer, 0, 3);
        assertEquals(69, buffer[0]);
        assertEquals(83, buffer[1]);
        assertEquals(84, buffer[2]);
        
        pclStream.seek(1);
        assertEquals(69, pclStream.read());
        
        pclStream.close();
    }
    
    /**
     * Test with an {@link InputStream} that does not support {@link InputStream#mark(int)}.
     */
    public void testWithMarkNotSupported() throws Exception {
        
        PclInputStream pclStream = new PclInputStreamForInputStream(new InputStream() {
            @Override
            public int read() throws IOException {
                return -1;
            }
        });

        try {
            pclStream.seek(1);
            fail("Seek should fail because mark() is not supported!");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("is not supported"));
        }
    }
}
