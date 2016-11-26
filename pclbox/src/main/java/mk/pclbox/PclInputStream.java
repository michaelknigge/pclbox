package mk.pclbox;

import java.io.IOException;

/**
 * All reads and seeks performed by the {@link PclParser} are using concrete implementations of a {@link PclInputStream}.
 */
public interface PclInputStream {
    
    /**
     * Closes this input stream and releases any system resources associated with the stream.
     */
    public void close() throws IOException;
    
    /**
     * Reads the next byte of data from this input stream. The value byte is returned as an int in the 
     * range 0 to 255. If no byte is available because the end of the stream has been reached,
     * the value -1 is returned.
     */
    public int read() throws IOException;

    /**
     * Reads up to len bytes of data into an array of bytes from this input stream. If pos equals count, 
     * then -1 is returned to indicate end of file. Otherwise, the number k of bytes read is equal to the
     * smaller of len and count-pos. If k is positive, then bytes buf[pos] through buf[pos+k-1] are copied
     * into b[off] through b[off+k-1] in the manner performed by System.arraycopy. The value k is added
     * into pos and k is returned.
     */
    public int read(byte[] b, int off, int len) throws IOException;

    /**
     * Sets the offset, measured from the beginning of this stream, at which the next read occurs.
     */
    public void seek(final long offset) throws IOException;
}
