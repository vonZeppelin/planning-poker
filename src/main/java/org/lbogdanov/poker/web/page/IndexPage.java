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

import static org.apache.wicket.AttributeModifier.append;
import static org.apache.wicket.validation.validator.StringValidator.maximumLength;
import static org.lbogdanov.poker.core.Constants.SESSION_CODE_MAX_LENGTH;
import static org.lbogdanov.poker.core.Constants.SESSION_DESCRIPTION_MAX_LENGTH;
import static org.lbogdanov.poker.core.Constants.SESSION_ESTIMATES_MAX_LENGTH;
import static org.lbogdanov.poker.core.Constants.SESSION_NAME_MAX_LENGTH;

import javax.inject.Inject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.LazyInitializer;
import org.apache.wicket.util.io.IClusterable;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.lbogdanov.poker.core.Duration;
import org.lbogdanov.poker.core.Session;
import org.lbogdanov.poker.core.SessionService;
import org.lbogdanov.poker.core.UserService;
import org.lbogdanov.poker.web.markup.BootstrapFeedbackPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Represents an index page.
 * 
 * @author Leonid Bogdanov
 */
public class IndexPage extends AbstractPage {

    /**
     * Simple POJO for internal login form.
     */
    private static final class Credentials implements IClusterable {

        private String username;
        private String password;
        private boolean rememberme;

    }

    /**
     * Simple POJO for session create and join forms.
     */
    private static final class Game implements IClusterable {

        private String code;
        private String name;
        private String description;
        private String estimates;

    }

    /**
     * A helper class to add Bootstrap validation styles to a control group.
     */
    private static final class ValidationModel extends AbstractReadOnlyModel<String> {

        private String cssClass;
        private LazyInitializer<FormComponent<?>> field;

        public ValidationModel(final Form<?> form, final String field, String cssClass) {
            this.cssClass = cssClass;
            this.field = new LazyInitializer<FormComponent<?>>() {

                @Override
                protected FormComponent<?> createInstance() {
                    return (FormComponent<?>) form.get(field);
                }

            };
        }

        @Override
        public String getObject() {
            return field.get().isValid() ? null : cssClass;
        }

    }

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexPage.class);
    private static final ResourceReference JS = new PageScriptResourceReference(IndexPage.class, "index.js");

    @Inject
    private SessionService sessionService;
    @Inject
    private UserService userService;

    /**
     * Creates a new instance of <code>Index</code> page.
     */
    @SuppressWarnings("unchecked")
    public IndexPage() {
        WebMarkupContainer session = new WebMarkupContainer("session");
        WebMarkupContainer login = new WebMarkupContainer("login") {

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisible(userService.getCurrentUser() == null);
            }

        };

        Form<?> internal = new StatelessForm<Credentials>("internal", new CompoundPropertyModel<Credentials>(new Credentials()));
        internal.add(new BootstrapFeedbackPanel("feedback"),
                     new TransparentWebMarkupContainer("usernameGroup").add(append("class", new ValidationModel(internal, "username", "error"))),
                     new TransparentWebMarkupContainer("passwordGroup").add(append("class", new ValidationModel(internal, "password", "error"))),
                     new RequiredTextField<String>("username"), new PasswordTextField("password"),
                     new CheckBox("rememberme"), new AjaxFallbackButton("submit", internal) {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                Credentials credentials = (Credentials) form.getModelObject();
                getSession().replaceSession();
                try {
                    userService.login(credentials.username, credentials.password, credentials.rememberme);
                    if (target != null) {
                        target.appendJavaScript("$('#crsl').carousel({interval: false}).carousel('next');");
                        target.add(getNavBar());
                    }
                } catch (RuntimeException re) {
                    LOGGER.info("Authentication error", re);
                    form.error(IndexPage.this.getString("login.internal.authError"));
                    if (target != null) {
                        target.add(form);
                    }
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                if (target != null) {
                    target.add(form);
                }
            }

        });

        IModel<Game> gameModel = new CompoundPropertyModel<Game>(new Game());
        IValidator<String> codeValidator = new IValidator<String>() {

            @Override
            public void validate(IValidatable<String> validatable) {
                String code = validatable.getValue();
                if (!sessionService.exists(code)) {
                    ValidationError error = new ValidationError();
                    error.addKey("session.join.invalidCode").setVariable("code", code);
                    validatable.error(error);
                }
            }

        };
        Form<?> join = new Form<Game>("join", gameModel);
        join.add(new BootstrapFeedbackPanel("feedback"),
                 new TransparentWebMarkupContainer("codeGroup").add(append("class", new ValidationModel(join, "code", "error"))),
                 new RequiredTextField<String>("code").add(maximumLength(SESSION_CODE_MAX_LENGTH), codeValidator),
                 new AjaxFallbackButton("submit", join) {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                Game game = (Game) form.getModelObject();
                setResponsePage(SessionPage.class, new PageParameters().add("code", game.code));
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                if (target != null) {
                    target.add(form);
                }
            }

        });
        IValidator<String> estimatesValidator = new IValidator<String>() {

            @Override
            public void validate(IValidatable<String> validatable) {
                try {
                    Duration.parse(validatable.getValue());
                } catch (IllegalArgumentException e) {
                    ValidationError error = new ValidationError();
                    error.addKey("session.create.estimates.invalidEstimate").setVariable("estimate", e.getMessage());
                    validatable.error(error);
                }
            }

        };
        Form<?> create = new Form<Game>("create", gameModel);
        create.add(new BootstrapFeedbackPanel("feedback"),
                   new TransparentWebMarkupContainer("nameGroup").add(append("class", new ValidationModel(create, "name", "error"))),
                   new RequiredTextField<String>("name").add(maximumLength(SESSION_NAME_MAX_LENGTH)),
                   new TransparentWebMarkupContainer("estimatesGroup").add(append("class", new ValidationModel(create, "estimates", "error"))),
                   new RequiredTextField<String>("estimates").add(maximumLength(SESSION_ESTIMATES_MAX_LENGTH), estimatesValidator),
                   new TextArea<String>("description").add(maximumLength(SESSION_DESCRIPTION_MAX_LENGTH)),
                   new AjaxFallbackButton("submit", create) {

            @Override
            public void onSubmit(AjaxRequestTarget target, Form<?> form) {
                Game game = (Game) form.getModelObject();
                Session session = sessionService.create(game.name, game.description, game.estimates);
                setResponsePage(SessionPage.class, new PageParameters().add("code", session.getCode()));
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                if (target != null) {
                    target.appendJavaScript("Poker.initEditor();");
                    target.add(form);
                }
            }

        });

        login.add(internal.setOutputMarkupId(true));
        session.add(join.setOutputMarkupId(true), create.setOutputMarkupId(true));
        session.add(append("class", new AbstractReadOnlyModel<String>() {

            @Override
            public String getObject() {
                return userService.getCurrentUser() != null ? "active" : null;
            }

        }));
        add(login, session);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptHeaderItem.forReference(JS));
    }

}
