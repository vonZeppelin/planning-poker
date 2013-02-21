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
package org.lbogdanov.poker.web.oauth;

import io.buji.oauth.OAuthFilter;
import io.buji.oauth.OAuthRealm;

import javax.inject.Inject;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.scribe.up.provider.BaseOAuthProvider;
import org.scribe.up.provider.OAuthProvider;
import org.scribe.up.provider.ProvidersDefinition;


/**
 * A helper filter to set a callback URL for a <code>OAuthProvider</code>. The URL will be calculated on the first
 * request to the app and set only once.
 * 
 * @author Leonid Bogdanov
 */
public class CallbackUrlSetterFilter extends PathMatchingFilter {

    @Inject
    private OAuthProvider oAuthProvider;
    @Inject
    private OAuthRealm oAuthRealm;
    @Inject
    private OAuthFilter oAuthFilter;

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        synchronized (this) {
            if (isEnabled()) {
                HttpServletRequest httpRequest = WebUtils.toHttp(request);
                String scheme = httpRequest.getScheme();
                int port = httpRequest.getServerPort();
                StringBuilder url = new StringBuilder(scheme).append("://").append(httpRequest.getServerName());
                if ("http".equals(scheme) && port != 80 || "https".equals(scheme) && port != 443) {
                    url.append(':').append(port);
                }
                url.append(httpRequest.getContextPath()).append('/').append(((String[]) mappedValue)[0]);
                ((BaseOAuthProvider) oAuthProvider).setCallbackUrl(url.toString());
                // reinitialize OAuthRealm and OAuthFilter instances after the URL was changed
                ProvidersDefinition definition = new ProvidersDefinition(oAuthProvider);
                oAuthFilter.setProvidersDefinition(definition);
                oAuthRealm.setProvidersDefinition(definition);
                setEnabled(false); // disable further filter executions
            }
        }
        return true;
    }

}
