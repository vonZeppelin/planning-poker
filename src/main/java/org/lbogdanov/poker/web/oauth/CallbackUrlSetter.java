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
import javax.inject.Singleton;

import org.scribe.up.provider.BaseOAuthProvider;
import org.scribe.up.provider.OAuthProvider;
import org.scribe.up.provider.ProvidersDefinition;


/**
 * A helper class to be able to lazily set actual callback URL for a <code>OAuthProvider</code>.
 * 
 * @author Leonid Bogdanov
 */
@Singleton
public class CallbackUrlSetter {

    @Inject
    private OAuthFilter oAuthFilter;
    @Inject
    private OAuthRealm oAuthRealm;
    @Inject
    private OAuthProvider oAuthProvider;
    private boolean isUrlSet;

    /**
     * Sets a callback URL for a <code>OAuthProvider</code> instance and reinitializes <code>OAuthRealm</code> and
     * <code>OAuthFilter</code> instances after the URL was changed.
     * 
     * @param url the actual callback URL
     */
    public void setCallbackUrl(String url) {
        ((BaseOAuthProvider) oAuthProvider).setCallbackUrl(url);
        ProvidersDefinition definition = new ProvidersDefinition(oAuthProvider);
        oAuthFilter.setProvidersDefinition(definition);
        oAuthRealm.setProvidersDefinition(definition);
        isUrlSet = true;
    }

    /**
     * Returns <b>true</b> if a callback URL was set once.
     * 
     * @return <b>true</b> if the callback URL was set once, otherwise <b>false</b>
     */
    public boolean isCallbackUrlSet() {
        return isUrlSet;
    }

}
