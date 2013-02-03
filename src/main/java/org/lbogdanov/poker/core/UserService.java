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
package org.lbogdanov.poker.core;


/**
 * A service to manipulate {@link User}.
 * 
 * @author Leonid Bogdanov
 */
public interface UserService {

    /**
     * Returns currently logged in <code>User</code> instance.
     * 
     * @return the <code>User</code> object or <code>null</code> for an anonymous user
     */
    public User getCurrentUser();

    /**
     * Performs a login attempt with specified credentials.
     * 
     * @param username the user name
     * @param password the password
     * @param rememberme  if a user identity should be remembered across sessions
     * @throws <code>RuntimeException</code> if the authentication attempt fails
     */
    public void login(String username, String password, boolean rememberme);

}
