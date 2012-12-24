package org.lbogdanov.poker.core;

import static org.junit.Assert.*;

import org.junit.Test;


/**
 * Tests for <code>Session</code> class.
 */
public class SessionTest {

    /**
     * Test method for {@link Session#newCode(int)}.
     */
    @Test
    public void testNewCode() {
        String regex = "\\w{5}";
        String code1 = Session.newCode(5);
        String code2 = Session.newCode(5);
        assertNotEquals(code1, code2);
        assertTrue(code1.matches(regex));
        assertTrue(code2.matches(regex));
        assertEquals(3, Session.newCode(3).length());
        assertEquals(25, Session.newCode(25).length());
    }

}
