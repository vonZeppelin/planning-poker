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

import static org.lbogdanov.poker.util.Settings.*;

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
import org.lbogdanov.poker.core.*;
import org.lbogdanov.poker.core.impl.SessionServiceImpl;
import org.lbogdanov.poker.core.impl.UserServiceImpl;
import org.lbogdanov.poker.util.Settings;
import org.lbogdanov.poker.web.oauth.CallbackUrlSetterFilter;
import org.lbogdanov.poker.web.oauth.InjectableOAuthFilter;
import org.lbogdanov.poker.web.oauth.InjectableOAuthRealm;
import org.lbogdanov.poker.web.oauth.InjectableOAuthUserFilter;
import org.scribe.up.provider.OAuthProvider;
import org.scribe.up.provider.impl.Google2Provider;
import org.scribe.up.provider.impl.Google2Provider.Google2Scope;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;
import com.google.inject.*;
import com.google.inject.name.Names;
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
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
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
            @SuppressWarnings("unchecked")
            protected void configureShiroWeb() {
                bind(String.class).annotatedWith(Names.named(InjectableOAuthFilter.FAILURE_URL_PARAM)).toInstance("/");
                // TODO simple ini-based realm for development
                bindRealm().toInstance(new IniRealm(IniFactorySupport.loadDefaultClassPathIni()));
                bindRealm().to(InjectableOAuthRealm.class).in(Singleton.class);

                addFilterChain("/" + Constants.OAUTH_CLBK_FILTER_URL, Key.get(InjectableOAuthFilter.class));
                addFilterChain("/" + Constants.OAUTH_FILTER_URL,
                               config(CallbackUrlSetterFilter.class, Constants.OAUTH_CLBK_FILTER_URL),
                               Key.get(InjectableOAuthUserFilter.class));
            }

            @Provides @Singleton
            private OAuthProvider getOAuthProvider() {
                Google2Provider provider = new Google2Provider();
                provider.setKey(GOOGLE_OAUTH_KEY.asString().get());
                provider.setSecret(GOOGLE_OAUTH_SECRET.asString().get());
                provider.setCallbackUrl("example.com"); // fake URL, will be replaced by CallbackUrlSetterFilter
                provider.setScope(Google2Scope.EMAIL_AND_PROFILE);
                return provider;
            }

        };
        Module appModule = new ServletModule() {

            @Override
            protected void configureServlets() {
                ServerConfig dbConfig = new ServerConfig();
                String jndiDataSource = DB_DATA_SOURCE.asString().orNull();
                if (Strings.isNullOrEmpty(jndiDataSource)) { // use direct JDBC connection
                    DataSourceConfig dsConfig = new DataSourceConfig();
                    dsConfig.setDriver(DB_DRIVER.asString().get());
                    dsConfig.setUrl(DB_URL.asString().get());
                    dsConfig.setUsername(DB_USER.asString().orNull());
                    dsConfig.setPassword(DB_PASSWORD.asString().orNull());
                    dbConfig.setDataSourceConfig(dsConfig);
                } else {
                    dbConfig.setDataSourceJndiName(jndiDataSource);
                }
                dbConfig.setName("PlanningPoker");
                dbConfig.setDefaultServer(true);
                dbConfig.addClass(Session.class);
                dbConfig.addClass(User.class);

                bind(EbeanServer.class).toInstance(EbeanServerFactory.create(dbConfig));
                bind(SessionService.class).to(SessionServiceImpl.class);
                bind(UserService.class).to(UserServiceImpl.class);
                bind(WebApplication.class).to(PokerWebApplication.class);
                bind(WicketFilter.class).in(Singleton.class);
                String wicketConfig = (DEVELOPMENT_MODE.asBool().or(false) ? RuntimeConfigurationType.DEVELOPMENT
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
