package mk.pclbox;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.UnsupportedEncodingException;

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

/**
 * JUnit-Tests for {@link HpglParser}.
 */
public final class HpglParserTest extends DataStreamParserTest {

    /**
     * Creates a {@link HpglParser} for parsing the given HP/GL data stream.
     */
    private final HpglParser getHpglParserFor(final String data) throws UnsupportedEncodingException {
        final ByteArrayInputStream input = new ByteArrayInputStream(data.getBytes("iso-8859-1"));
        final PclInputStreamForInputStream pclStream = new PclInputStreamForInputStream(input);

        return new HpglParser(new PclParserContext(pclStream, this));
    }

    /**
     * Checks parsing of a complete empty stream (zero file size).
     */
    public void testEmptyStream() throws Exception {
        final ByteArrayInputStream data = new ByteArrayInputStream(new byte[0]);
        final PclInputStreamForInputStream pclStream = new PclInputStreamForInputStream(data);
        new HpglParser(new PclParserContext(pclStream, this)).parse();

        assertTrue(this.getCommands().isEmpty());
    }

    /**
     * Checks parsing of a single HP/GL command.
     */
    public void testOnlyOneCommand() throws Exception {
        this.getHpglParserFor("IN;").parse();

        assertEquals(
                buildExpected(
                        new HpglCommand(0, "IN", "")),
                this.getCommands());
    }

    /**
     * Checks parsing of multiple HP/GL commands.
     */
    public void testMultipleCommands() throws Exception {
        this.getHpglParserFor("IN;PU10,20;").parse();

        assertEquals(
                buildExpected(
                        new HpglCommand(0, "IN", ""),
                        new HpglCommand(3, "PU", "10,20")),
                this.getCommands());
    }

    /**
     * Checks that a truncated command triggers an exception.
     */
    public void testTruncatedCommandThrowsException() throws Exception {
        try {
            this.getHpglParserFor("IN;P").parse();
        } catch (final EOFException e) {
            assertTrue(e.getMessage().startsWith("The HP/GL data stream unexpectedly "));
            assertEquals(
                    buildExpected(
                            new HpglCommand(0, "IN", "")),
                    this.getCommands());
        }
    }

    /**
     * Checks that a command that is not terminated properly throws an exception.
     */
    public void testMissingTerminator() throws Exception {
        try {
            this.getHpglParserFor("IN").parse();
        } catch (final EOFException e) {
            assertTrue(e.getMessage().startsWith("The HP/GL data stream unexpectedly "));
        }
    }

    /**
     * Checks commands that include double quotes (see "CO" - comment - command).
     */
    public void testCommentCommand() throws Exception {
        this.getHpglParserFor("IN;COba;CO\"Foo\";CO\"Hello \"\"World\"\"\";CO\"1 \"\"2\"\" 3\";IN;").parse();

        assertEquals(
                buildExpected(
                        new HpglCommand(0, "IN", ""),
                        new HpglCommand(3, "CO", "ba"),
                        new HpglCommand(8, "CO", "\"Foo\""),
                        new HpglCommand(16, "CO", "\"Hello \"World\"\""),
                        new HpglCommand(36, "CO", "\"1 \"2\" 3\""),
                        new HpglCommand(50, "IN", "")),
                this.getCommands());
    }
}
