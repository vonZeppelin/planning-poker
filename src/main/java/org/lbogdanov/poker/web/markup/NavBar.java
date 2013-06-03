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
package org.lbogdanov.poker.web.markup;

import static org.apache.wicket.AttributeModifier.append;

import javax.inject.Inject;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.lbogdanov.poker.core.User;
import org.lbogdanov.poker.core.UserService;
import org.lbogdanov.poker.web.page.MySessionsPage;

import fiftyfive.wicket.shiro.markup.LogoutLink;


/**
 * A Navigation bar implementation.
 * 
 * @author Leonid Bogdanov
 */
public class NavBar extends Panel {

    @Inject
    private UserService userService;

    /**
     * @see Panel#Panel(String)
     */
    public NavBar(String id) {
        super(id);
        Behavior visibilityManager = new Behavior() {

            @Override
            public void onConfigure(Component component) {
                component.setVisible(userService.getCurrentUser() != null);
            }

        };
        WebMarkupContainer userMenu = new WebMarkupContainer("userMenu");
        WebMarkupContainer navigation = new WebMarkupContainer("navigation");
        Label username = new BodylessLabel("username", new AbstractReadOnlyModel<User>() {

            @Override
            public User getObject() {
                return userService.getCurrentUser();
            }

        });
        userMenu.add(username, new LogoutLink("logout"));
        navigation.add(new WebMarkupContainer("sessions").add(append("class", new AbstractReadOnlyModel<String>() {

            @Override
            public String getObject() {
                return MySessionsPage.class.equals(NavBar.this.getPage().getClass()) ? "active" : null;
            }

        })));
        add(userMenu.add(visibilityManager), navigation.add(visibilityManager));
    }

}
