package mk.pclbox;

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
        assertFalse(TEXT_A_AT_1.equals(TEXT_A_AT_2)); // same text, different offset
        assertFalse(TEXT_A_AT_1.equals(TEXT_B_AT_1)); // different text, same offset
        assertTrue(TEXT_A_AT_1.equals(new TextCommand(1, new byte[] { 65 })));
    }

    /**
     * Checks the method toString.
     */
    public void testToString() {
        assertEquals("A", TEXT_A_AT_1.toString());
        assertEquals("A", TEXT_A_AT_2.toString());
        assertEquals("B", TEXT_B_AT_1.toString());
        assertEquals("B", TEXT_B_AT_2.toString());
    }
}
