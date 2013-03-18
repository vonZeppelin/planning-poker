/**
 * Copyright 2012 Leonid Bogdanov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lbogdanov.poker.web.page;

import java.util.Collections;

import org.apache.wicket.bootstrap.Bootstrap;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.lbogdanov.poker.web.markup.NavBar;
import org.lbogdanov.poker.web.plugin.I18nPlugin;


/**
 * Base class for all pages: adds Twitter Bootstrap files, global navbar.
 * 
 * @author Leonid Bogdanov
 */
abstract class AbstractPage extends WebPage {

    protected final ResourceReference I18N = new JavaScriptResourceReference(AbstractPage.class, "i18n.js",
                                                                             getLocale(), null, null) {

            @Override
            public Iterable<? extends HeaderItem> getDependencies() {
                return Collections.singletonList(JavaScriptHeaderItem.forReference(I18nPlugin.get()));
            }

    };

    private NavBar navBar;

    /**
     * @see WebPage#WebPage()
     */
    public AbstractPage() {
        init();
    }

    /**
     * @see WebPage#WebPage(IModel)
     */
    public AbstractPage(IModel<?> model) {
        super(model);
        init();
    }

    /**
     * @see WebPage#WebPage(PageParameters)
     */
    public AbstractPage(PageParameters parameters) {
        super(parameters);
        init();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderHead(IHeaderResponse response) {
        Bootstrap.renderHeadPlain(response);
    }

    /**
     * Returns a <code>NavBar</code> instance.
     * 
     * @return the <code>NavBar</code> instance
     */
    public NavBar getNavBar() {
        return navBar;
    }

    /**
     * Performs page initialization.
     */
    protected void init() {
        navBar = new NavBar("navbar");
        add(navBar.setOutputMarkupId(true));
    }

}
