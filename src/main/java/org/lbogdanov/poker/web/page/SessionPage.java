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

import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.bootstrap.Bootstrap;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.resource.JQueryPluginResourceReference;
import org.lbogdanov.poker.core.Session;
import org.lbogdanov.poker.core.SessionService;
import org.lbogdanov.poker.web.markup.BodylessLabel;
import org.lbogdanov.poker.web.markup.LimitableLabel;
import org.ocpsoft.prettytime.Duration;
import org.ocpsoft.prettytime.PrettyTime;

import com.google.common.base.Strings;
import com.google.common.collect.Iterables;


/**
 * Represents a Planning Poker session page.
 * 
 * @author Leonid Bogdanov
 */
@RequiresUser
public class SessionPage extends AbstractPage {

    private static final int LABEL_MAX_LENGTH = 32;
    private static final ResourceReference CSS = new CssResourceReference(SessionPage.class, "session.css");
    private static final ResourceReference JS = new JQueryPluginResourceReference(SessionPage.class, "session.js") {

        @Override
        public Iterable<? extends HeaderItem> getDependencies() {
            return Iterables.concat(super.getDependencies(),
                                    Collections.singletonList(JavaScriptHeaderItem.forReference(Bootstrap.plain())));
        }

    };

    @Inject
    private SessionService sessionService;

    /**
     * Creates a new instance of <code>Session</code> page.
     */
    public SessionPage(PageParameters parameters) {
        Session session = sessionService.find(parameters.get("code").toString());
        if (session == null) {
            throw new AbortWithHttpErrorCodeException(HttpServletResponse.SC_NOT_FOUND, "Session not found");
        }

        LimitableLabel name = new LimitableLabel("session.name", session.getName());
        if (!Strings.isNullOrEmpty(session.getDescription())) {
            name.add(AttributeModifier.append("class", "tip"),
                     AttributeModifier.append("title", session.getDescription()));
        }
        add(name.setMaxLength(LABEL_MAX_LENGTH),
            new BodylessLabel("session.code", session.getCode()).setMaxLength(LABEL_MAX_LENGTH),
            new BodylessLabel("session.author", session.getAuthor()).setMaxLength(LABEL_MAX_LENGTH),
            new BodylessLabel("session.created", formatDate(session.getCreated())).setMaxLength(LABEL_MAX_LENGTH));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptHeaderItem.forReference(JS));
        response.render(CssHeaderItem.forReference(CSS));
    }

    private static String formatDate(Date created) {
        final long MILLIS_PER_WEEK = TimeUnit.DAYS.toMillis(7);
        PrettyTime prettyTime = new PrettyTime();
        Duration largest = Iterables.getFirst(prettyTime.calculatePreciseDuration(created), null);
        if (largest != null && largest.getUnit().getMillisPerUnit() < MILLIS_PER_WEEK) {
            return prettyTime.format(largest);
        } else { // fallback to an absolute date string when difference is more than a week
            return DateFormat.getInstance().format(created);
        }
    }

}
