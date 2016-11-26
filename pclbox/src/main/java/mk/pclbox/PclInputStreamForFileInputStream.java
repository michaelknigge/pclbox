package mk.pclbox;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Implementation of {@link PclInputStream} for underlying {@link FileInputStream}s.
 * This implementation uses the {@link FileChannel} of the {@link FileInputStream}
 * to seek within the file.
 */
final class PclInputStreamForFileInputStream implements PclInputStream {
    
    private final FileInputStream input;

    /**
     * Constructor that is given the underlying {@link FileInputStream}.
     */
    PclInputStreamForFileInputStream(final FileInputStream input) {
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
        this.input.getChannel().position(offset);
    }
}
