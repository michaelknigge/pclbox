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

    private final PclParserContext context;

    /**
     * Constructor that internally creates a {@link FileInputStream} for reading and seeking
     * within the PCL data stream.
     *
     * @param inputFile - the {@link File} that contains the PCL data stream.
     */
    public PclParser(final File inputFile) throws FileNotFoundException {
        this(new FileInputStream(inputFile));
    }

    /**
     * Constructor that internally creates a {@link FileInputStream} for reading and seeking
     * within the PCL data stream.
     *
     * @param inputFileName - the name of the file that contains the PCL data stream.
     */
    public PclParser(final String inputFileName) throws FileNotFoundException {
        this(new FileInputStream(inputFileName));
    }

    /**
     * Constructor that uses the ready to use {@link PclInputStream} for reading and seeking within
     * the PCL data stream.
     *
     * @param input - the {@link PclInputStream} that will be used to read the PCL data stream.
     */
    public PclParser(final PclInputStream input) {
        this.context = new PclParserContext(input);
    }

    /**
     * Constructor that uses a {@link FileInputStream} for reading and seeking within the PCL data stream.
     * Seeking is done by using a {@link FileChannel} that is provided by the {@link FileInputStream}.
     *
     * @param input - the {@link FileInputStream} that will be used to read the PCL data stream.
     */
    public PclParser(final FileInputStream input) {
        this.context = new PclParserContext(new PclInputStreamForInputStream(input));
    }

    /**
     * Constructor that uses a base {@link InputStream} for reading and seeking within the PCL data stream.
     * Seeking is done by using a {@link InputStream#reset()} and {@link InputStream#skip(long)} that may not
     * be very fast, depending of the implementation. Also note that you must not invoke {@link InputStream#mark()},
     * {@link InputStream#reset()} or {@link InputStream#skip(long)} on the given stream.
     *
     * @param input - the {@link InputStream} that will be used to read the PCL data stream.
     */
    public PclParser(final InputStream input) {
        this.context = new PclParserContext(new PclInputStreamForInputStream(input));
    }

    /**
     * Parses the next printer command. The read command will be returned as a {@link PrinterCommand} object.
     *
     * @return the read {@link PrinterCommand} or <code>null</code> if the end of the stream has been reached.
     */
    public PrinterCommand parseNextPrinterCommand() throws IOException {
        return this.context.getCurrentParser().parseNextPrinterCommand();
    }

    @Override
    public void close() throws IOException {
        this.context.close();
    }
}
