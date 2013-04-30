package org.lbogdanov.poker.web.markup;

import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * An {@link AjaxPagingNavigator} subclass with Twitter Bootstrap pagination markup.
 * 
 * @author Alexandra Fomina
 */
public class BootstrapPagingNavigator extends AjaxPagingNavigator {

    /**
     * @see AjaxPagingNavigator#AjaxPagingNavigator(String, IPageable)
     */
    public BootstrapPagingNavigator(String id, IPageable pageable) {
        super(id, pageable);
    }

}
