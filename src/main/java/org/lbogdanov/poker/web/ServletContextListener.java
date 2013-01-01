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
package org.lbogdanov.poker.web;

import java.io.IOException;

import javax.inject.Singleton;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.shiro.config.IniFactorySupport;
import org.apache.shiro.guice.web.ShiroWebModule;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.wicket.guice.GuiceWebApplicationFactory;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WicketFilter;
import org.lbogdanov.poker.core.SessionService;
import org.lbogdanov.poker.core.impl.SessionServiceImpl;
import org.lbogdanov.poker.util.Settings;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;


/**
 * Configures Guice modules for Planning Poker web app.
 * 
 * @author Leonid Bogdanov
 */
public class ServletContextListener extends GuiceServletContextListener {

    private ServletContext servletContext;

    /**
     * {@inheritDoc}
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        servletContext = servletContextEvent.getServletContext();
        super.contextInitialized(servletContextEvent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Injector getInjector() {
        Module shiroModule = new ShiroWebModule(servletContext) {

            @Override
            protected void configureShiroWeb() {
                // TODO simple ini-based realm for development
                bindRealm().toInstance(new IniRealm(IniFactorySupport.loadDefaultClassPathIni()));
            }

        };
        Module appModule = new ServletModule() {

            @Override
            protected void configureServlets() {
                try {
                    String config = Resources.toString(Resources.getResource("settings.properties"), Charsets.ISO_8859_1);
                    Splitter.MapSplitter splitter = Splitter.onPattern("\r?\n").omitEmptyStrings().withKeyValueSeparator('=');
                    Settings.init(splitter.split(config));
                } catch (IOException ioe) {
                    addError(ioe);
                }
                bind(SessionService.class).to(SessionServiceImpl.class);
                bind(WebApplication.class).to(PokerWebApplication.class);
                bind(WicketFilter.class).in(Singleton.class);
                filter("/*").through(WicketFilter.class, ImmutableMap.of(WicketFilter.APP_FACT_PARAM,
                                                                         GuiceWebApplicationFactory.class.getName(),
                                                                         WicketFilter.FILTER_MAPPING_PARAM,
                                                                         "/*",
                                                                         "injectorContextAttribute",
                                                                         Injector.class.getName()));
            }

        };
        return Guice.createInjector(ShiroWebModule.guiceFilterModule(), shiroModule, appModule);
    }

}
