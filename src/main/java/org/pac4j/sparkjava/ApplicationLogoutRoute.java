package org.pac4j.sparkjava;

import org.pac4j.core.config.Config;
import org.pac4j.core.engine.ApplicationLogoutLogic;
import org.pac4j.core.engine.DefaultApplicationLogoutLogic;
import spark.Request;
import spark.Response;
import spark.Route;

import static org.pac4j.core.util.CommonHelper.assertNotNull;

/**
 * <p>This route handles the application logout process, based on the {@link #applicationLogoutLogic}.</p>
 *
 * <p>The configuration can be provided via the following parameters: <code>config</code> (the security configuration),
 * <code>defaultUrl</code> (default logourl url) and <code>logoutUrlPattern</code> (pattern that logout urls must match).</p>
 *
 * @author Jerome Leleu
 * @since 1.1.0
 */
public class ApplicationLogoutRoute implements Route {

    private ApplicationLogoutLogic<Object, SparkWebContext> applicationLogoutLogic = new DefaultApplicationLogoutLogic<>();

    private Config config;

    private String defaultUrl;

    private String logoutUrlPattern;

    public ApplicationLogoutRoute(final Config config) {
        this(config, null);
    }

    public ApplicationLogoutRoute(final Config config, final String defaultUrl) {
        this(config, defaultUrl, null);
    }

    public ApplicationLogoutRoute(final Config config, final String defaultUrl, final String logoutUrlPattern) {
        this.config = config;
        this.defaultUrl = defaultUrl;
        this.logoutUrlPattern = logoutUrlPattern;
    }

    @Override
    public Object handle(final Request request, final Response response) throws Exception {

        assertNotNull("applicationLogoutLogic", applicationLogoutLogic);
        assertNotNull("config", config);
        final SparkWebContext context = new SparkWebContext(request, response, config.getSessionStore());

        applicationLogoutLogic.perform(context, config, config.getHttpActionAdapter(), this.defaultUrl, this.logoutUrlPattern);
        return null;
    }

    public ApplicationLogoutLogic<Object, SparkWebContext> getApplicationLogoutLogic() {
        return applicationLogoutLogic;
    }

    public void setApplicationLogoutLogic(ApplicationLogoutLogic<Object, SparkWebContext> applicationLogoutLogic) {
        this.applicationLogoutLogic = applicationLogoutLogic;
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

    public void setLogoutUrlPattern(final String logoutUrlPattern) {
        this.logoutUrlPattern = logoutUrlPattern;
    }
}
