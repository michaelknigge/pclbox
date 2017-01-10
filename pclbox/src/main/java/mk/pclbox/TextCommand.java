package mk.pclbox;

import java.io.IOException;
import java.io.OutputStream;

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

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * A {@link TextCommand} contains text that has to be printed by the printer. The text
 * is handled as a byte[] and not a string because decoding of the byte[] depends on
 * the text parsing method that (maybe) has been set by a "Text Parsing Method" PCL command
 * in the data stream (so the text may be encoded 8 Bit, UTF-8 or whatever).
 */
public final class TextCommand extends PrinterCommand {

    private static final Charset ISO_8859_1 = Charset.forName("iso-8859-1");

    final byte[] text;

    /**
     * Constructor of the {@link TextCommand}.
     *
     * @param offset - position within the data stream
     * @param text - the text
     */
    public TextCommand(long offset, final byte[] text) {
        super(offset);
        this.text = text.clone();
    }

    /**
     * Gets the text that has to be printed by a printer. Note that the text may be encoded
     * as a multi-byte text, depending on a "Text Parsing Method" that may be present in
     * the PCL printer data stream.
     *
     * @return the text as a byte array.
     */
    public byte[] getText() {
        return this.text.clone();
    }

    @Override
    void accept(PrinterCommandVisitor visitor) {
        visitor.handle(this);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.getText()) ^ this.getOffsetHash();
    }

    @Override
    public boolean equals(final Object other) {
        if (other instanceof TextCommand) {
            final TextCommand o = (TextCommand) other;
            return Arrays.equals(o.getText(), this.getText()) && o.getOffset() == this.getOffset();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return new String(this.getText(), ISO_8859_1) + "@" + this.getOffset();
    }

    @Override
    String toCommandString() {
        // The primary use of the toCommandString() method is to provide a "key" for HashMaps....
        // So we use a static string as a "placeholder" for all TextCommands
        return "TEXT";
    }

    @Override
    String toDisplayString() {
        return new String(this.getText(), ISO_8859_1);
    }

    @Override
    byte[] toByteArray() {
        return this.getText().clone();
    }

    @Override
    void writeTo(OutputStream out) throws IOException {
        out.write(this.getText());
    }
}
