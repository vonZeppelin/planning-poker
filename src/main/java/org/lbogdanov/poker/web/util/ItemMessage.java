package org.lbogdanov.poker.web.util;

import org.lbogdanov.poker.core.Item;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Alexandra Fomina
 */
@JsonTypeName("itemAdd")
public class ItemMessage extends Message<Item> {

    public ItemMessage(Object origin, Item message) {
        super(origin, message);
    }

}
