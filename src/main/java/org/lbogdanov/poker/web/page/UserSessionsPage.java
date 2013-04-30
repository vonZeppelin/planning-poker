package org.lbogdanov.poker.web.page;

import static org.lbogdanov.poker.core.Constants.ITEMS_PER_PAGE;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackHeadersToolbar;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxNavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.*;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.lbogdanov.poker.core.*;
import org.lbogdanov.poker.web.markup.BootstrapPagingNavigator;
import org.lbogdanov.poker.web.markup.SessionActionPanel;

import com.google.common.collect.ImmutableList;

/**
 * Represents a page containing information about Planning Poker sessions created by current user.
 * 
 * @author Alexandra Fomina
 */
@RequiresUser
public class UserSessionsPage extends AbstractPage {

    /**
     * A {@link SortableDataProvider} subclass that provides Planning Poker sessions created by current user.
     */
    private static final class SortableSessionProvider extends SortableDataProvider<Session, String> {

        @Inject
        private SessionService sessionService;
        @Inject
        private UserService userService;

        public SortableSessionProvider() {
            Injector.get().inject(this);
            setSort(new SortParam<String>("created", false));
        }

        @Override
        public Iterator<? extends Session> iterator(long first, long count) {
            List<? extends Session> sessions = getSessions().getPage((int) (first / ITEMS_PER_PAGE));
            long fromIdx = first % ITEMS_PER_PAGE;
            long toIdx = Math.min(fromIdx + count, sessions.size());
            return sessions.subList((int) fromIdx, (int) toIdx).iterator();
        }

        @Override
        public void detach() {}

        @Override
        public long size() {
            return getSessions().getTotalRowCount();
        }

        @Override
        public IModel<Session> model(Session object) {
            return Model.of(object);
        }

        private PagingList<Session> getSessions() {
            return sessionService.find(userService.getCurrentUser(), getSort().getProperty(), getSort().isAscending(), ITEMS_PER_PAGE);
        }

    }

    /**
     * Creates a new instance of <code>UserSessions</code> page.
     */
    public UserSessionsPage() {
        final WebMarkupContainer sessions = new WebMarkupContainer("sessionsContainer");
        List<? extends IColumn<Session, String>> columns = ImmutableList.of(
                new PropertyColumn<Session, String>(new ResourceModel("session.name"), "name", "name"),
                new PropertyColumn<Session, String>(new ResourceModel("session.description"), "description"),
                new PropertyColumn<Session, String>(new ResourceModel("session.created"), "created", "created"),
                new PropertyColumn<Session, String>(new ResourceModel("session.author"), "author", "author"),
                new AbstractColumn<Session, String>(new ResourceModel("session.actions")) {

                    @Override
                    public void populateItem(Item<ICellPopulator<Session>> cellItem, String componentId, IModel<Session> rowModel) {
                        cellItem.add(new SessionActionPanel(componentId, rowModel));
                    }

        });
        ISortableDataProvider<Session, String> dataProvider =  new SortableSessionProvider();
        DataTable<?, String> sessionTable = new DataTable<Session, String>("sessions", columns, dataProvider, ITEMS_PER_PAGE);
        sessionTable.addTopToolbar(new AjaxFallbackHeadersToolbar<String>(sessionTable, dataProvider));
        sessionTable.addBottomToolbar(new AjaxNavigationToolbar(sessionTable) {

            @Override
            protected PagingNavigator newPagingNavigator(String navigatorId, final DataTable<?, ?> table) {
                return new BootstrapPagingNavigator(navigatorId, table) {

                    @Override
                    protected void onAjaxEvent(AjaxRequestTarget target) {
                        target.add(table);
                    }

                };
            }

        });
        sessionTable.addBottomToolbar(new NoRecordsToolbar(sessionTable, new ResourceModel("sessions.noRecords")));
        sessions.add(sessionTable.setOutputMarkupId(true));
        add(sessions.setOutputMarkupId(true));
    }

}
