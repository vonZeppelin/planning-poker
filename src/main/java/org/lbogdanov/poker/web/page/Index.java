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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.bootstrap.Bootstrap;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.util.io.IClusterable;
import org.odlabs.wiquery.core.javascript.JsQuery;


/**
 * Represents an index page.
 * 
 * @author Leonid Bogdanov
 */
public class Index extends WebPage {

    private static final class Credentials implements IClusterable {

        String username;
        String password;
        boolean remember;

    }

    /**
     * Creates a new instance of <code>Index</code> page.
     */
    public Index() {
        Form<Credentials> internalLogin = new Form<Credentials>("internalLogin", new CompoundPropertyModel<Credentials>(new Credentials()));
        internalLogin.add(new TextField<String>("username"))
                     .add(new PasswordTextField("password"))
                     .add(new AjaxButton("submit") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                Credentials creds = (Credentials) form.getModelObject();
                if ("user123".equalsIgnoreCase(creds.username) && "123456".equals(creds.password)) {
                    String js = new JsQuery().$("#crsl").chain("carousel", "'next'").render().toString();
                    target.appendJavaScript(js);
                }
            }

        });
        add(internalLogin);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderHead(IHeaderResponse response) {
        Bootstrap.renderHeadPlain(response);
    }

}
