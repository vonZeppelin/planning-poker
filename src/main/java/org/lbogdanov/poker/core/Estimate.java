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
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

/**
 * Represents a session item estimate.
 * 
 * @author Alexandra Fomina
 */
@Entity
@Table(name = "ESTIMATES")
@IdClass(EstimatePrimaryKey.class)
public class Estimate implements Serializable {

    static final int MINUTES_PER_HOUR = 60;
    static final int MINUTES_PER_DAY = MINUTES_PER_HOUR * 8;
    static final int MINUTES_PER_WEEK = MINUTES_PER_DAY * 5;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private User user;
    @ManyToOne
    @PrimaryKeyJoinColumn(name = "ITEM_ID", referencedColumnName = "ID")
    private Item item;
    @Column(name = "ESTIMATE", nullable = false)
    private Integer value;

    /**
     * Parses the given <code>String</code> to a <code>List</code> of <code>Duration</code> objects.
     * The <code>String</code> represents duration values in minutes, hours, days or weeks,
     * separated with spaces, commas or semicolons. For example "30m 4h 1d 2w",
     * where 'm' stands for minute, 'h' - hour, 'd' - day and 'w' - week.
     * 
     * @param input a <code>String</code> to parse
     * @return the durations represented by the given <code>String</code>
     * @throws IllegalArgumentException if the given <code>String</code> doesn't matches syntax
     */
    public static List<Estimate> parse(String input) {
        List<Estimate> durations = new ArrayList<Estimate>();
        StringBuilder duration = new StringBuilder();
        for (char chr : input.toCharArray()) {
            if (Character.isWhitespace(chr) || chr == ',' || chr == ';') {
                continue;
            }
            if (Character.isDigit(chr)) {
                duration.append(chr);
            } else {
                int mul;
                switch (chr) {
                    case 'm':
                        mul = 1;
                        break;
                    case 'h':
                        mul = MINUTES_PER_HOUR;
                        break;
                    case 'd':
                        mul = MINUTES_PER_DAY;
                        break;
                    case 'w':
                        mul = MINUTES_PER_WEEK;
                        break;
                   default:
                       throw new IllegalArgumentException(duration.toString() + chr);
                }
                durations.add(new Estimate(Integer.parseInt(duration.toString()) * mul));
                duration.setLength(0);
            }
        }
        if (duration.length() > 0) {
            throw new IllegalArgumentException(duration.toString());
        }
        return durations;
    }

    /**
     * Creates a zero valued <code>Estimate</code> instance.
     */
    public Estimate() {
        value = 0;
    }

    /**
     * Creates an <code>Estimate</code> instance with a specified value.
     * 
     * @param value the estimate value in minutes
     */
    public Estimate(int value) {
        setValue(value);
    }

    /**
     * Returns an estimate author.
     * 
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets an estimate author.
     * 
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Returns an item evaluated by an estimate.
     * 
     * @return the item
     */
    public Item getItem() {
        return item;
    }

    /**
     * Sets an estimation item.
     * 
     * @param item the item to set
     */
    public void setItem(Item item) {
        this.item = item;
    }

    /**
     * Returns an estimate value.
     * 
     * @return the estimate value
     */
    public Integer getValue() {
        return value;
    }

    /**
     * Sets an estimate value.
     * 
     * @param value the estimate value to set, must be non-negative
     */
    public void setValue(Integer value) {
        Preconditions.checkArgument(value >= 0, "Value must be non-negative");
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(getItem(), getUser());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Estimate) {
            Estimate other = (Estimate) obj;
            return Objects.equal(this.getItem(), other.getItem())
                   && Objects.equal(this.getUser(), other.getUser());
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        int n = value;
        if (n == 0) {
            return "0";
        }
        List<String> duration = new ArrayList<String>(4);
        if (n >= MINUTES_PER_WEEK) {
            duration.add((n / MINUTES_PER_WEEK) + "w");
            n %= MINUTES_PER_WEEK;
        }
        if (n >= MINUTES_PER_DAY) {
            duration.add((n / MINUTES_PER_DAY) + "d");
            n %= MINUTES_PER_DAY;
        }
        if (n >= MINUTES_PER_HOUR) {
            duration.add((n / MINUTES_PER_HOUR) + "h");
            n %= MINUTES_PER_HOUR;
        }
        if (n > 0) {
            duration.add(n + "m");
        }
        return Joiner.on(' ').join(duration);
    }

}
