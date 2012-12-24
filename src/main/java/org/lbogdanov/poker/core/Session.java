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

import java.util.Random;

/**
 * Represents a Planning Poker session.
 * 
 * @author Leonid Bogdanov
 */
public class Session {

	/**
	 * Generates a new alphanumeric code of a specified length which can be used
	 * to uniquely identify a session.
	 * 
	 * @param length
	 *            the desired code length
	 * @return the new code
	 */
	public static String newCode(int length) {
		StringBuilder code = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			char letter = (char) (65 + random.nextInt(26));
			letter = (random.nextBoolean()) ? letter : (char) (letter + 32);

			if (random.nextBoolean()) {
				code.append(letter);
			} else {
				code.append(random.nextInt(10));
			}
		}
		return code.toString();
	}

}
