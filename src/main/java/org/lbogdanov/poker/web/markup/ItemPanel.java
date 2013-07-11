package org.lbogdanov.poker.web.markup;

import static org.lbogdanov.poker.core.Estimate.parse;

import javax.inject.Inject;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.lbogdanov.poker.core.*;
import org.lbogdanov.poker.web.plugin.SimpleSliderPlugin;
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
    private ItemService itemService;
    @Inject
    private UserService userService;
    private Link<?> removeLink;
    private boolean isModerator;

    @SuppressWarnings("unchecked")
    public ItemPanel(String id, final Item item, final Session session, Object origin) {
        super(id, Model.of(item));
        if (origin instanceof User) {
            isModerator = Objects.equal(origin, session.getAuthor());
        } else {
            isModerator = Objects.equal(origin, getSession().getId());
        }
        Component title;
        Component description;
        if (isModerator) {
            title = new AjaxEditableLabel<String>("title", new PropertyModel<String>(item, "title")) {

                @Override
                protected void onSubmit(AjaxRequestTarget target) {
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
            description = new AjaxEditableLabel<String>("description", new PropertyModel<String>(item, "description")) {

                @Override
                protected void onSubmit(AjaxRequestTarget target) {
                    String description = getEditor().getModelObject();
                    if (!Objects.equal(item.getDescription(), description)) {
                        item.setDescription(Strings.nullToEmpty(description));
                        itemChanged(item);
                    }
                    super.onSubmit(target);
                }

            };
        } else {
            title = new Label("title", PropertyModel.of(getDefaultModel(), "title"));
            description = new Label("description", PropertyModel.of(getDefaultModel(), "description"))
                                    .add(new AttributeAppender("id", "description"));
        }
        removeLink = new AjaxFallbackLink<Item>("remove", (IModel<Item>) getDefaultModel()) {

            @Override
            public void onClick(AjaxRequestTarget target) {
                Item item = this.getModelObject();
                itemService.delete(item);
                send(getPage(), Broadcast.BREADTH, new ItemRemoveEvent(null, item));
            }

        };

        Form<?> estimationForm = new Form<Void>("estimation");
        estimationForm.add(new TextField<String>("estIdx", Model.of("")).add(AttributeAppender.append("data-slider-range", "0," + (session.getEstimates().split("\\s").length - 1))),
                           new HiddenField<String>("estimate", Model.of("")), new AjaxFallbackButton("submit", estimationForm) {

                            @Override
                            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                                Estimate estimate = itemService.find(userService.getCurrentUser(), item);
                                if (estimate == null) {
                                    estimate = new Estimate();
                                }
                                String est = ((TextField<String>) form.get("estimate")).getModelObject();
                                estimate.setValue(parse(est).get(0).getValue());
                                if (estimate.getItem() == null) {
                                    estimate.setItem(item);
                                }
                                itemService.approve(estimate);
                            }

                        });
        add(title.setOutputMarkupId(true), description, removeLink, estimationForm.setOutputMarkupId(true));

    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptHeaderItem.forReference(SimpleSliderPlugin.get()));
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        removeLink.setVisible(isModerator);
    }

    private void itemChanged(Item item) {
        itemService.save(item);
        Message<Item> msg = new ItemChangeEvent(getSession().getId(), item);
        send(getPage(), Broadcast.BREADTH, msg);
    }

}