package de.textmode.pclbox;

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
    public int read(byte[] b) throws IOException {
        return this.input.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return this.input.read(b, off, len);
    }

    @Override
    public void seek(long offset) throws IOException {
        this.input.getChannel().position(offset);
    }

    @Override
    public long tell() throws IOException {
        return this.input.getChannel().position();
    }
}
