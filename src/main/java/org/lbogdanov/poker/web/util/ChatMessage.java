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

import org.lbogdanov.poker.core.User;

import com.fasterxml.jackson.annotation.JsonTypeName;


/**
 * Represents a chat message.
 * 
 * @author Leonid Bogdanov
 */
@JsonTypeName("chatMsg")
public final class ChatMessage extends Message<String> {

    public final User author;

    /**
     * Creates a new instance of the <code>ChatMessage</code> class.
     * 
     * @param origin the message origin identifier
     * @param author the author of the message
     * @param message the message text
     */
    public ChatMessage(Object origin, User author, String message) {
        super(origin, message);
        this.author = author;
    }

}
