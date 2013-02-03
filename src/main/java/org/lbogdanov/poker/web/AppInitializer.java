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
import java.io.InputStream;
import java.util.Properties;

import javax.inject.Singleton;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.shiro.config.IniFactorySupport;
import org.apache.shiro.guice.web.ShiroWebModule;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.guice.GuiceWebApplicationFactory;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WicketFilter;
import org.lbogdanov.poker.core.Session;
import org.lbogdanov.poker.core.SessionService;
import org.lbogdanov.poker.core.User;
import org.lbogdanov.poker.core.UserService;
import org.lbogdanov.poker.core.impl.SessionServiceImpl;
import org.lbogdanov.poker.core.impl.UserServiceImpl;
import org.lbogdanov.poker.util.Settings;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
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
public class AppInitializer extends GuiceServletContextListener {

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
        try {
            InputStream settings = Resources.newInputStreamSupplier(Resources.getResource("settings.properties")).getInput();
            Properties props = new Properties();
            try {
                props.load(settings);
            } finally {
                settings.close();
            }
            Settings.init(Maps.fromProperties(props));
        } catch (IOException ioe) {
            Throwables.propagate(ioe);
        }
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
                ServerConfig dbConfig = new ServerConfig();
                String jndiDataSource = Settings.DB_DATA_SOURCE.asString().orNull();
                if (!Strings.isNullOrEmpty(jndiDataSource)) {
                    dbConfig.setDataSourceJndiName(jndiDataSource);
                } else {
                    DataSourceConfig dsConfig = new DataSourceConfig();
                    dsConfig.setDriver(Settings.DB_DRIVER.asString().get());
                    dsConfig.setUrl(Settings.DB_URL.asString().get());
                    dsConfig.setUsername(Settings.DB_USER.asString().orNull());
                    dsConfig.setPassword(Settings.DB_PASSWORD.asString().orNull());
                    dbConfig.setDataSourceConfig(dsConfig);
                }
                // register JPA-mapped classes
                dbConfig.addClass(Session.class);
                dbConfig.addClass(User.class);
                dbConfig.setName("PlanningPoker");
                dbConfig.setDefaultServer(true);

                bind(EbeanServer.class).toInstance(EbeanServerFactory.create(dbConfig));
                bind(SessionService.class).to(SessionServiceImpl.class);
                bind(UserService.class).to(UserServiceImpl.class);
                bind(WebApplication.class).to(PokerWebApplication.class);
                bind(WicketFilter.class).in(Singleton.class);
                String wicketConfig = (Settings.DEVELOPMENT_MODE.asBool().or(false) ? RuntimeConfigurationType.DEVELOPMENT
                                                                                    : RuntimeConfigurationType.DEPLOYMENT).toString();
                filter("/*").through(WicketFilter.class, ImmutableMap.of(WicketFilter.FILTER_MAPPING_PARAM,
                                                                         "/*",
                                                                         WebApplication.CONFIGURATION,
                                                                         wicketConfig,
                                                                         WicketFilter.APP_FACT_PARAM,
                                                                         GuiceWebApplicationFactory.class.getName(),
                                                                         "injectorContextAttribute",
                                                                         Injector.class.getName()));
            }

        };
        return Guice.createInjector(ShiroWebModule.guiceFilterModule(), shiroModule, appModule);
    }

}
