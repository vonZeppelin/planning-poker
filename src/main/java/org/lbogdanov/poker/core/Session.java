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

import static org.lbogdanov.poker.core.Constants.SESSION_CODE_MAX_LENGTH;
import static org.lbogdanov.poker.core.Constants.SESSION_DESCRIPTION_MAX_LENGTH;
import static org.lbogdanov.poker.core.Constants.SESSION_ESTIMATES_MAX_LENGTH;
import static org.lbogdanov.poker.core.Constants.SESSION_NAME_MAX_LENGTH;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;


/**
 * Represents a Planning Poker session.
 * 
 * @author Leonid Bogdanov
 */
@Entity
@Table(name = "SESSIONS")
public class Session extends AbstractEntity {

    @Column(name = "NAME", length = SESSION_NAME_MAX_LENGTH, nullable = false)
    private String name = "";
    @Column(name = "CODE", length = SESSION_CODE_MAX_LENGTH, nullable = false, unique = true)
    private String code = "";
    @Column(name = "CREATED", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created = new Date();
    @Column(name = "DESCRIPTION", length = SESSION_DESCRIPTION_MAX_LENGTH, nullable = true)
    private String description = "";
    @ManyToOne(optional = false)
    @JoinColumn(name = "AUTHOR_ID", nullable = false)
    private User author;
    @Column(name = "ESTIMATES", length = SESSION_ESTIMATES_MAX_LENGTH, nullable = false)
    private String estimates;
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Item> items = Collections.emptyList();

    /**
     * Returns a session name.
     * 
     * @return the session name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets a session name, only the first {@link Constants.SESSION_NAME_MAX_LENGTH} characters are stored.
     * 
     * @param name the session name
     */
    public void setName(String name) {
        this.name = limitString(name, SESSION_NAME_MAX_LENGTH);
    }

    /**
     * Returns a session code.
     * 
     * @return the session code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = limitString(code, SESSION_CODE_MAX_LENGTH);
    }

    /**
     * Returns a session description.
     * 
     * @return the session description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets a session description, only the first {@link Constants.SESSION_DESCRIPTION_MAX_LENGTH} characters are stored.
     * 
     * @param description the session description
     */
    public void setDescription(String description) {
        this.description = limitString(description, SESSION_DESCRIPTION_MAX_LENGTH);
    }

    /**
     * Returns a session author (creator).
     * 
     * @return the session author (creator)
     */
    public User getAuthor() {
        return author;
    }

    /**
     * Sets a session author (creator).
     * 
     * @param author the session author (creator)
     */
    public void setAuthor(User author) {
        this.author = author;
    }

    /**
     * Returns a session creation date.
     * 
     * @return the session creation date
     */
    public Date getCreated() {
        return created;
    }

    /**
     * Sets a session creation date.
     * 
     * @param date the session creation date
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * Returns session estimates.
     * 
     * @return the estimates that is used by the session
     */
    public String getEstimates() {
        return estimates;
    }

    /**
     * Sets session estimates.
     * 
     * @param estimates the estimates that is used by the session
     */
    public void setEstimates(String estimates) {
        this.estimates = estimates;
    }

    /**
     * Returns session items.
     * 
     * @return the items
     */
    public List<Item> getItems() {
        return ImmutableList.copyOf(items);
    }

    /**
     * Sets session items.
     * 
     * @param items the items to set
     */
    public void setItems(List<Item> items) {
        this.items = items;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(getCode());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Session) {
            Session other = (Session) obj;
            return Objects.equal(getCode(), other.getCode());
        }
        return false;
    }

}
