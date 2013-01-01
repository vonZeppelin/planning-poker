package org.lbogdanov.poker.core;

import static org.junit.Assert.*;

import javax.inject.Inject;

import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lbogdanov.poker.core.impl.SessionServiceImpl;


/**
 * Tests for <code>SessionService</code> implementation.
 */
@RunWith(JukitoRunner.class)
public class SessionServiceTest {

    @Inject
    private SessionServiceImpl sessionService;

    /**
     * Test method for {@link SessionService#newCode(int)}.
     */
    @Test
    public void testNewCode() {
        String regex = "\\w{5}";
        String code1 = sessionService.newCode(5);
        String code2 = sessionService.newCode(5);
        assertNotEquals(code1, code2);
        assertTrue(code1.matches(regex));
        assertTrue(code2.matches(regex));
        assertEquals(3, sessionService.newCode(3).length());
        assertEquals(25, sessionService.newCode(25).length());
    }

}
