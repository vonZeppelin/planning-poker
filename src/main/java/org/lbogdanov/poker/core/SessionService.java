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
 * A service to create and manipulate {@link Session} instances.
 * 
 * @author Leonid Bogdanov
 */
public interface SessionService {

    /**
     * Checks whether a session with a specified code exists.
     * 
     * @param code the session code
     * @return <b>true</b> if the session with the specified code exists, otherwise <b>false</b>
     */
    public boolean exists(String code);

    /**
     * Returns a session with a specified code, or <code>null</code> if no such session exists.
     * 
     * @param code the session code
     * @return the session
     */
    public Session find(String code);

    /**
     * Returns the given number of sessions created by a specified author ordered by a specified order criterion.
     * 
     * @param author the author
     * @param orderBy the order criterion
     * @param ascending <code>true</code> for ascending sort order
     * @param pageSize the number of returned sessions
     * @return the sessions
     */
    public PagingList<Session> find(User author, String orderBy, boolean ascending, int pageSize);

    /**
     * Creates a new session object and persists it in a storage.
     * 
     * @param name the session name
     * @param description the session description
     * @param estimates the session estimates
     * @return a newly created <code>Session</code> object
     */
    public Session create(String name, String description, String estimates);

    /**
     * Deletes a session.
     * 
     * @param session the session to delete
     */
    public void delete(Session session);

}
