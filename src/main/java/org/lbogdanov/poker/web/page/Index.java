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
 * @author Leonid Bogdanov
 */
public class Index extends WebPage {

    private static final class Credentials implements IClusterable {

        String username;
        String password;
        boolean remember;

    }

    /**
     * 
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
    public void renderHead(IHeaderResponse response) {
        Bootstrap.renderHeadPlain(response);
    }

}
