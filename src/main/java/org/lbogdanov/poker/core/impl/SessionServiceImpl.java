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
package org.lbogdanov.poker.core.impl;

import static org.lbogdanov.poker.core.Constants.SESSION_CODE_DEFAULT_LENGTH;
import static org.lbogdanov.poker.util.Settings.SESSION_CODE_LENGTH;

import java.security.SecureRandom;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.lbogdanov.poker.core.*;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.OrderBy;
import com.avaje.ebean.annotation.Transactional;


/**
 * Implementation of {@link SessionService} interface.
 * 
 * @author Leonid Bogdanov
 */
@Singleton
public class SessionServiceImpl implements SessionService {

    @Inject
    private EbeanServer ebean;
    @Inject
    private UserService userService;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean exists(String code) {
        return ebean.find(Session.class)
                    .where().eq("code", code)
                    .findRowCount() != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Session find(String code) {
        return ebean.find(Session.class)
                    .where().eq("code", code)
                    .findUnique();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Session create(String name, String description, String estimations) {
        Session session = new Session();
        session.setName(name);
        session.setDescription(description);
        session.setEstimates(estimations);
        session.setCode(newCode(SESSION_CODE_LENGTH.asInt().or(SESSION_CODE_DEFAULT_LENGTH)));
        session.setAuthor(userService.getCurrentUser());
        ebean.save(session);
        return session;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PagingList<Session> find(User author, String orderBy, boolean ascending, int pageSize) {
        OrderBy<Session> order = ebean.find(Session.class)
                                      .where().eq("author", author)
                                      .orderBy();
        return new EbeanPagingList<Session>((ascending ? order.asc(orderBy) : order.desc(orderBy)).findPagingList(pageSize));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(Session session) {
        ebean.delete(session);
    }

    /**
     * Generates a new alphanumeric code of a specified length which can be used to uniquely identify a session.
     * 
     * @param length the desired code length
     * @return the new code
     */
    private String newCode(int length) {
        Random rnd = new SecureRandom();
        StringBuilder code = new StringBuilder(length);
        do {
            code.setLength(0);
            while (code.length() < length) {
                if (rnd.nextBoolean()) { // append a new letter or digit?
                    char letter = (char) ('a' + rnd.nextInt(26));
                    code.append(rnd.nextBoolean() ? Character.toUpperCase(letter) : letter);
                } else {
                    code.append(rnd.nextInt(10));
                }
            }
        } while (exists(code.toString()));
        return code.toString();
    }

}
