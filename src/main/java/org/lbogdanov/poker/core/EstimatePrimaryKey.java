package org.lbogdanov.poker.core;

import java.io.Serializable;

import com.google.common.base.Objects;

/**
 * A primary key for {@link Estimate} class.
 * 
 * @author afomina
 */
public class EstimatePrimaryKey implements Serializable {

    private User user;
    private Item item;

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the item
     */
    public Item getItem() {
        return item;
    }

    /**
     * @param item the item to set
     */
    public void setItem(Item item) {
        this.item = item;
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
        if (obj instanceof EstimatePrimaryKey) {
            EstimatePrimaryKey other = (EstimatePrimaryKey) obj;
            return Objects.equal(this.getItem(), other.getItem())
                   && Objects.equal(this.getUser(), other.getUser());
        }
        return false;
    }
}
