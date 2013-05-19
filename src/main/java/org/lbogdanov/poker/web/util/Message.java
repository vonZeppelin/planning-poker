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
package org.lbogdanov.poker.web.util;

import org.apache.wicket.util.io.IClusterable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;


/**
 * A base class for all messages sent via Atmosphere framework.
 * 
 * @author Leonid Bogdanov
 */
@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "type")
public abstract class Message<T> implements IClusterable {

    @JsonIgnore
    public final Object origin;
    public final T message;

    /**
     * Creates a new instance of the <code>Message</code> class.
     * 
     * @param origin identifies a "scope" where the message was originally created, e.g., an HTTP session
     * @param message the message payload
     */
    public Message(Object origin, T message) {
        this.origin = origin;
        this.message = message;
    }

}
