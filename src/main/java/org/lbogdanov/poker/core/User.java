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
@Table(name="USERS")
public class User extends AbstractEntity {

    @Column(name="FIRST_NAME", length=128, nullable=false)
    private String firstName;
    @Column(name="LAST_NAME", length=128, nullable=true)
    private String lastName;
    @Column(name="EXTERNAL_ID", length=64, nullable=false, unique=true)
    private String externalId;

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the externalId
     */
    public String getExternalId() {
        return externalId;
    }

    /**
     * @param externalId the externalId to set
     */
    public void setExternalId(String externalId) {
        this.externalId = externalId;
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
