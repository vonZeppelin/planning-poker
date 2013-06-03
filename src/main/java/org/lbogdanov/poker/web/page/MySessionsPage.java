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
package org.lbogdanov.poker.web.page;

import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.ThrottlingSettings;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.sort.AjaxFallbackOrderByBorder;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackHeadersToolbar;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxNavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NoRecordsToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.*;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.time.Duration;
import org.lbogdanov.poker.core.PagingList;
import org.lbogdanov.poker.core.Session;
import org.lbogdanov.poker.core.SessionService;
import org.lbogdanov.poker.core.UserService;
import org.lbogdanov.poker.web.markup.BootstrapPagingNavigator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;

/**
 * Represents a page containing information about Planning Poker sessions that current user created or participated in.
 * 
 * @author Alexandra Fomina
 */
@RequiresUser
public class MySessionsPage extends AbstractPage {

    /**
     * A <code>SortableDataProvider</code> of Planning Poker sessions that current user created or participated in.
     */
    private final class SessionsProvider extends SortableDataProvider<Session, String> {

        private transient PagingList<Session> data;
        private transient String sessionName;

        @Override
        public Iterator<? extends Session> iterator(long first, long count) {
            if (data == null) {
                data = load();
            }
            int page = (int) (first / data.getPageSize());
            return Iterators.limit(data.getPage(page).iterator(), (int) count);
        }

        @Override
        public long size() {
            if (data == null) {
                data = load();
            }
            return data.getTotalRowCount();
        }

        @Override
        public IModel<Session> model(Session object) {
            final Long id = object.getId();
            return new LoadableDetachableModel<Session>(object) {

                @Override
                protected Session load() {
                    return sessionService.find(id);
                }

            };
        }

        public void invalidate() {
            data = null;
        }

        public String getSessionName() {
            return sessionName;
        }

        public void setSessionName(String name) {
            this.sessionName = name;
        }

        private PagingList<Session> load() {
            SortParam<String> sort = getSort();
            return sessionService.find(userService.getCurrentUser(), getSessionName(), sort.getProperty(),
                                       sort.isAscending(), (int) sessionsTable.getItemsPerPage());
        }

    }

    /**
     * Represents a column in the Planning Poker sessions table.
     */
    private static class Column extends PropertyColumn<Session, String> {

        public Column(String resourceKey, String sortProperty, String propertyExpression) {
            super(new ResourceModel(resourceKey), sortProperty, propertyExpression);
        }

        @Override
        public String getCssClass() {
            return getPropertyExpression();
        }

    }

    private static final ResourceReference CSS = new CssResourceReference(MySessionsPage.class, "mysessions.css");
    private static final List<Long> ITEMS_PER_PAGE = ImmutableList.of(10L, 50L, 100L);

    @Inject
    private SessionService sessionService;
    @Inject
    private UserService userService;
    private DataTable<?, String> sessionsTable;

    /**
     * Creates a new instance of <code>MySessionsPage</code> page.
     */
    public MySessionsPage() {
        final SessionsProvider dataProvider =  new SessionsProvider();
        dataProvider.setSort("created", SortOrder.DESCENDING); // default sort: created, desc
        List<AbstractColumn<Session, String>> columns = ImmutableList.of(
            new Column("session.name", "name", "name"),
            new Column("session.description", null, "description"),
            new Column("session.created", "created", "created") {

                @Override
                @SuppressWarnings({"rawtypes", "unchecked"})
                public IModel<Object> getDataModel(IModel<Session> rowModel) {
                    Date created = (Date) super.getDataModel(rowModel).getObject();
                    String formatted = DateFormat.getDateTimeInstance(DateFormat.SHORT,
                                                                      DateFormat.SHORT,
                                                                      getLocale()).format(created);
                    return new Model(formatted);
                }

            },
            new Column("session.author", "author", "author"),
            new AbstractColumn<Session, String>(new ResourceModel("session.actions")) {

                @Override
                public void populateItem(Item<ICellPopulator<Session>> item, String compId, IModel<Session> model) {
                    PageParameters params = new PageParameters().add("code", model.getObject().getCode());
                    Link<?> go = new BookmarkablePageLink<Void>("goto", SessionPage.class, params);
                    Link<?> delete = new AjaxFallbackLink<Session>("delete", model) { // TODO Add confirmation

                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            sessionService.delete(getModelObject());
                            dataProvider.invalidate();
                            if (target != null) {
                                target.add(sessionsTable);
                            }
                        }

                    };
                    item.add(new Fragment(compId, "actions", MySessionsPage.this).add(go, delete));
                }

                @Override
                public String getCssClass() {
                    return "actions";
                }

            }
        );
        sessionsTable = new DataTable<Session, String>("sessions", columns, dataProvider, ITEMS_PER_PAGE.get(0));
        sessionsTable.addTopToolbar(new AjaxFallbackHeadersToolbar<String>(sessionsTable, dataProvider) {

            @Override
            protected WebMarkupContainer newSortableHeader(String borderId, String property, ISortStateLocator<String> locator) {
                return new AjaxFallbackOrderByBorder<String>(borderId, property, locator, getAjaxCallListener()) {

                    @Override
                    protected void onAjaxClick(AjaxRequestTarget target) {
                        target.add(getTable());
                    }

                    @Override
                    protected void onSortChanged() {
                        dataProvider.invalidate();
                        getTable().setCurrentPage(0);
                    }

                };
            }

        });
        sessionsTable.addBottomToolbar(new AjaxNavigationToolbar(sessionsTable) {

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
        sessionsTable.addBottomToolbar(new NoRecordsToolbar(sessionsTable));

        TextField<?> sessionName = new TextField<String>("sessionName",
                                                         PropertyModel.<String>of(dataProvider, "sessionName"));
        sessionName.add(new OnChangeAjaxBehavior() {

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                dataProvider.invalidate();
                target.add(sessionsTable);
            }

            @Override
            protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                super.updateAjaxAttributes(attributes);
                ThrottlingSettings throttling = new ThrottlingSettings("sessionName",
                                                                       Duration.milliseconds(300),
                                                                       true);
                attributes.setThrottlingSettings(throttling);
            }

        });
        DropDownChoice<?> pageSize = new DropDownChoice<Long>("pageSize",
                                                              PropertyModel.<Long>of(sessionsTable, "itemsPerPage"),
                                                              ITEMS_PER_PAGE);
        pageSize.add(new OnChangeAjaxBehavior() {

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                dataProvider.invalidate();
                target.add(sessionsTable);
            }

        });
        add(sessionsTable.setOutputMarkupId(true), sessionName, pageSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(CSS));
    }

}
