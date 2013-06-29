package org.lbogdanov.poker.web.page;

import static org.apache.wicket.validation.validator.StringValidator.maximumLength;
import static org.lbogdanov.poker.core.Constants.USER_EMAIL_MAX_LENGTH;
import static org.lbogdanov.poker.core.Constants.USER_FIRST_NAME_MAX_LENGTH;
import static org.lbogdanov.poker.core.Constants.USER_LAST_NAME_MAX_LENGTH;

import javax.inject.Inject;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.lbogdanov.poker.core.User;
import org.lbogdanov.poker.core.UserService;
import org.lbogdanov.poker.web.markup.BootstrapFeedbackPanel;
import org.lbogdanov.poker.web.markup.ControlGroup;


/**
 * Represents a user's profile page.
 * 
 * @author Alexandra Fomina
 */
@RequiresUser
public class ProfilePage extends AbstractPage {

    @Inject
    private UserService userService;

    /**
     * Creates a new instance of <code>Profile</code> page.
     */
    public ProfilePage() {
        Form<?> profile = new Form<User>("profile", new CompoundPropertyModel<User>(userService.getCurrentUser()));
        MarkupContainer firstNameGroup = new ControlGroup("firstNameGroup");
        MarkupContainer lastNameGroup = new ControlGroup("lastNameGroup");
        MarkupContainer emailGroup = new ControlGroup("emailGroup");
        MarkupContainer buttonGroup = new ControlGroup("buttonGroup").add(new AjaxFallbackButton("save", profile) {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                userService.save((User) form.getModelObject());
                form.success(ProfilePage.this.getString("updated"));
                if (target != null) {
                    target.add(form);
                    target.add(getNavBar());
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                if (target != null) {
                    target.add(form);
                }
            }

        });

        profile.add(firstNameGroup.add(new RequiredTextField<String>("firstName").setLabel(new ResourceModel("firstName"))
                                                                                 .add(maximumLength(USER_FIRST_NAME_MAX_LENGTH))),
                    lastNameGroup.add(new TextField<String>("lastName").setLabel(new ResourceModel("lastName"))
                                                                       .add(maximumLength(USER_LAST_NAME_MAX_LENGTH))),
                    emailGroup.add(new EmailTextField("email").setLabel(new ResourceModel("email"))
                                                              .add(maximumLength(USER_EMAIL_MAX_LENGTH))),
                    new BootstrapFeedbackPanel("feedback"), buttonGroup);
        add(profile);
    }

}
