package mk.pclbox;

import java.io.IOException;

/**
 * Interface that every printer data stream parser implements.
 */
interface DataStreamParser {

    /**
     * Parses the next printer command. The read command will be returned as a {@link PrinterCommand} object.
     *
     * @return the read {@link PrinterCommand} or <code>null</code> if the end of the stream has been reached.
     */
    public PrinterCommand parseNextPrinterCommand() throws IOException, PclException;

    /**
     * Closes the data stream from which the {@link DataStreamParser} reads.
     */
    public void close() throws IOException;
}
