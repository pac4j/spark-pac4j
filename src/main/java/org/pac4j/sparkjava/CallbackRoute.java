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
import org.pac4j.core.client.IndirectClient;
import org.pac4j.core.config.Config;
import org.pac4j.core.context.Pac4jConstants;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.Credentials;
import org.pac4j.core.exception.RequiresHttpAction;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.core.profile.UserProfile;
import org.pac4j.core.util.CommonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;

/**
 * <p>This route handles the callback from the identity provider to finish the authentication process.</p>
 * <p>The default url after login (if none has originally be requested) can be defined via the {@link #setDefaultUrl(String)} method.</p>
 *
 * @author Jerome Leleu
 * @since 1.0.0
 */
public class CallbackRoute implements Route {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected Config config;

    protected String defaultUrl = Pac4jConstants.DEFAULT_URL_VALUE;

    public CallbackRoute(final Config config) {
        this.config = config;
    }

    public CallbackRoute(final Config config, final String defaultUrl) {
        this.config = config;
        this.defaultUrl = defaultUrl;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

        CommonHelper.assertNotNull("config", config);
        final WebContext context = new SparkWebContext(request, response, config.getSessionStore());
        CommonHelper.assertNotNull("config.httpActionAdapter", config.getHttpActionAdapter());

        final Clients clients = config.getClients();
        CommonHelper.assertNotNull("clients", clients);
        final Client client = clients.findClient(context);
        logger.debug("client: {}", client);
        CommonHelper.assertNotNull("client", client);
        CommonHelper.assertTrue(client instanceof IndirectClient, "only indirect clients are allowed on the callback url");

        final Credentials credentials;
        try {
            credentials = client.getCredentials(context);
        } catch (final RequiresHttpAction e) {
            return config.getHttpActionAdapter().adapt(e.getCode(), context);
        }
        logger.debug("credentials: {}", credentials);

        final UserProfile profile = client.getUserProfile(credentials, context);
        logger.debug("profile: {}", profile);
        saveUserProfile(context, profile);
        redirectToOriginallyRequestedUrl(context, response);

        return null;
    }

    protected void saveUserProfile(final WebContext context, final UserProfile profile) {
        final ProfileManager manager = new ProfileManager(context);
        if (profile != null) {
            manager.save(true, profile);
        }
    }

    protected void redirectToOriginallyRequestedUrl(final WebContext context, final Response response) throws IOException {
        final String requestedUrl = (String) context.getSessionAttribute(Pac4jConstants.REQUESTED_URL);
        logger.debug("requestedUrl: {}", requestedUrl);
        if (CommonHelper.isNotBlank(requestedUrl)) {
            context.setSessionAttribute(Pac4jConstants.REQUESTED_URL, null);
            response.redirect(requestedUrl);
        } else {
            response.redirect(this.defaultUrl);
        }
    }

    public String getDefaultUrl() {
        return this.defaultUrl;
    }

    public void setDefaultUrl(final String defaultUrl) {
        this.defaultUrl = defaultUrl;
    }
}
