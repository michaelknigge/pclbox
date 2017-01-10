package mk.pclbox;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

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

import junit.framework.TestCase;

/**
 * JUnit-Tests for {@link ControlCharacterCommand}.
 */
public final class ControlCharacterCommandTest extends TestCase {

    private static final ControlCharacterCommand CR_AT_1 = new ControlCharacterCommand(1, (byte) 0x0D);
    private static final ControlCharacterCommand CR_AT_2 = new ControlCharacterCommand(2, (byte) 0x0D);
    private static final ControlCharacterCommand TAB_AT_1 = new ControlCharacterCommand(1, (byte) 0x09);
    private static final ControlCharacterCommand TAB_AT_2 = new ControlCharacterCommand(2, (byte) 0x09);

    /**
     * Checks the method hashCode.
     */
    public void testHashCode() {
        assertEquals(12, CR_AT_1.hashCode());
        assertEquals(15, CR_AT_2.hashCode());
        assertEquals(8, TAB_AT_1.hashCode());
        assertEquals(11, TAB_AT_2.hashCode());
    }

    /**
     * Checks the method equals.
     */
    public void testEquals() {
        assertFalse(CR_AT_1.equals("DUMMY")); // equals with object of other class

        assertFalse(CR_AT_1.equals(CR_AT_2)); // same char, different offset
        assertFalse(CR_AT_1.equals(TAB_AT_1)); // different char, same offset
        assertTrue(CR_AT_1.equals(new ControlCharacterCommand(1, (byte) 0x0D)));
    }

    /**
     * Checks the method toString.
     */
    public void testToString() {
        assertEquals("<0x0D>@1", CR_AT_1.toString());
        assertEquals("<0x0D>@2", CR_AT_2.toString());
        assertEquals("<0x09>@1", TAB_AT_1.toString());
        assertEquals("<0x09>@2", TAB_AT_2.toString());
    }

    /**
     * Checks the method toCommandString.
     */
    public void testToCommandString() {
        assertEquals("0x0D", CR_AT_1.toCommandString());
        assertEquals("0x09", TAB_AT_1.toCommandString());
    }

    /**
     * Checks the method toDisplayString.
     */
    public void testToDisplayString() {
        assertEquals("0x0D", CR_AT_1.toDisplayString());
        assertEquals("0x09", TAB_AT_1.toDisplayString());
    }

    /**
     * Checks the method toByteArray and writeTo.
     */
    public void testToBinary() throws Exception {
        final ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        CR_AT_1.writeTo(baos1);
        Arrays.equals(CR_AT_1.toByteArray(), baos1.toByteArray());

        final ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        CR_AT_1.writeTo(baos2);
        Arrays.equals(TAB_AT_1.toByteArray(), baos2.toByteArray());
    }
}
