package mk.pclbox;

import junit.framework.TestCase;

/**
 * JUnit-Tests for {@link TwoBytePclCommand}.
 */
public final class TwoBytePclCommandTest extends TestCase {

    private static final TwoBytePclCommand ESC_E_AT_1 = new TwoBytePclCommand(1, (byte) 0x45);
    private static final TwoBytePclCommand ESC_E_AT_2 = new TwoBytePclCommand(2, (byte) 0x45);
    private static final TwoBytePclCommand ESC_9_AT_1 = new TwoBytePclCommand(1, (byte) 0x39);
    private static final TwoBytePclCommand ESC_9_AT_2 = new TwoBytePclCommand(2, (byte) 0x39);

    /**
     * Checks the method hashCode.
     */
    public void testHashCode() {
        assertEquals(68, ESC_E_AT_1.hashCode());
        assertEquals(71, ESC_E_AT_2.hashCode());
        assertEquals(56, ESC_9_AT_1.hashCode());
        assertEquals(59, ESC_9_AT_2.hashCode());
    }

    /**
     * Checks the method equals.
     */
    public void testEquals() {
        assertFalse(ESC_E_AT_1.equals(ESC_E_AT_2)); // same command, different offset
        assertFalse(ESC_E_AT_1.equals(ESC_9_AT_1)); // different command, same offset
        assertTrue(ESC_E_AT_1.equals(new TwoBytePclCommand(1, (byte) 0x45)));
    }

    /**
     * Checks the method toString.
     */
    public void testToString() {
        assertEquals("<esc>E@1", ESC_E_AT_1.toString());
        assertEquals("<esc>E@2", ESC_E_AT_2.toString());
        assertEquals("<esc>9@1", ESC_9_AT_1.toString());
        assertEquals("<esc>9@2", ESC_9_AT_2.toString());
    }
}
