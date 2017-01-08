package mk.pclbox;

import junit.framework.TestCase;

/**
 * JUnit-Tests for {@link PjlCommand}.
 */
public final class PjlCommandTest extends TestCase {

    private static final PjlCommand COMMENT_AT_1 = new PjlCommand(1, "@PJL COMMENT FOO");
    private static final PjlCommand COMMENT_AT_2 = new PjlCommand(2, "@PJL COMMENT FOO");
    private static final PjlCommand ENTER_AT_1 = new PjlCommand(1, "@PJL ENTER LANGUAGE=PCL");
    private static final PjlCommand ENTER_AT_2 = new PjlCommand(2, "@PJL ENTER LANGUAGE=PCL");

    /**
     * Checks the method hashCode.
     */
    public void testHashCode() {
        assertEquals(-2048168586, COMMENT_AT_1.hashCode());
        assertEquals(-2048168587, COMMENT_AT_2.hashCode());
        assertEquals(-1246566229, ENTER_AT_1.hashCode());
        assertEquals(-1246566232, ENTER_AT_2.hashCode());
    }

    /**
     * Checks the method equals.
     */
    public void testEquals() {
        assertFalse(COMMENT_AT_1.equals(COMMENT_AT_2)); // same command, different offset
        assertFalse(COMMENT_AT_1.equals(ENTER_AT_1)); // different command, same offset
        assertTrue(COMMENT_AT_1.equals(new PjlCommand(1, "@PJL COMMENT FOO")));
    }

    /**
     * Checks the method toString.
     */
    public void testToString() {
        assertEquals("@PJL COMMENT FOO", COMMENT_AT_1.toString());
        assertEquals("@PJL COMMENT FOO", COMMENT_AT_2.toString());
        assertEquals("@PJL ENTER LANGUAGE=PCL", ENTER_AT_1.toString());
        assertEquals("@PJL ENTER LANGUAGE=PCL", ENTER_AT_2.toString());
    }
}
