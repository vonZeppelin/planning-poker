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

import java.io.Serializable;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

import com.google.common.base.Preconditions;


/**
 * A <code>Label</code> that can have a limit on a length of a displayed string.
 * 
 * @author Leonid Bogdanov
 */
public class LimitableLabel extends Label {

    private int maxLength = -1;

    /**
     * @see Label#Label(String)
     */
    public LimitableLabel(String id) {
        super(id);
    }

    /**
     * @see Label#Label(String, String)
     */
    public LimitableLabel(String id, String label) {
        super(id, label);
    }

    /**
     * @see Label#Label(String, Serializable)
     */
    public LimitableLabel(String id, Serializable label) {
        super(id, label);
    }

    /**
     * @see Label#Label(String, IModel)
     */
    public LimitableLabel(String id, IModel<?> model) {
        super(id, model);
    }

    /**
     * Sets a maximum length for a displayed string: a longer string will be abbreviated with ellipses.
     * 
     * @param length the maximum length, should be greater than 3 or -1 for no limit
     * @return this object
     */
    public LimitableLabel setMaxLength(int length) {
        Preconditions.checkArgument(length == -1 || length > 3, "legnth");
        maxLength = length;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
        String label = getDefaultModelObjectAsString();
        if (maxLength != -1 && label != null && label.length() > maxLength) {
            label = label.substring(0, maxLength - 3) + "...";
        }
        replaceComponentTagBody(markupStream, openTag, label);
    }

}
