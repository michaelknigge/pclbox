package mk.pclbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

/**
 * The {@link PclParser} parses the given {@link InputStream} and interprets the printer commands. 
 */
public final class PclParser implements AutoCloseable {
    
    private final PclInputStream input;
    
    /**
     * Constructor that internally creates a {@link FileInputStream} for reading and seeking
     * within the PCL data stream.
     */
    public PclParser(final File inputFile) throws FileNotFoundException {
        this(new FileInputStream(inputFile));
    }
    
    /**
     * Constructor that internally creates a {@link FileInputStream} for reading and seeking
     * within the PCL data stream.
     */
    public PclParser(final String inputFileName) throws FileNotFoundException {
        this(new FileInputStream(inputFileName));
    }

    /**
     * Constructor that uses the ready to use {@link PclInputStream} for reading and seeking within
     * the PCL data stream.  
     */
    public PclParser(final PclInputStream input) {
        this.input = input;
    }
    
    /**
     * Constructor that uses a {@link FileInputStream} for reading and seeking within the PCL data stream.
     * Seeking is done by using a {@link FileChannel} that is provided by the {@link FileInputStream}.   
     */
    public PclParser(final FileInputStream input) {
        this.input = new PclInputStreamForInputStream(input);
    }
    
    /**
     * Constructor that uses a base {@link InputStream} for reading and seeking within the PCL data stream.
     * Seeking is done by using a {@link InputStream#reset()} and {@link InputStream#skip(long)} that may not
     * be very fast, depending of the implementation.
     */
    public PclParser(final InputStream input) {
        this.input = new PclInputStreamForInputStream(input);
    }

    /**
     * Parses the next printer command. The read command will be returned as a {@link PrinterCommand} object.
     */
    public PrinterCommand parseNextPrinterCommand() {
        return null; // To be implemented..... soon.... I promise ;-)
    }

    /**
     * Positions the current position within the stream to the given offset and parses the next printer command
     * at this position. The read command will be returned as a {@link PrinterCommand} object. If the used
     * {@link InputStream} is a {@link FileInputStream}, then the position within the file is changed using the
     * {@link FileChannel}. Otherwise the {@link InputStream} is required to support the {@link InputStream#mark(int)}
     * method for changing the position within the file.
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
