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

import java.util.Collections;

import org.apache.wicket.bootstrap.Bootstrap;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.resource.JQueryPluginResourceReference;

import com.google.common.collect.Iterables;


/**
 * A {@link ResourceReference} for a JavaScript resource that depends on JQuery and Twitter Bootstrap libs.
 * 
 * @author Leonid Bogdanov
 */
class PageScriptResourceReference extends JQueryPluginResourceReference {

    /**
     * @see JQueryPluginResourceReference#JQueryPluginResourceReference(Class, String)
     */
    public PageScriptResourceReference(Class<?> scope, String name) {
        super(scope, name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<? extends HeaderItem> getDependencies() {
        return Iterables.concat(super.getDependencies(),
                                Collections.singletonList(JavaScriptHeaderItem.forReference(Bootstrap.plain())));
    }

}
