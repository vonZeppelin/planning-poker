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
import static org.lbogdanov.poker.core.Constants.SESSION_NAME_MAX_LENGTH;

import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.io.IClusterable;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.lbogdanov.poker.core.SessionService;
import org.lbogdanov.poker.web.markup.BootstrapFeedbackPanel;
import org.odlabs.wiquery.core.javascript.JsQuery;


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

    }

    @Inject
    private SessionService sessionService;

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
                setVisible(SecurityUtils.getSubject().getPrincipal() == null);
            }

        };

        Form<?> internal = new StatelessForm<Credentials>("internal", new CompoundPropertyModel<Credentials>(new Credentials()));
        internal.add(new RequiredTextField<String>("username"), new PasswordTextField("password"),
                     new CheckBox("rememberme"), new AjaxFallbackButton("submit", internal) {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                Credentials credentials = (Credentials) form.getModelObject();
                Subject subject = SecurityUtils.getSubject();
                // force a new session to prevent session fixation attack
                subject.getSession().stop();
                getSession().replaceSession();
                AuthenticationToken token = new UsernamePasswordToken(credentials.username,
                                                                      credentials.password,
                                                                      credentials.rememberme);
                try {
                    subject.login(token);
                    String js = new JsQuery().$("#crsl").chain("carousel", "{interval: false}")
                                                        .chain("carousel", "'next'").render().toString();
                    if (target != null) {
                        target.appendJavaScript(js);
                        target.add(getNavBar());
                    }
                } catch (AuthenticationException ae) {
                    // TODO Handle errors
                }
            }

        });

        IModel<Game> gameModel = new CompoundPropertyModel<Game>(new Game());
        final Form<?> join = new Form<Game>("join", gameModel);
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
        AttributeModifier errorAppender = append("class", new AbstractReadOnlyModel<String>() {

            @Override
            public String getObject() {
                return ((FormComponent<String>) join.get("code")).isValid() ? null : "error";
            }

        });
        join.add(new BootstrapFeedbackPanel("feedback"),
                 new TransparentWebMarkupContainer("codeGroup").add(errorAppender),
                 new RequiredTextField<String>("code").add(maximumLength(SESSION_CODE_MAX_LENGTH), codeValidator),
                 new AjaxFallbackButton("submit", join) {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                Game game = (Game) form.getModelObject();
                setResponsePage(SessionPage.class);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                if (target != null) {
                    target.add(form);
                }
            }

        });
        Form<?> create = new Form<Game>("create", gameModel) {

            @Override
            protected void onSubmit() {
                Game game = getModelObject();
                setResponsePage(SessionPage.class);
            }

        };
        create.add(new BootstrapFeedbackPanel("feedback"),
                   new RequiredTextField<String>("name").add(maximumLength(SESSION_NAME_MAX_LENGTH)),
                   new TextArea<String>("description"));

        login.add(internal);
        session.add(join.setOutputMarkupId(true), create);
        session.add(append("class", new AbstractReadOnlyModel<String>() {

            @Override
            public String getObject() {
                return SecurityUtils.getSubject().getPrincipal() != null ? "active" : null;
            }

        }));
        add(login, session);
    }

}
