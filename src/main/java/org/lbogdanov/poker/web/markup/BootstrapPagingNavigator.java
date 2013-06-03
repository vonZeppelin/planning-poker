package org.lbogdanov.poker.web.markup;

import static org.apache.wicket.AttributeModifier.append;

import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigation;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.markup.html.navigation.paging.*;
import org.apache.wicket.model.AbstractReadOnlyModel;

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

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onInitialize() {
        super.onInitialize();
        PagingNavigationLink<?> first = (PagingNavigationLink<?>) get("first");
        PagingNavigationLink<?> last = (PagingNavigationLink<?>) get("last");
        PagingNavigationIncrementLink<?> prev = (PagingNavigationIncrementLink<?>) get("prev");
        PagingNavigationIncrementLink<?> next = (PagingNavigationIncrementLink<?>) get("next");

        first.setAutoEnable(false);
        last.setAutoEnable(false);
        prev.setAutoEnable(false);
        next.setAutoEnable(false);

        add(new TransparentWebMarkupContainer("firstParent").add(activeAppender(first)),
            new TransparentWebMarkupContainer("lastParent").add(activeAppender(last)),
            new TransparentWebMarkupContainer("prevParent").add(activeAppender(prev)),
            new TransparentWebMarkupContainer("nextParent").add(activeAppender(next)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PagingNavigation newNavigation(String id, IPageable pageable, IPagingLabelProvider labelProvider) {
        return new AjaxPagingNavigation(id, pageable, labelProvider) {

            @Override
            protected void populateItem(LoopItem loopItem) {
                super.populateItem(loopItem);
                PagingNavigationLink<?> link = (PagingNavigationLink<?>) loopItem.get("pageLink");
                link.setAutoEnable(false);
                loopItem.add(activeAppender(link));
            }

        };
    }

    private AttributeAppender activeAppender(final PagingNavigationLink<?> link) {
        return append("class", new AbstractReadOnlyModel<String>() {

            @Override
            public String getObject() {
                return link.linksTo(getPage()) ? "active" : null;
            }

        });
    }

    private AttributeAppender activeAppender(final PagingNavigationIncrementLink<?> link) {
        return append("class", new AbstractReadOnlyModel<String>() {

            @Override
            public String getObject() {
                return link.linksTo(getPage()) ? "active" : null;
            }

        });
    }

}
