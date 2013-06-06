package org.lbogdanov.poker.web.util;

import org.lbogdanov.poker.core.Item;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Alexandra Fomina
 */
@JsonTypeName("itemEdit")
public class ItemChangeEvent extends Message<Item> {
 
    public ItemChangeEvent(Object origin, Item message) {
        super(origin, message);
    }

}
