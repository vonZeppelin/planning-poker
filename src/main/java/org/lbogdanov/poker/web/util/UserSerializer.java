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

import java.io.IOException;
import java.util.Locale;

import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.string.Strings;
import org.lbogdanov.poker.core.User;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;


/**
 * Converts a <code>User</code> instance into a string representation.
 * 
 * @author Leonid Bogdanov
 */
public class UserSerializer extends StdSerializer<User> implements IConverter<User> {

    private static final UserSerializer INSTANCE = new UserSerializer();

    /**
     * Returns a single instance of <code>UserSerializer</code>.
     * 
     * @return the <code>UserSerializer</code> instance
     */
    public static UserSerializer get() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(User value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(format(value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String convertToString(User value, Locale locale) {
        return format(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User convertToObject(String value, Locale locale) {
        throw new UnsupportedOperationException();
    }

    private static String format(User user) {
        return Strings.join(" ", user.getFirstName(), user.getLastName());
    }

    private UserSerializer() {
        super(User.class);
    }

}
