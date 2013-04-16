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
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.lbogdanov.poker.core.Session;
import org.lbogdanov.poker.core.SessionService;
import org.lbogdanov.poker.web.markup.BodylessLabel;
import org.lbogdanov.poker.web.markup.LimitableLabel;
import org.lbogdanov.poker.web.plugin.CustomScrollbarPlugin;
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
    private static final ResourceReference JS = new PageScriptResourceReference(SessionPage.class, "session.js");

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

        final TextArea<String> chatMsg = new TextArea<String>("chatMsg", Model.of(""));
        Form<?> chatForm = new Form<Void>("chatForm");
        chatForm.add(chatMsg, new AjaxFallbackButton("chatSend", chatForm) {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                System.out.println(chatMsg.getModelObject());
            }

            @Override
            protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                super.updateAjaxAttributes(attributes);
                attributes.getAjaxCallListeners().add(new AjaxCallListener() {

                    @Override
                    public CharSequence getPrecondition(Component component) {
                        return "return $('#chatMsg').val().length > 0;";
                    }

                    @Override
                    public CharSequence getCompleteHandler(Component component) {
                        return "Poker.msgSend(jqXHR);";
                    }

                });
            }

        });

        LimitableLabel name = new LimitableLabel("session.name", session.getName());
        if (!Strings.isNullOrEmpty(session.getDescription())) {
            name.add(AttributeModifier.append("class", "tip"),
                     AttributeModifier.append("title", session.getDescription()));
        }

        add(chatForm.setOutputMarkupId(true), name.setMaxLength(LABEL_MAX_LENGTH),
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
        response.render(CssHeaderItem.forReference(CSS));
        response.render(JavaScriptHeaderItem.forReference(I18N));
        response.render(JavaScriptHeaderItem.forReference(CustomScrollbarPlugin.get()));
        response.render(JavaScriptHeaderItem.forReference(JS));
    }

    private String formatDate(Date created) {
        final long MILLIS_PER_WEEK = TimeUnit.DAYS.toMillis(7);
        Locale locale = getLocale();
        PrettyTime prettyTime = new PrettyTime(locale);
        Duration largest = Iterables.getFirst(prettyTime.calculatePreciseDuration(created), null);
        if (largest != null && largest.getUnit().getMillisPerUnit() < MILLIS_PER_WEEK) {
            return prettyTime.format(largest);
        } else { // fallback to an absolute date string when difference is more than a week
            return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale).format(created);
        }
    }

}
