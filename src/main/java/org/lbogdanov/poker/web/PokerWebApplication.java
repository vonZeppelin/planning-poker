package org.lbogdanov.poker.web;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.lbogdanov.poker.web.page.Index;


/**
 * @author Leonid Bogdanov
 */
public class PokerWebApplication extends WebApplication {

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends Page> getHomePage() {
        return Index.class;
    }

}
