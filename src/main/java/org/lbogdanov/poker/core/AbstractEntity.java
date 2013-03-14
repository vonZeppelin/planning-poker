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

import java.io.Serializable;

import javax.persistence.*;

import com.google.common.base.Strings;


/**
 * A parent class to be extended by every JPA-mapped entity.
 * 
 * @author Leonid Bogdanov
 */
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false, unique = true)
    private Long id;

    /**
     * Returns an ID value.
     * 
     * @return the ID value
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets an ID value.
     * 
     * @return the ID value
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract int hashCode();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract boolean equals(Object obj);

    /**
     * Ensures that an input string won't be longer than a specified limit, treats <code>null</code> as empty strings.
     * 
     * @param string the input string, can be <code>null</code>
     * @param maxLen the max length of the output string
     * @return the output string
     */
    protected static String limitString(String string, int maxLen) {
        String str = Strings.nullToEmpty(string);
        return str.substring(0, Math.min(maxLen, str.length()));
    }

}
