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
 * JUnit-Tests for {@link ParameterizedPclCommand}.
 */
public final class ParameterizedPclCommandTest extends TestCase {

    private static final ParameterizedPclCommand SIMPLEX_AT_1 = new ParameterizedPclCommand(1, '&', 'l', "0", 'S');
    private static final ParameterizedPclCommand SIMPLEX_AT_2 = new ParameterizedPclCommand(2, '&', 'l', "0", 'S');
    private static final ParameterizedPclCommand DUPLEX_AT_1 = new ParameterizedPclCommand(1, '&', 'l', "1", 'S');
    private static final ParameterizedPclCommand DUPLEX_AT_2 = new ParameterizedPclCommand(2, '&', 'l', "1", 'S');
    private static final ParameterizedPclCommand UEL_AT_0 = new ParameterizedPclCommand(0, '%', 0x00, "-12345", 'X');

    /**
     * Checks the method hashCode.
     */
    public void testHashCode() {
        assertEquals(40, SIMPLEX_AT_1.hashCode());
        assertEquals(43, SIMPLEX_AT_2.hashCode());
        assertEquals(41, DUPLEX_AT_1.hashCode());
        assertEquals(42, DUPLEX_AT_2.hashCode());
    }

    /**
     * Checks the method equals.
     */
    public void testEquals() {
        assertFalse(SIMPLEX_AT_1.equals(SIMPLEX_AT_2)); // same command, different offset
        assertFalse(SIMPLEX_AT_1.equals(DUPLEX_AT_1)); // different command, same offset
        assertTrue(SIMPLEX_AT_1.equals(new ParameterizedPclCommand(1, '&', 'l', "0", 'S')));

        assertFalse(SIMPLEX_AT_1.equals(new ParameterizedPclCommand(1, '*', 'l', "0", 'S')));
        assertFalse(SIMPLEX_AT_1.equals(new ParameterizedPclCommand(1, '&', 'm', "0", 'S')));
        assertFalse(SIMPLEX_AT_1.equals(new ParameterizedPclCommand(1, '&', 'l', "0", 'T')));
    }

    /**
     * Checks the method toString.
     */
    public void testToString() {
        assertEquals("<esc>&l0S@1", SIMPLEX_AT_1.toString());
        assertEquals("<esc>&l0S@2", SIMPLEX_AT_2.toString());
        assertEquals("<esc>&l1S@1", DUPLEX_AT_1.toString());
        assertEquals("<esc>&l1S@2", DUPLEX_AT_2.toString());
        assertEquals("<esc>%-12345X@0", UEL_AT_0.toString());
    }

    /**
     * Checks the method toCommandString.
     */
    public void testToCommandString() {
        assertEquals("&lS", SIMPLEX_AT_1.toCommandString());
        assertEquals("&lS", DUPLEX_AT_1.toCommandString());
        assertEquals("%X", UEL_AT_0.toCommandString());
    }

    /**
     * Checks the method toDisplayString.
     */
    public void testToDisplayString() {
        assertEquals("&l0S", SIMPLEX_AT_1.toDisplayString());
        assertEquals("&l1S", DUPLEX_AT_1.toDisplayString());
        assertEquals("%-12345X", UEL_AT_0.toDisplayString());
    }

    /**
     * Checks the method toByteArray and writeTo.
     */
    public void testToBinary() throws Exception {
        final ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        SIMPLEX_AT_1.writeTo(baos1);
        Arrays.equals(SIMPLEX_AT_1.toByteArray(), baos1.toByteArray());

        final ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        DUPLEX_AT_1.writeTo(baos2);
        Arrays.equals(DUPLEX_AT_1.toByteArray(), baos2.toByteArray());

        final ByteArrayOutputStream baos3 = new ByteArrayOutputStream();
        UEL_AT_0.writeTo(baos3);
        Arrays.equals(UEL_AT_0.toByteArray(), baos3.toByteArray());
    }
}
