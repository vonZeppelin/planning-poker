package org.lbogdanov.poker.web.markup;

import javax.inject.Inject;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.lbogdanov.poker.core.Session;
import org.lbogdanov.poker.core.SessionService;
import org.lbogdanov.poker.web.page.SessionPage;

/**
 * Represents an interface for user to interact with Planning Poker session.
 * 
 * @author Alexandra Fomina
 * 
 */
public class SessionActionPanel extends Panel {

    @Inject
    private SessionService sessionService;

    public SessionActionPanel(String id, IModel<Session> model) {
        super(id, model);
        Session session = model.getObject();
        add(new BookmarkablePageLink<Void>("sessionLink", SessionPage.class, new PageParameters().add("code", session.getCode())),
            new AjaxFallbackLink<Session>("delete", Model.of(session)) {

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        sessionService.delete(getModelObject());
                        if (target != null) {
                            MarkupContainer parent = SessionActionPanel.this.getParent();
                            while (!(parent instanceof DataTable)) {
                                parent = parent.getParent();
                            }
                            target.add(parent);
                        }
                    }

        });
    }

}
