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

import static org.lbogdanov.poker.core.Constants.USER_EXTERNAL_ID_MAX_LENGTH;
import static org.lbogdanov.poker.core.Constants.USER_FIRST_NAME_MAX_LENGTH;
import static org.lbogdanov.poker.core.Constants.USER_LAST_NAME_MAX_LENGTH;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.google.common.base.Objects;


/**
 * Represents a User.
 * 
 * @author Leonid Bogdanov
 */
@Entity
@Table(name = "USERS")
public class User extends AbstractEntity {

    @Column(name = "FIRST_NAME", length = USER_FIRST_NAME_MAX_LENGTH, nullable = false)
    private String firstName = "";
    @Column(name = "LAST_NAME", length = USER_LAST_NAME_MAX_LENGTH, nullable = true)
    private String lastName = "";
    @Column(name = "EXTERNAL_ID", length = USER_EXTERNAL_ID_MAX_LENGTH, nullable = false, unique = true)
    private String externalId = "";

    /**
     * Returns a user's first name.
     * 
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets a user's first name, only the first {@link Constants.USER_FIRST_NAME_MAX_LENGTH} characters are stored.
     * 
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = limitString(firstName, USER_FIRST_NAME_MAX_LENGTH);
    }

    /**
     * Returns a user's last name.
     * 
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets a user's last name, only the first {@link Constants.USER_LAST_NAME_MAX_LENGTH} characters are stored.
     * 
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = limitString(lastName, USER_LAST_NAME_MAX_LENGTH);
    }

    /**
     *  Returns a user's ID in an external system from which the user originates.
     * 
     * @return the external ID
     */
    public String getExternalId() {
        return externalId;
    }

    /**
     * Sets a user's ID in an external system, only the first {@link Constants.USER_EXTERNAL_ID_MAX_LENGTH} characters are stored.
     * 
     * @param externalId the external ID
     */
    public void setExternalId(String externalId) {
        this.externalId = limitString(externalId, USER_EXTERNAL_ID_MAX_LENGTH);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(getExternalId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User other = (User) obj;
            return Objects.equal(this.getExternalId(), other.getExternalId());
        }
        return false;
    }

}
