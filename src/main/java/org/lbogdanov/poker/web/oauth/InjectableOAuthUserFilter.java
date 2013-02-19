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

import javax.inject.Inject;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.scribe.up.provider.OAuthProvider;

import io.buji.oauth.filter.OAuthUserFilter;


/**
 * A subclass of <code>InjectableOAuthUserFilter</code> merely to add injection support.
 * 
 * @author Leonid Bogdanov
 */
public class InjectableOAuthUserFilter extends OAuthUserFilter {

    /**
     * {@inheritDoc}
     */
    @Override @Inject
    public void setProvider(OAuthProvider provider) {
        super.setProvider(provider);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        redirectToLogin(request, response);
        return false;
    }

}
