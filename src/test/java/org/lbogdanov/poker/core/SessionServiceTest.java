package org.lbogdanov.poker.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Random;

import javax.inject.Inject;

import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lbogdanov.poker.core.impl.SessionServiceImpl;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Query;


/**
 * Tests for <code>SessionService</code> implementation.
 */
@RunWith(JukitoRunner.class)
public class SessionServiceTest {

    @Inject
    private SessionServiceImpl sessionService;

    /**
     * Sets up test environment, is called before a test is executed.
     * 
     * @param ebean mocked <code>EbeanServer</code> instance
     */
    @Before
    @SuppressWarnings("unchecked")
    public void setup(EbeanServer ebean) {
        Query<Session> query = mock(Query.class);
        ExpressionList<Session> exprList = mock(ExpressionList.class);
        when(exprList.eq(anyString(), any())).thenReturn(exprList);
        when(exprList.findRowCount()).thenAnswer(new Answer<Integer>() {

            private Random random = new Random();

            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return random.nextInt(32);
            }

        });
        when(query.where()).thenReturn(exprList);
        when(ebean.find(Session.class)).thenReturn(query);
    }

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
