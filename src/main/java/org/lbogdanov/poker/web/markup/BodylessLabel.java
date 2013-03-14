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

import org.apache.wicket.model.IModel;


/**
 * A <code>LimitableLabel</code> that by default doesn't render its body.
 * 
 * @author Leonid Bogdanov
 */
public class BodylessLabel extends LimitableLabel {

    /**
     * @see LimitableLabel#LimitableLabel(String)
     */
    public BodylessLabel(String id) {
        super(id);
        setRenderBodyOnly(true);
    }

    /**
     * @see LimitableLabel#LimitableLabel(String, String)
     */
    public BodylessLabel(String id, String label) {
        super(id, label);
        setRenderBodyOnly(true);
    }

    /**
     * @see LimitableLabel#LimitableLabel(String, Serializable)
     */
    public BodylessLabel(String id, Serializable label) {
        super(id, label);
        setRenderBodyOnly(true);
    }

    /**
     * @see LimitableLabel#LimitableLabel(String, IModel)
     */
    public BodylessLabel(String id, IModel<?> model) {
        super(id, model);
        setRenderBodyOnly(true);
    }

}
