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
package org.lbogdanov.poker.web.plugin;

import java.util.Arrays;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.resource.JQueryPluginResourceReference;

import com.google.common.collect.Iterables;


/**
 * A <code>ResourceReference</code> for jQuery custom scrollbar plugin.
 * 
 * @author Leonid Bogdanov
 */
public class CustomScrollbarPlugin extends JQueryPluginResourceReference {

    private static final CustomScrollbarPlugin INSTANCE = new CustomScrollbarPlugin();
    private static final ResourceReference MOUSEWHEEL = new JavaScriptResourceReference(CustomScrollbarPlugin.class,
                                                                                        "jquery.mousewheel.js");
    private static final ResourceReference CSS = new CssResourceReference(CustomScrollbarPlugin.class,
                                                                          "jquery.mCustomScrollbar.css");

    /**
     * Returns a single instance of jQuery custom scrollbar plugin resource reference.
     * 
     * @return the single instance
     */
    public static CustomScrollbarPlugin get() {
            return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<? extends HeaderItem> getDependencies() {
        return Iterables.concat(super.getDependencies(), Arrays.<HeaderItem>asList(JavaScriptHeaderItem.forReference(MOUSEWHEEL),
                                                                                   CssHeaderItem.forReference(CSS)));
    }

    private CustomScrollbarPlugin() {
        super(CustomScrollbarPlugin.class, "jquery.mCustomScrollbar.js");
    }

}
