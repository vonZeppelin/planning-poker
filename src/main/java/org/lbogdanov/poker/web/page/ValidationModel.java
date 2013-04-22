package org.lbogdanov.poker.web.page;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.util.LazyInitializer;


/**
 * A helper class to add Bootstrap validation styles to a control group.
 */
final class ValidationModel extends AbstractReadOnlyModel<String> {

    private String cssClass;
    private LazyInitializer<FormComponent<?>> field;

    public ValidationModel(final Form<?> form, final String field, String cssClass) {
        this.cssClass = cssClass;
        this.field = new LazyInitializer<FormComponent<?>>() {

            @Override
            protected FormComponent<?> createInstance() {
                return (FormComponent<?>) form.get(field);
            }

        };
    }

    @Override
    public String getObject() {
        return field.get().isValid() ? null : cssClass;
    }

}
