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
 * JUnit-Tests for {@link HpglCommand}.
 */
public final class HpglCommandTest extends TestCase {

    private static final HpglCommand IN_AT_1 = new HpglCommand(1, "IN", "");
    private static final HpglCommand IN_AT_2 = new HpglCommand(2, "IN", "");
    private static final HpglCommand CO_AT_1 = new HpglCommand(1, "CO", "");
    private static final HpglCommand CO_AT_2 = new HpglCommand(2, "CO", "");

    private static final HpglCommand CO_AT_2_WITH_TEXT = new HpglCommand(2, "CO", "X");

    /**
     * Checks the method hashCode.
     */
    public void testHashCode() {
        assertEquals(2340, IN_AT_1.hashCode());
        assertEquals(2343, IN_AT_2.hashCode());
        assertEquals(2157, CO_AT_1.hashCode());
        assertEquals(2158, CO_AT_2.hashCode());
        assertEquals(2102, CO_AT_2_WITH_TEXT.hashCode());
    }

    /**
     * Checks the method equals.
     */
    public void testEquals() {
        assertFalse(IN_AT_1.equals("DUMMY")); // equals with object of other class
        assertFalse(CO_AT_2.equals(CO_AT_2_WITH_TEXT)); // only parameters differ

        assertFalse(IN_AT_1.equals(IN_AT_2)); // same command, different offset
        assertFalse(IN_AT_1.equals(CO_AT_1)); // different command, same offset
        assertTrue(IN_AT_1.equals(new HpglCommand(1, "IN", "")));
    }

    /**
     * Checks the method getTextualDescription.
     */
    public void testGetTextualDescription() {
        assertEquals("Initialize", IN_AT_1.getTextualDescription());
        assertEquals("Comment", CO_AT_1.getTextualDescription());

        assertEquals(
                "Unknown HP/GL-Command ZZ",
                new HpglCommand(2, "ZZ", "").getTextualDescription());
    }

    /**
     * Checks the method toString.
     */
    public void testToString() {
        assertEquals("IN@1", IN_AT_1.toString());
        assertEquals("IN@2", IN_AT_2.toString());
        assertEquals("CO@1", CO_AT_1.toString());
        assertEquals("CO@2", CO_AT_2.toString());
        assertEquals("COX@2", CO_AT_2_WITH_TEXT.toString());
    }

    /**
     * Checks the method toCommandString.
     */
    public void testToCommandString() {
        assertEquals("IN", IN_AT_1.toCommandString());
        assertEquals("CO", CO_AT_1.toCommandString());
    }

    /**
     * Checks the method toDisplayString.
     */
    public void testToDisplayString() {
        assertEquals("IN", IN_AT_1.toDisplayString());
        assertEquals("CO", CO_AT_1.toDisplayString());
    }

    /**
     * Checks the method toByteArray and writeTo.
     */
    public void testToBinary() throws Exception {
        final ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        IN_AT_1.writeTo(baos1);
        assertTrue(Arrays.equals(IN_AT_1.toByteArray(), baos1.toByteArray()));

        final ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        CO_AT_1.writeTo(baos2);
        assertTrue(Arrays.equals(CO_AT_1.toByteArray(), baos2.toByteArray()));

        final ByteArrayOutputStream baos3 = new ByteArrayOutputStream();
        CO_AT_2_WITH_TEXT.writeTo(baos3);
        assertTrue(Arrays.equals(CO_AT_2_WITH_TEXT.toByteArray(), baos3.toByteArray()));
    }

}
