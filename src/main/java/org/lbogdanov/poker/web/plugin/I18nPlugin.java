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

import org.apache.wicket.resource.JQueryPluginResourceReference;


/**
 * A <code>ResourceReference</code> for jQuery i18n plugin.
 * 
 * @author Leonid Bogdanov
 */
public class I18nPlugin extends JQueryPluginResourceReference {

    private static final I18nPlugin INSTANCE = new I18nPlugin();

    /**
     * Returns a single instance of jQuery i18n plugin resource reference.
     * 
     * @return the single instance
     */
    public static I18nPlugin get() {
            return INSTANCE;
    }

    private I18nPlugin() {
        super(I18nPlugin.class, "jquery.i18n.js");
    }

}
