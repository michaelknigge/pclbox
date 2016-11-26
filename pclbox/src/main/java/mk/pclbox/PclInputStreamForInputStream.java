package mk.pclbox;

import java.io.IOException;
import java.io.InputStream;

/**
 * Implementation of {@link PclInputStream} for underlying {@link InputStream}s.
 * This implementation uses the {@link InputStream#reset()} and {@link InputStream#skip(long)} 
 * to seek within the file.
 */
final class PclInputStreamForInputStream implements PclInputStream {
    
    private final InputStream input;

    /**
     * Constructor that is given the underlying {@link InputStream}.
     */
    PclInputStreamForInputStream(final InputStream input) {
        this.input = input;
    }

    @Override
    public void close() throws IOException {
        this.input.close();
    }

    @Override
    public int read() throws IOException {
        return this.input.read();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return this.input.read(b, off, len);
    }

    @Override
    public void seek(long offset) throws IOException {
        if (!this.input.markSupported()) {
            throw new IOException(new StringBuilder()
                    .append("Repositioning with the PCL data stream is not supported for input streams of type ")
                    .append(this.input.getClass().getSimpleName())
                    .toString());
        }
        
        this.input.reset();

        if (this.input.skip(offset) != offset) {
            throw new IOException(new StringBuilder()
                    .append("An error occurred when trying to position to offset ")
                    .append(offset)
                    .toString());
        }
    }
}

