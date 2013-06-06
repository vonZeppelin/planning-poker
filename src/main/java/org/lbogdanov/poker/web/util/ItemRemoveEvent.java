package org.lbogdanov.poker.web.util;

import org.lbogdanov.poker.core.Item;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Alexandra Fomina
 */
@JsonTypeName("itemRemove")
public class ItemRemoveEvent extends Message<Item> {

    public ItemRemoveEvent(Object origin, Item message) {
        super(origin, message);
    }

}