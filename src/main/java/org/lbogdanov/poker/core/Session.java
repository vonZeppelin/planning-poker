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
import static org.lbogdanov.poker.core.Constants.SESSION_NAME_MAX_LENGTH;

import javax.persistence.*;

import com.google.common.base.Objects;


/**
 * Represents a Planning Poker session.
 * 
 * @author Leonid Bogdanov
 */
@Entity
@Table(name="SESSIONS")
public class Session extends AbstractEntity {

    @Column(name="NAME", length=SESSION_NAME_MAX_LENGTH, nullable=false)
    private String name;
    @Column(name="CODE", length=SESSION_CODE_MAX_LENGTH, nullable=false, unique=true)
    private String code;
    @Lob
    @Column(name="DESCRIPTION", nullable=true)
    private String description;
    @ManyToOne(optional=false)
    @JoinColumn(name="AUTHOR_ID")
    private User author;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the author
     */
    public User getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(User author) {
        this.author = author;
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
        if (obj instanceof Session) {
            Session other = (Session) obj;
            return Objects.equal(getCode(), other.getCode());
        }
        return false;
    }

}
