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
package org.lbogdanov.poker.core;


/**
 * A utility class that holds various constant values.
 * 
 * @author Leonid Bogdanov
 */
public final class Constants {

    public static final int SESSION_CODE_DEFAULT_LENGTH = 10;
    public static final int SESSION_CODE_MAX_LENGTH = 32;
    public static final int SESSION_NAME_MAX_LENGTH = 128;
    public static final int SESSION_DESCRIPTION_MAX_LENGTH = 4096;
    public static final int SESSION_ESTIMATES_MAX_LENGTH = 1024;
    public static final int USER_FIRST_NAME_MAX_LENGTH = 128;
    public static final int USER_LAST_NAME_MAX_LENGTH = 128;
    public static final int USER_EMAIL_MAX_LENGTH = 254;
    public static final int USER_EXTERNAL_ID_MAX_LENGTH = 64;

    public static final String OAUTH_FILTER_URL = "oauth";
    public static final String OAUTH_CLBK_FILTER_URL = "oauth-clbk";

    private Constants() {}

}
