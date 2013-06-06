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

import org.apache.wicket.bootstrap.Bootstrap;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.pages.AbstractErrorPage;


/**
 * Represents a custom error page feat. Tard, the Grumpy Cat!
 * 
 * @author Leonid Bogdanov
 */
public class ErrorPage extends AbstractErrorPage {

    /**
     * Creates a new instance of <code>ErrorPage</code>.
     */
    public ErrorPage() {
        add(homePageLink("home"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderHead(IHeaderResponse response) {
        Bootstrap.renderHeadPlain(response);
    }

}
