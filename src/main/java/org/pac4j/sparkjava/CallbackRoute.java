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

import org.pac4j.core.client.Client;
import org.pac4j.core.client.Clients;
import org.pac4j.core.context.Pac4jConstants;
import org.pac4j.core.credentials.Credentials;
import org.pac4j.core.exception.RequiresHttpAction;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.util.CommonHelper;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Route to receive callback calls from identity providers and finish the authentication process.
 *
 * @author Jerome Leleu
 * @since 1.0.0
 */
public class CallbackRoute extends ExtraHttpActionHandler implements Route {
	
	private final Clients clients;

	private String defaultUrl = "/";
	
	public CallbackRoute(final Clients clients) {
		this.clients = clients;
	}

	@Override
	public Object handle(Request request, Response response)  throws Exception {
		final SparkWebContext context = new SparkWebContext(request, response);
        @SuppressWarnings("rawtypes")
		final Client client = clients.findClient(context);
        logger.debug("client : {}", client);
        
        final Credentials credentials;
        try {
            credentials = client.getCredentials(context);
        } catch (final RequiresHttpAction e) {
            handle(e);
            return null;
        }
        logger.debug("credentials : {}", credentials);
        
        // get user profile
        @SuppressWarnings("unchecked")
		final CommonProfile profile = (CommonProfile) client.getUserProfile(credentials, context);
        logger.debug("profile : {}", profile);
        
        if (profile != null) {
            // only save profile when it's not null
            UserUtils.setProfile(request, profile);
        }
        
        final String requestedUrl = (String) request.session().attribute(Pac4jConstants.REQUESTED_URL);
        logger.debug("requestedUrl : {}", requestedUrl);
        if (CommonHelper.isNotBlank(requestedUrl)) {
            response.redirect(requestedUrl);
        } else {
            response.redirect(this.defaultUrl);
        }
        
        return null;
	}

	public String getDefaultUrl() {
		return defaultUrl;
	}

	public void setDefaultUrl(String defaultUrl) {
		this.defaultUrl = defaultUrl;
	}
}
