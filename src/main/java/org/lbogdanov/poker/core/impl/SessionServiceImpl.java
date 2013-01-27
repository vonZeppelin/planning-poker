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

import java.security.SecureRandom;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.lbogdanov.poker.core.Session;
import org.lbogdanov.poker.core.SessionService;

import com.avaje.ebean.EbeanServer;


/**
 * Implementation of {@link SessionService} interface.
 * 
 * @author Leonid Bogdanov
 */
@Singleton
public class SessionServiceImpl implements SessionService {

    @Inject
    private EbeanServer ebean;

    /**
     * {@inheritDoc}
     */
    @Override
    public String newCode(int length) {
        Random rnd = new SecureRandom();
        StringBuilder code = new StringBuilder(length);
        do {
            code.setLength(0);
            while (code.length() < length) {
                if (rnd.nextBoolean()) { // append new letter or digit?
                    char letter = (char) ('a' + rnd.nextInt(26));
                    code.append(rnd.nextBoolean() ? Character.toUpperCase(letter) : letter);
                } else {
                    code.append(rnd.nextInt(10));
                }
            }
        } while (exists(code.toString()));
        return code.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists(String code) {
        return ebean.find(Session.class)
                    .where()
                    .eq("code", code)
                    .findRowCount() != 0;
    }

}
