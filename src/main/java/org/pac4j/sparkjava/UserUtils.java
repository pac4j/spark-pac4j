/*
  Copyright 2015 - 2015 pac4j organization

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package org.pac4j.sparkjava;

import org.pac4j.core.context.Pac4jConstants;
import org.pac4j.core.profile.CommonProfile;
import spark.Request;

/**
 * Helper class to manage the user profile (save to / get from session).
 *
 * @author Jerome Leleu
 * @since 1.0.0
 */
public class UserUtils {

	public static CommonProfile getProfile(final Request request) {
		return request.session().attribute(Pac4jConstants.USER_PROFILE);
	}
	
	public static void setProfile(final Request request, final CommonProfile profile) {
		request.session().attribute(Pac4jConstants.USER_PROFILE, profile);
	}
	
	public static void logout(final Request request) {
		setProfile(request, null);
	}
}
