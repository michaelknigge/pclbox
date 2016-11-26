package mk.pclbox;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

/**
 * The {@link PclParser} parses the given {@link InputStream} and interprets the printer commands. 
 */
public final class PclParser implements AutoCloseable{
    
    private final PclInputStream input;
    
    /**
     * Constructor for simple {@link InputStream} streams. If the given {@link InputStream} is a {@link FileInputStream},
     * then internally a special {@link PclInputStream} is created that uses a {@link FileChannel} to seek within the
     * {@link InputStream} if necessary. Otherwise, for seeking within the stream a {@link PclInputStream} is used that
     * uses {@link InputStream#reset()} and {@link InputStream#skip(long)} seek if required. 
     */
    public PclParser(final InputStream input) {
        this.input = input instanceof FileInputStream 
                ? new PclInputStreamForFileInputStream((FileInputStream) input) 
                : new PclInputStreamForInputStream(input);
    }
    
    /**
     * Constructor that uses the ready to use {@link PclInputStream} for reading and seeking within the PCL data.  
     */
    public PclParser(final PclInputStream input) {
        this.input = input;
    }

    /**
     * Parses the next printer command. The read command will be returned as a {@link PrinterCommand} object.
     */
    public PrinterCommand parseNextPrinterCommand() {
        return null; // To be implemented..... soon.... I promise ;-)
    }

    /**
     * Positions the current position within the stream to the given offset and parses the next printer command at this position.
     * The read command will be returned as a {@link PrinterCommand} object. If the used {@link InputStream} is a {@link FileInputStream},
     * then the position within the file is changed using the {@link FileChannel}. Otherwise the {@link InputStream} is required to
     * support the {@link InputStream#mark(int)} method for changing the position within the file.
     */
    public PrinterCommand parseNextPrinterCommandAt(final long position) throws IOException {
        this.input.seek(position);
        return this.parseNextPrinterCommand();
    }

    @Override
    public void close() throws Exception {
        this.input.close();
    }
}
