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
import spark.Filter;
import spark.Request;
import spark.Response;

/**
 * Filter to protect resources.
 *
 * @author Jerome Leleu
 * @since 1.0.0
 */
public class RequiresAuthenticationFilter extends ExtraHttpActionHandler implements Filter {

	private final Clients clients;

	private final String clientName;

	public RequiresAuthenticationFilter(final Clients clients, final String clientName) {
		this.clients = clients;
		this.clientName = clientName;
	}

	@Override
	public void handle(Request request, Response response) {
        final CommonProfile profile = UserUtils.getProfile(request);
        logger.debug("profile: {}", profile);

        // null profile, not authenticated
        if (profile == null) {
            // no authentication tried -> redirect to provider
            // keep the current url
            String requestedUrl = request.url();
            String queryString = request.queryString();
            if (CommonHelper.isNotBlank(queryString)) {
                requestedUrl += "?" + queryString;
            }
            logger.debug("requestedUrl: {}", requestedUrl);
            request.session().attribute(Pac4jConstants.REQUESTED_URL, requestedUrl);
            // compute and perform the redirection
            final SparkWebContext context = new SparkWebContext(request, response);
			@SuppressWarnings("unchecked")
			Client<Credentials, CommonProfile> client = clients.findClient(this.clientName);
            try {
                client.redirect(context, true, false);
            } catch (RequiresHttpAction e) {
                handle(e);
            }
        }
	}
}
