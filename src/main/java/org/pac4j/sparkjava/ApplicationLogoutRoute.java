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

import org.pac4j.core.config.Config;
import org.pac4j.core.context.Pac4jConstants;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.core.util.CommonHelper;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.regex.Pattern;

/**
 * <p>This route handles the application logout process.</p>
 * <p>After logout, the user is redirected to the url defined by the <i>url</i> parameter. If no url is provided, a blank page is displayed. If the url does not match the pattern, the default url is used.</p>
 * <p>The default url can be defined via the {@link #setDefaultUrl(String)} method.</p>
 * <p>The logout url pattern can be defined via the {@link #setLogoutUrlPattern(String)} method.</p>
 *
 * @author Jerome Leleu
 * @since 1.1.0
 */
public class ApplicationLogoutRoute implements Route {

    protected Config config;

    protected String defaultUrl = Pac4jConstants.DEFAULT_URL_VALUE;

    protected String logoutUrlPattern = Pac4jConstants.DEFAULT_LOGOUT_URL_PATTERN_VALUE;

    public ApplicationLogoutRoute(final Config config) {
        this.config = config;
    }

    public ApplicationLogoutRoute(final Config config, final String defaultUrl) {
        this.config = config;
        this.defaultUrl = defaultUrl;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

        CommonHelper.assertNotNull("config", config);
        final WebContext context = new SparkWebContext(request, response, config.getSessionStore());
        final ProfileManager manager = new ProfileManager(context);
        manager.logout();

        final String url = context.getRequestParameter(Pac4jConstants.URL);
        if (url != null) {
            if (Pattern.matches(this.logoutUrlPattern, url)) {
                response.redirect(url);
            } else {
                response.redirect(this.defaultUrl);
            }
        }

        return null;
    }

    public String getDefaultUrl() {
        return this.defaultUrl;
    }

    public void setDefaultUrl(final String defaultUrl) {
        this.defaultUrl = defaultUrl;
    }

    public String getLogoutUrlPattern() {
        return logoutUrlPattern;
    }

    public void setLogoutUrlPattern(String logoutUrlPattern) {
        this.logoutUrlPattern = logoutUrlPattern;
    }
}
