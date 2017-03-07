package org.pac4j.sparkjava;

import org.pac4j.core.config.Config;
import org.pac4j.core.engine.DefaultLogoutLogic;
import org.pac4j.core.engine.LogoutLogic;
import spark.Request;
import spark.Response;
import spark.Route;

import static org.pac4j.core.util.CommonHelper.assertNotNull;

/**
 * <p>This route handles the (application + identity provider) logout process, based on the {@link #logoutLogic}.</p>
 *
 * <p>The configuration can be provided via the following parameters: <code>config</code> (the security configuration),
 * <code>defaultUrl</code> (default logourl url), <code>logoutUrlPattern</code> (pattern that logout urls must match),
 * <code>localLogout</code> (whether the application logout must be performed),
 * <code>destroySession</code> (whether we must destroy the web session during the local logout)
 * and <code>centralLogout</code> (whether the centralLogout must be performed).</p>
 *
 * @author Jerome Leleu
 * @since 1.1.0
 */
public class LogoutRoute implements Route {

    private LogoutLogic<Object, SparkWebContext> logoutLogic = new DefaultLogoutLogic<>();

    private Config config;

    private String defaultUrl;

    private String logoutUrlPattern;

    private Boolean localLogout;

    private Boolean destroySession;

    private Boolean centralLogout;

    public LogoutRoute(final Config config) {
        this(config, null);
    }

    public LogoutRoute(final Config config, final String defaultUrl) {
        this(config, defaultUrl, null);
    }

    public LogoutRoute(final Config config, final String defaultUrl, final String logoutUrlPattern) {
        this.config = config;
        this.defaultUrl = defaultUrl;
        this.logoutUrlPattern = logoutUrlPattern;
    }

    @Override
    public Object handle(final Request request, final Response response) throws Exception {

        assertNotNull("logoutLogic", logoutLogic);
        assertNotNull("config", config);
        final SparkWebContext context = new SparkWebContext(request, response, config.getSessionStore());

        logoutLogic.perform(context, config, config.getHttpActionAdapter(), this.defaultUrl, this.logoutUrlPattern, this.localLogout, this.destroySession, this.centralLogout);
        return null;
    }

    public LogoutLogic<Object, SparkWebContext> getLogoutLogic() {
        return logoutLogic;
    }

    public void setLogoutLogic(final LogoutLogic<Object, SparkWebContext> logoutLogic) {
        this.logoutLogic = logoutLogic;
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

    public Boolean getLocalLogout() {
        return localLogout;
    }

    public void setLocalLogout(final Boolean localLogout) {
        this.localLogout = localLogout;
    }

    public Boolean getDestroySession() {
        return destroySession;
    }

    public void setDestroySession(final Boolean destroySession) {
        this.destroySession = destroySession;
    }

    public Boolean getCentralLogout() {
        return centralLogout;
    }

    public void setCentralLogout(final Boolean centralLogout) {
        this.centralLogout = centralLogout;
    }
}
