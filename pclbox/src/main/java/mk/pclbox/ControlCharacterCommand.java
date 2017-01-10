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

/**
 * A {@link ControlCharacterCommand} describes a PCL 5 control character like form feed,
 * line feed, backspace and so on. It contains a single byte.
 */
public final class ControlCharacterCommand extends PrinterCommand {

    /**
     * Backspace. Moves the current active position (CAP) left a distance equal to
     * the width of the last printed symbol or space.
     */
    public static final int BACKSPACE = 8;

    /**
     * Moves the current active position (CAP) to the next tab stop on the current line.
     */
    public static final int HORIZONTAL_TAB = 9;

    /**
     * Line feed. Advances the current active position (CAP) to the same horizontal
     * position on the next line.
     */
    public static final int LINE_FEED = 10;

    /**
     * Form feed. Advances the current active position (CAP) to the same horizontal
     * position at the top of the text area on the next page.
     */
    public static final int FORM_FEED = 12;

    /**
     * Carriage return. Moves the current active position (CAP) to the left
     * margin on the current line.
     */
    public static final int CARRIAGE_RETURN = 13;

    /**
     * Shift out. Switch from the standard font to the alternate font.
     */
    public static final int SHIFT_OUT = 14;

    /**
     * Shift in. Switch from the alternate font to the standard font.
     */
    public static final int SHIFT_IN = 15;


    private final byte controlCharacter;

    /**
     * Constructor of the {@link ControlCharacterCommand}.
     *
     * @param offset - position within the data stream
     * @param controlCharacter - the control character
     */
    public ControlCharacterCommand(final long offset, final byte controlCharacter) {
        super(offset);
        this.controlCharacter = controlCharacter;
    }

    /**
     * Returns the control character byte of this {@link ControlCharacterCommand}.
     *
     * @return the control character byte
     */
    public byte getControlCharacter() {
        return this.controlCharacter;
    }

    @Override
    void accept(PrinterCommandVisitor visitor) {
        visitor.handle(this);
    }

    @Override
    public int hashCode() {
        return this.getControlCharacter() ^ this.getOffsetHash();
    }

    @Override
    public boolean equals(final Object other) {
        if (other instanceof ControlCharacterCommand) {
            final ControlCharacterCommand o = (ControlCharacterCommand) other;
            return o.getControlCharacter() == this.getControlCharacter() && o.getOffset() == this.getOffset();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.format("<0x%02X>@%d", this.getControlCharacter(), this.getOffset());
    }

    @Override
    String toCommandString() {
        return String.format("0x%02X", this.getControlCharacter());
    }

    @Override
    String toDisplayString() {
        return String.format("0x%02X", this.getControlCharacter());
    }

    @Override
    byte[] toByteArray() {
        return new byte[] { this.getControlCharacter() };
    }

    @Override
    void writeTo(OutputStream out) throws IOException {
        out.write(this.getControlCharacter());
    }
}
