package org.lbogdanov.poker.web.util;

import org.lbogdanov.poker.core.Item;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Alexandra Fomina
 */
@JsonTypeName("itemAdd")
public class ItemMessage extends Message<Item> {

    private String markupId;

    public ItemMessage(Object origin, Item message) {
        super(origin, message);
    }

    /**
     * @return the markupId
     */
    public String getMarkupId() {
        return markupId;
    }

    /**
     * @param markupId the markupId to set
     */
    public void setMarkupId(String markupId) {
        this.markupId = markupId;
    }

}
