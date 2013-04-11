package org.lbogdanov.poker.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Random;

import javax.inject.Inject;

import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lbogdanov.poker.core.impl.SessionServiceImpl;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Query;
import com.avaje.ebean.TxCallable;


/**
 * Tests for <code>SessionService</code> implementation.
 */
@Ignore
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
        when(ebean.execute(any(TxCallable.class))).thenAnswer(new Answer<Object>() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return ((TxCallable<?>) invocation.getArguments()[0]).call();
            }

        });
    }

    /**
     * Test method for {@link SessionService#newCode(int)}.
     */
    @Test
    public void testCreate() {
        String regex = "\\w{10}";
        Session session1 = sessionService.create("Session1", "", "");
        Session session2 = sessionService.create("Session2", "", "");
        assertNotEquals(session1.getCode(), session2.getCode());
        assertTrue(session1.getCode().matches(regex));
        assertTrue(session2.getCode().matches(regex));
    }

}
