package org.lbogdanov.poker.web.markup;

import javax.inject.Inject;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.lbogdanov.poker.core.Item;
import org.lbogdanov.poker.core.Session;
import org.lbogdanov.poker.core.SessionService;
import org.lbogdanov.poker.core.User;
import org.lbogdanov.poker.web.util.ItemChangeEvent;
import org.lbogdanov.poker.web.util.ItemRemoveEvent;
import org.lbogdanov.poker.web.util.Message;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

/**
 * Represents an UI for Planning Poker session item.
 * 
 * @author Alexandra Fomina
 */

public class ItemPanel<T> extends Panel {

    @Inject
    private SessionService sessionService;
    private Link<?> removeLink;
    private boolean isModerator;

    public ItemPanel(String id, final IModel<Item> model, final Session session, Object origin) {
        super(id, model);
        if (origin instanceof User) {
            isModerator = Objects.equal(origin, session.getAuthor());
        } else {
            isModerator = Objects.equal(origin, getSession().getId());
        }
        Component title;
        Component description;
        if (isModerator) {
            title = new AjaxEditableLabel<String>("title", new PropertyModel<String>(model.getObject(), "title")) {

                @Override
                protected void onSubmit(AjaxRequestTarget target) {
                    Item item = model.getObject();
                    String title = getEditor().getModelObject();
                    if (!Objects.equal(item.getTitle(), title)) {
                        item.setTitle(title);
                        itemChanged(item);
                    }
                    super.onSubmit(target);
                }

                @Override
                protected FormComponent<String> newEditor(MarkupContainer parent, String componentId, IModel<String> model) {
                    return super.newEditor(parent, componentId, model).setRequired(true);
                }


                @Override
                protected void onError(AjaxRequestTarget target) {
                    if (target != null) {
                        target.add(this);
                    }
                }

            };
            description = new AjaxEditableLabel<String>("description", new PropertyModel<String>(model.getObject(), "description")) {

                @Override
                protected void onSubmit(AjaxRequestTarget target) {
                    Item item = model.getObject();
                    String description = getEditor().getModelObject();
                    if (!Objects.equal(item.getDescription(), description)) {
                        item.setDescription(Strings.nullToEmpty(description));
                        itemChanged(item);
                    }
                    super.onSubmit(target);
                }

            };
        } else {
            title = new Label("title", new PropertyModel<String>(model.getObject(), "title"));
            description = new Label("description", new PropertyModel<String>(model.getObject(), "description"))
                                    .add(new AttributeAppender("id", "description"));
        }
        removeLink = new AjaxFallbackLink<Item>("remove", model) {

            @Override
            public void onClick(AjaxRequestTarget target) {
                Item item = this.getModelObject();
                sessionService.removeItem(item);
                send(getPage(), Broadcast.BREADTH, new ItemRemoveEvent(null, item));
            }

        };
        add(title.setOutputMarkupId(true), description, removeLink);

    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        removeLink.setVisible(isModerator);
    }

    private void itemChanged(Item item) {
        sessionService.saveItem(item);
        Message<Item> msg = new ItemChangeEvent(getSession().getId(), item);
        send(getPage(), Broadcast.BREADTH, msg);
    }

}