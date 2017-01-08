package mk.pclbox;

import junit.framework.TestCase;

/**
 * JUnit-Tests for {@link ParameterizedPclCommand}.
 */
public final class ParameterizedPclCommandTest extends TestCase {

    private static final ParameterizedPclCommand SIMPLEX_AT_1 = new ParameterizedPclCommand(1, '&', 'l', "0", 'S');
    private static final ParameterizedPclCommand SIMPLEX_AT_2 = new ParameterizedPclCommand(2, '&', 'l', "0", 'S');
    private static final ParameterizedPclCommand DUPLEX_AT_1 = new ParameterizedPclCommand(1, '&', 'l', "1", 'S');
    private static final ParameterizedPclCommand DUPLEX_AT_2 = new ParameterizedPclCommand(2, '&', 'l', "1", 'S');

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
    }
}
