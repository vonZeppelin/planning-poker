package org.lbogdanov.poker.core;

import static org.lbogdanov.poker.core.Constants.ITEM_DESCRIPTION_MAX_LENGTH;
import static org.lbogdanov.poker.core.Constants.ITEM_TITLE_MAX_LENGTH;

import javax.persistence.*;

import com.google.common.base.Objects;

/**
 * Represents an item for Planning Poker session evaluation.
 * 
 * @author Alexandra Fomina
 */
@Entity
@Table(name = "ITEMS")
public class Item extends AbstractEntity {

    @Column(name = "TITLE", length = ITEM_TITLE_MAX_LENGTH, nullable = false)
    private String title = "";
    @Column(name = "DESCRIPTION", length = ITEM_DESCRIPTION_MAX_LENGTH, nullable = true)
    private String description = "";
    @ManyToOne
    @JoinColumn(name = "SESSION_ID", nullable = false)
    private Session session;

    /**
     * Returns an item title.
     * 
     * @return the name
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets an item title.
     * 
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns an item description.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets an item description.
     * 
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(getTitle());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Item) {
            Item other = (Item) obj;
            return Objects.equal(this.getTitle(), other.getTitle()) && 
                   Objects.equal(this.getDescription(), other.getDescription());
        }
        return false;
    }

}
