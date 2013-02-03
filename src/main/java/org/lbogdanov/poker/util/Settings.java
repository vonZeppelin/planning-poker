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
package org.lbogdanov.poker.util;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;


/**
 * A simple container of application settings.
 * 
 * @author Leonid Bogdanov
 */
public enum Settings {
    SESSION_CODE_LENGTH, DEVELOPMENT_MODE, DB_DATA_SOURCE, DB_DRIVER, DB_URL, DB_USER, DB_PASSWORD;

    private static final Logger LOGGER = LoggerFactory.getLogger(Settings.class);

    private static Map<Settings, String> settings = Maps.newEnumMap(Settings.class);

    private final String key;

    /**
     * Initialises settings container with values taken from a {string -> string} map.
     * 
     * @param config <code>Map</code> instance that holds settings values
     */
    public static void init(Map<String, String> config) {
        settings.clear();
        for (Settings setting : Settings.values()) {
            String value = config.get(setting.key);
            if (value == null) {
                LOGGER.warn("A value of a {} param is missing", setting);
            } else {
                settings.put(setting, value);
            }
        }
    }

    /**
     * Returns a value of a setting as <code>Integer</code>. The value is returned as an instance of
     * {@link Optional} class to indicate the fact that it can be missing.
     * 
     * @return the value of the setting
     */
    public Optional<Integer> asInt() {
        return Optional.fromNullable(Ints.tryParse(asString().or("")));
    }

    /**
     * Returns a value of a setting as <code>Long</code>. The value is returned as an instance of
     * {@link Optional} class to indicate the fact that it can be missing.
     * 
     * @return the value of the setting
     */
    public Optional<Long> asLong() {
        return Optional.fromNullable(Longs.tryParse(asString().or("")));
    }

    /**
     * Returns a value of a setting as <code>Double</code>. The value is returned as an instance of
     * {@link Optional} class to indicate the fact that it can be missing.
     * 
     * @return the value of the setting
     */
    public Optional<Double> asDouble() {
        return Optional.fromNullable(Doubles.tryParse(asString().or("")));
    }

    /**
     * Returns a value of a setting as <code>Float</code>. The value is returned as an instance of
     * {@link Optional} class to indicate the fact that it can be missing.
     * 
     * @return the value of the setting
     */
    public Optional<Float> asFloat() {
        return Optional.fromNullable(Floats.tryParse(asString().or("")));
    }

    /**
     * Returns a value of a setting as <code>Boolean</code>. The value is returned as an instance of
     * {@link Optional} class to indicate the fact that it can be missing.
     * 
     * @return the value of the setting
     */
    public Optional<Boolean> asBool() {
        Optional<String> value = asString();
        return value.isPresent() ? Optional.of(Boolean.valueOf(value.get())) : Optional.<Boolean>absent();
    }

    /**
     * Returns a value of a setting as <code>String</code>. The value is returned as an instance of
     * {@link Optional} class to indicate the fact that it can be missing.
     * 
     * @return the value of the setting
     */
    public Optional<String> asString() {
        return Optional.fromNullable(settings.get(this));
    }

    private Settings(String key) {
        this.key = key;
    }

    private Settings() {
        key = Joiner.on('.').join(Splitter.on('_').split(name().toLowerCase()));
    }

}
