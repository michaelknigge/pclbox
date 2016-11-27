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
    private long position;

    /**
     * Constructor that is given the underlying {@link InputStream}.
     */
    PclInputStreamForInputStream(final InputStream input) {
        this.input = input;
        this.position = 0;
    }

    @Override
    public void close() throws IOException {
        this.input.close();
    }

    @Override
    public int read() throws IOException {
        final int result = this.input.read();
        if (result != -1) {
            ++this.position;
        }
        return result;
    }

    @Override
    public int read(byte[] b) throws IOException {
        final int result = this.input.read(b);
        if (result != -1) {
            this.position = this.position + result;
        }
        return result;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        final int result = this.input.read(b, off, len);
        if (result != -1) {
            this.position = this.position + result;
        }
        return result;
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
        this.position = 0; // after reset() we are at offset 0
        this.position = this.input.skip(offset); // now we are at the offset returned from skip

        if (this.position != offset) {
            throw new IOException(new StringBuilder()
                    .append("An error occurred when trying to position to offset ")
                    .append(offset)
                    .toString());
        }
    }

    @Override
    public long tell() throws IOException {
        return this.position;
    }
}
