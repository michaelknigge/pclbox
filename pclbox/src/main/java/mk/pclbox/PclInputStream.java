package mk.pclbox;

/*
 * Copyright 2017 Michael Knigge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;

/**
 * All reads and seeks performed by the {@link PclParser} are performed with a
 * concrete implementation of a {@link PclInputStream}.
 */
public interface PclInputStream {

    /**
     * Closes this input stream and releases any system resources associated with the stream.
     * The {@link PclParser} assumes that this method is idempotent ({@link PclParser} will not
     * ensure that {@link PclParser#close()} delegates to the underlying close() method only once).
     */
    public void close() throws IOException;

    /**
     * Reads the next byte of data from this input stream. The value byte is returned as an int in the
     * range 0 to 255. If no byte is available because the end of the stream has been reached,
     * the value -1 is returned.
     *
     * @return the next byte of data, or -1 if the end of the stream is reached.
     */
    public int read() throws IOException;

    /**
     * Reads some number of bytes from the input stream and stores them into the buffer array b. The number
     * of bytes actually read is returned as an integer. This method blocks until input data is available,
     * end of file is detected, or an exception is thrown.
     *
     * @param b - the buffer into which the data is read.
     *
     * @return the total number of bytes read into the buffer, or -1 if there is no more data because the
     *     end of the stream has been reached.
     */
    public int read(byte[] b) throws IOException;

    /**
     * Reads up to len bytes of data from the input stream into an array of bytes. An attempt is made to
     * read as many as len bytes, but a smaller number may be read. The number of bytes actually read is
     * returned as an integer.
     *
     * @param b - the buffer into which the data is read.
     * @param off - the start offset in array b at which the data is written.
     * @param len - the maximum number of bytes to read.
     *
     * @return the total number of bytes read into the buffer, or -1 if there is no more data because the end
     *     of the stream has been reached.
     */
    public int read(byte[] b, int off, int len) throws IOException;

    /**
     * Sets the offset, measured from the beginning of this stream, at which the next read occurs.
     */
    public void seek(final long offset) throws IOException;

    /**
     * Gets the current offset, measured from the beginning of this stream, at which the next read will occur.
     *
     * @return the current offset, measured from the beginning of the stream.
     */
    public long tell() throws IOException;
}
