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

import javax.inject.Inject;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.lbogdanov.poker.core.User;
import org.lbogdanov.poker.core.UserService;
import org.lbogdanov.poker.web.page.ProfilePage;

import fiftyfive.wicket.shiro.markup.LogoutLink;


/**
 * A Navigation bar implementation.
 * 
 * @author Leonid Bogdanov
 */
public class NavBar extends Panel { // TODO Remove this class?

    @Inject
    private UserService userService;

    /**
     * @see Panel#Panel(String)
     */
    public NavBar(String id) {
        super(id);
        WebMarkupContainer userMenu = new WebMarkupContainer("userMenu") {

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisible(userService.getCurrentUser() != null);
            }

        };
        Label username = new BodylessLabel("username", new AbstractReadOnlyModel<User>() {

            @Override
            public User getObject() {
                return userService.getCurrentUser();
            }

        });
        userMenu.add(username, new BookmarkablePageLink<Void>("profile", ProfilePage.class), new LogoutLink("logout"));
        add(userMenu);
    }

}
