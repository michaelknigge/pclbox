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
 * JUnit-Tests for {@link TextCommand}.
 */
public final class TextCommandTest extends TestCase {

    private static final TextCommand TEXT_A_AT_1 = new TextCommand(1, new byte[] { 65 });
    private static final TextCommand TEXT_A_AT_2 = new TextCommand(2, new byte[] { 65 });
    private static final TextCommand TEXT_B_AT_1 = new TextCommand(1, new byte[] { 66 });
    private static final TextCommand TEXT_B_AT_2 = new TextCommand(2, new byte[] { 66 });

    /**
     * Checks the method hashCode.
     */
    public void testHashCode() {
        assertEquals(97, TEXT_A_AT_1.hashCode());
        assertEquals(98, TEXT_A_AT_2.hashCode());
        assertEquals(96, TEXT_B_AT_1.hashCode());
        assertEquals(99, TEXT_B_AT_2.hashCode());
    }

    /**
     * Checks the method equals.
     */
    public void testEquals() {
        assertFalse(TEXT_A_AT_1.equals("DUMMY")); // equals with object of other class

        assertFalse(TEXT_A_AT_1.equals(TEXT_A_AT_2)); // same text, different offset
        assertFalse(TEXT_A_AT_1.equals(TEXT_B_AT_1)); // different text, same offset
        assertTrue(TEXT_A_AT_1.equals(new TextCommand(1, new byte[] { 65 })));
    }

    /**
     * Checks the method toString.
     */
    public void testToString() {
        assertEquals("A@1", TEXT_A_AT_1.toString());
        assertEquals("A@2", TEXT_A_AT_2.toString());
        assertEquals("B@1", TEXT_B_AT_1.toString());
        assertEquals("B@2", TEXT_B_AT_2.toString());
    }

    /**
     * Checks the method toCommandString.
     */
    public void testToCommandString() {
        assertEquals("TEXT", TEXT_A_AT_1.toCommandString());
        assertEquals("TEXT", TEXT_B_AT_1.toCommandString());
    }

    /**
     * Checks the method toDisplayString.
     */
    public void testToDisplayString() {
        assertEquals("A", TEXT_A_AT_1.toDisplayString());
        assertEquals("B", TEXT_B_AT_1.toDisplayString());
    }

    /**
     * Checks the method toByteArray and writeTo.
     */
    public void testToBinary() throws Exception {
        final ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        TEXT_A_AT_1.writeTo(baos1);
        assertTrue(Arrays.equals(TEXT_A_AT_1.toByteArray(), baos1.toByteArray()));

        final ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        TEXT_B_AT_1.writeTo(baos2);
        assertTrue(Arrays.equals(TEXT_B_AT_1.toByteArray(), baos2.toByteArray()));
    }
}
