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

import static org.lbogdanov.poker.core.Constants.LABEL_MAX_LENGTH;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.atmosphere.EventBus;
import org.apache.wicket.atmosphere.ResourceRegistrationListener;
import org.apache.wicket.atmosphere.Subscribe;
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
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.cpr.Meteor;
import org.lbogdanov.poker.core.Session;
import org.lbogdanov.poker.core.SessionService;
import org.lbogdanov.poker.core.UserService;
import org.lbogdanov.poker.web.markup.BodylessLabel;
import org.lbogdanov.poker.web.markup.LimitableLabel;
import org.lbogdanov.poker.web.plugin.CustomScrollbarPlugin;
import org.lbogdanov.poker.web.util.ChatMessage;
import org.lbogdanov.poker.web.util.Message;
import org.lbogdanov.poker.web.util.OriginFilter;
import org.ocpsoft.prettytime.Duration;
import org.ocpsoft.prettytime.PrettyTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;


/**
 * Represents a Planning Poker session page.
 * 
 * @author Leonid Bogdanov
 */
@RequiresUser
public class SessionPage extends AbstractPage {

    /**
     * Subscribes clients to corresponding named {@link Broadcaster}s depending on the current <code>Session</code> code.
     */
    public static final class Subscriber implements ResourceRegistrationListener {

        private static final Subscriber INSTANCE = new Subscriber();

        /**
         * Returns a single instance of <code>Subscriber</code>.
         * 
         * @return the <code>Subscriber</code> instance
         */
        public static Subscriber get() {
            return INSTANCE;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void resourceRegistered(String uuid, Page page) {
            if (page instanceof SessionPage) {
                Session session = ((SessionPage) page).session;
                Broadcaster broadcaster = BroadcasterFactory.getDefault().lookup(session.getCode(), true);
                // TODO Atmosphere issue: searching AtmosphereResource instance by its uuid doesn't work
                Meteor meteor = Meteor.lookup((HttpServletRequest) page.getRequest().getContainerRequest());
                broadcaster.addAtmosphereResource(meteor.getAtmosphereResource());
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void resourceUnregistered(String uuid) {}

        private Subscriber() {}

    }

    private static final Logger LOG = LoggerFactory.getLogger(SessionPage.class);
    private static final ResourceReference CSS = new CssResourceReference(SessionPage.class, "session.css");
    private static final ResourceReference JS = new PageScriptResourceReference(SessionPage.class, "session.js");

    @Inject
    private SessionService sessionService;
    @Inject
    private UserService userService;
    @Inject
    private ObjectMapper mapper;
    private Session session;

    /**
     * Creates a new instance of <code>Session</code> page.
     */
    public SessionPage(PageParameters parameters) {
        session = sessionService.find(parameters.get("code").toString());
        if (session == null) {
            throw new AbortWithHttpErrorCodeException(HttpServletResponse.SC_NOT_FOUND, "Session not found");
        }

        final TextArea<String> chatMsg = new TextArea<String>("chatMsg", Model.of(""));
        Form<?> chatForm = new Form<Void>("chatForm");
        chatForm.add(chatMsg, new AjaxFallbackButton("chatSend", chatForm) {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                ChatMessage message = new ChatMessage(getSession().getId(), userService.getCurrentUser(),
                                                      chatMsg.getModelObject());
                post(session.getCode(), message);
            }

            @Override
            protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                super.updateAjaxAttributes(attributes);
                AjaxCallListener listener = new AjaxCallListener();
                listener.onPrecondition("return $('#chatMsg').val().length > 0;")
                        .onBeforeSend("Poker.toggleForm('chatForm', true);")
                        .onComplete("Poker.msgSent(jqXHR); Poker.toggleForm('chatForm', false);");
                attributes.getAjaxCallListeners().add(listener);
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

    /**
     * Asynchronously publishes messages to the session participants via Atmosphere framework.
     * 
     * @param target the <code>AjaxRequestTarget</code> instance
     * @param msg the message to publish
     * @throws Exception if any error occurred
     */
    @Subscribe(contextAwareFilter = OriginFilter.class)
    public void publishMessage(AjaxRequestTarget target, Message<?> msg) throws Exception {
        if (target == null) {
            LOG.info("Couldn't sent async message, target was null");
        } else {
            target.appendJavaScript(String.format("Poker.dispatch(%s);", mapper.writeValueAsString(msg)));
        }
    }

    private static void post(Object channel, Message<?> message) {
        Broadcaster broadcaster = BroadcasterFactory.getDefault().lookup(channel);
        if (broadcaster == null) {
            LOG.info("No active Broadcaster for a channel {}", channel);
        } else {
            EventBus eventBus = EventBus.get();
            for (AtmosphereResource resource : broadcaster.getAtmosphereResources()) {
                eventBus.post(message, resource);
            }
        }
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
