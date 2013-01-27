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

import org.apache.wicket.Component;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.panel.FeedbackPanel;


/**
 * A {@link FeedbackPanel} subclass that adapts Twitter Bootstrap alert styles to a messages list.
 * 
 * @author Leonid Bogdanov
 */
public class BootstrapFeedbackPanel extends FeedbackPanel {

    /**
     * @see FeedbackPanel#FeedbackPanel(String)
     */
    public BootstrapFeedbackPanel(String id) {
        super(id);
    }

    /**
     * @see FeedbackPanel#FeedbackPanel(String, IFeedbackMessageFilter)
     */
    public BootstrapFeedbackPanel(String id, IFeedbackMessageFilter filter) {
        super(id, filter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getCSSClass(FeedbackMessage message) {
        switch (message.getLevel()) {
            case FeedbackMessage.INFO:
            case FeedbackMessage.DEBUG:
                return "alert alert-info";
            case FeedbackMessage.SUCCESS:
                return "alert alert-success";
            case FeedbackMessage.ERROR:
            case FeedbackMessage.FATAL:
                return "alert alert-error";
            case FeedbackMessage.WARNING:
            case FeedbackMessage.UNDEFINED:
            default:
                return "alert";
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Component newMessageDisplayComponent(String id, FeedbackMessage message) {
        return super.newMessageDisplayComponent(id, message).setRenderBodyOnly(true);
    }

}
