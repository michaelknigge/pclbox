package mk.pclbox;

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
}
