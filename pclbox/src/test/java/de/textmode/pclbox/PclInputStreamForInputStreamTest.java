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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

/**
 * JUnit-Tests for {@link PclInputStreamForInputStream}.
 */
public final class PclInputStreamForInputStreamTest extends TestCase {

    /**
     * Happy-Flow test with an {@link InputStream} that supports {@link InputStream#mark(int)},
     * {@link InputStream#reset()} and {@link InputStream#skip(long)}.
     */
    public void testWithMarkSupported() throws Exception {

        final ByteArrayInputStream in = new ByteArrayInputStream("TEST".getBytes("utf-8"));
        final PclInputStream pclStream = new PclInputStreamForInputStream(in);

        assertEquals(0, pclStream.tell());
        assertEquals(84, pclStream.read());
        assertEquals(1, pclStream.tell());

        final byte[] buffer = new byte[3];
        assertEquals(3, pclStream.read(buffer, 0, 3));
        assertEquals(69, buffer[0]);
        assertEquals(83, buffer[1]);
        assertEquals(84, buffer[2]);
        assertEquals(4, pclStream.tell());

        pclStream.seek(1);
        assertEquals(1, pclStream.tell());
        assertEquals(69, pclStream.read());
        assertEquals(2, pclStream.tell());

        assertEquals(2, pclStream.read(buffer));
        assertEquals(83, buffer[0]);
        assertEquals(84, buffer[1]);
        assertEquals(4, pclStream.tell());

        try {
            pclStream.seek(5);
            fail("Seek should fail we tried to seek after the end of the file");
        } catch (final IOException e) {
            assertTrue(e.getMessage().contains("to position to offset 5"));
        }

        pclStream.close();
    }

    /**
     * Test with an {@link InputStream} that does not support {@link InputStream#mark(int)}.
     */
    public void testWithMarkNotSupported() throws Exception {

        final PclInputStream pclStream = new PclInputStreamForInputStream(new InputStream() {
            @Override
            public int read() throws IOException {
                return -1;
            }
        });

        try {
            pclStream.seek(1);
            fail("Seek should fail because mark() is not supported!");
        } catch (final IOException e) {
            assertTrue(e.getMessage().contains("is not supported"));
        }
    }
}
