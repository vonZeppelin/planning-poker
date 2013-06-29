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
package org.lbogdanov.poker.web.markup;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.FeedbackMessages;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.Strings;

import com.google.common.collect.Iterators;


/**
 * A <code>Border</code> to ease construction of Twitter Bootstrap control groups that support validation.
 * 
 * @author Leonid Bogdanov
 */
public class ControlGroup extends Border {

    // feedback message types ordered by seriousness
    private static final int[] MESSAGE_TYPES = {FeedbackMessage.FATAL, FeedbackMessage.ERROR, FeedbackMessage.WARNING,
                                                FeedbackMessage.SUCCESS, FeedbackMessage.INFO, FeedbackMessage.DEBUG,
                                                FeedbackMessage.UNDEFINED};

    private final Component label;

    /**
     * @see Border#Border(String)
     */
    public ControlGroup(String id) {
        this(id, Model.of(""));
    }

    /**
     * @see Border#Border(String, IModel)
     */
    public ControlGroup(String id, IModel<String> model) {
        super(id, model);
        addToBorder(label = new Label("label", getDefaultModel()));
        add(AttributeModifier.append("class", "control-group"));
        add(AttributeModifier.append("class", new AbstractReadOnlyModel<String>() {

            @Override
            public String getObject() {
                FormComponent<?> formComponent = lastFormComponent();
                if (formComponent != null) {
                    FeedbackMessages messages = formComponent.getFeedbackMessages();
                    if (!messages.isEmpty()) {
                        for (int type : MESSAGE_TYPES) {
                            FeedbackMessage message = messages.first(type);
                            if (message != null) {
                                return messageToCssClass(message);
                            }
                        }
                        return messageToCssClass(messages.first());
                    }
                }
                return null;
            }

        }));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onInitialize() {
        super.onInitialize();

        FormComponent<?> formComponent = lastFormComponent();
        if (formComponent != null) {
            formComponent.setOutputMarkupId(true); // always output component id so the label can reference it
            label.add(AttributeModifier.replace("for", formComponent.getMarkupId()));
            IModel<String> labelModel = formComponent.getLabel();
            if (labelModel != null && !Strings.isEmpty(labelModel.getObject())) {
                label.setDefaultModel(labelModel);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onConfigure() {
        super.onConfigure();
        label.setVisible(!Strings.isEmpty(label.getDefaultModelObjectAsString()));
    }

    /**
     * Returns a Twitter Bootstrap CSS class for a specified <code>FeedbackMessageE</code>.
     * 
     * @param message the <code>FeedbackMessage</code>
     * @return the CSS class name
     */
    private static String messageToCssClass(FeedbackMessage message) {
        switch (message.getLevel()) {
            case FeedbackMessage.FATAL:
            case FeedbackMessage.ERROR:
                return "error";
            case FeedbackMessage.WARNING:
                return "warning";
            case FeedbackMessage.SUCCESS:
                return "success";
            case FeedbackMessage.INFO:
                return "info";
            default:
                return "";
        }
    }

    /**
     * Returns the last child <code>FormComponent</code>.
     * 
     * @return the last child of type <code>FormComponent</code>
     */
    private FormComponent<?> lastFormComponent() {
        // TODO More tricky traversal?
        return (FormComponent<?>) Iterators.getLast(getBodyContainer().visitChildren(FormComponent.class), null);
    }

}
