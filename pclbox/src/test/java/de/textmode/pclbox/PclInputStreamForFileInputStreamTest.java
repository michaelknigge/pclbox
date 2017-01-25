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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import junit.framework.TestCase;

/**
 * JUnit-Tests for {@link PclInputStreamForFileInputStream}.
 */
public final class PclInputStreamForFileInputStreamTest extends TestCase {

    /**
     * Happy-Flow test with an {@link FileInputStream}.
     */
    public void testAllMethodsOfInterface() throws Exception {

        final File testFile = File.createTempFile(this.getClass().getSimpleName(), null);
        testFile.deleteOnExit(); // in case this method throws an exception...

        final FileOutputStream out = new FileOutputStream(testFile);
        try {
            out.write("TEST".getBytes("utf-8"));
        } finally {
            out.close();
        }

        final PclInputStream pclStream = new PclInputStreamForFileInputStream(new FileInputStream(testFile));

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

        pclStream.close();

        assertTrue(testFile.delete());
    }

}
