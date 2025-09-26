package org.pac4j.sparkjava;

import org.pac4j.core.adapter.FrameworkAdapter;
import org.pac4j.core.config.Config;
import org.pac4j.core.engine.LogoutLogic;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Route to handle (application + identity provider) logout (pac4j v6 style).
 */
public class LogoutRoute implements Route {

    /**
     * The logout logic to execute (overrides the one from the config if set).
     */
    private LogoutLogic logoutLogic;

    /**
     * The pac4j security configuration.
     */
    private Config config;

    /**
     * Default URL to redirect to after logout.
     */
    private String defaultUrl;

    /**
     * A regex pattern for allowed post-logout redirection URLs.
     */
    private String logoutUrlPattern;

    /**
     * Whether to perform local logout.
     */
    private Boolean localLogout;

    /**
     * Whether to destroy the web session.
     */
    private Boolean destroySession;

    /**
     * Whether to perform central logout at the identity provider.
     */
    private Boolean centralLogout;

    /**
     * Create a LogoutRoute with the provided config.
     * @param config the pac4j configuration
     */
    public LogoutRoute(final Config config) {
        this(config, null);
    }

    /**
     * Create a LogoutRoute with a default URL.
     * @param config the pac4j configuration
     * @param defaultUrl the default URL after logout
     */
    public LogoutRoute(final Config config, final String defaultUrl) {
        this(config, defaultUrl, null);
    }

    /**
     * Create a LogoutRoute with default URL and URL pattern.
     * @param config the pac4j configuration
     * @param defaultUrl the default URL after logout
     * @param logoutUrlPattern the allowed logout URL pattern
     */
    public LogoutRoute(final Config config, final String defaultUrl, final String logoutUrlPattern) {
        this.config = config;
        this.defaultUrl = defaultUrl;
        this.logoutUrlPattern = logoutUrlPattern;
    }

    @Override
    public Object handle(final Request request, final Response response) {

        FrameworkAdapter.INSTANCE.applyDefaultSettingsIfUndefined(config);

        config.getLogoutLogic().perform(
                config,
                this.defaultUrl,
                this.logoutUrlPattern,
                this.localLogout,
                this.destroySession,
                this.centralLogout,
                new SparkFrameworkParameters(request, response)
        );

        return null;
    }

    /**
     * Get the logout logic used by this route.
     * @return the logout logic
     */
    public LogoutLogic getLogoutLogic() {
        return logoutLogic;
    }

    /**
     * Set a specific logout logic (optional).
     * @param logoutLogic the logout logic to use
     */
    public void setLogoutLogic(final LogoutLogic logoutLogic) {
        this.logoutLogic = logoutLogic;
    }

    /**
     * Get the default redirection URL after logout.
     * @return the default URL
     */
    public String getDefaultUrl() {
        return this.defaultUrl;
    }

    /**
     * Set the default redirection URL after logout.
     * @param defaultUrl the default URL
     */
    public void setDefaultUrl(final String defaultUrl) {
        this.defaultUrl = defaultUrl;
    }

    /**
     * Get the allowed logout URL pattern.
     * @return the pattern
     */
    public String getLogoutUrlPattern() {
        return logoutUrlPattern;
    }

    /**
     * Set the allowed logout URL pattern.
     * @param logoutUrlPattern the pattern
     */
    public void setLogoutUrlPattern(final String logoutUrlPattern) {
        this.logoutUrlPattern = logoutUrlPattern;
    }

    /**
     * Whether to perform local logout.
     * @return true to perform local logout
     */
    public Boolean getLocalLogout() {
        return localLogout;
    }

    /**
     * Set whether to perform local logout.
     * @param localLogout local logout flag
     */
    public void setLocalLogout(final Boolean localLogout) {
        this.localLogout = localLogout;
    }

    /**
     * Whether to destroy the HTTP session.
     * @return true to destroy the session
     */
    public Boolean getDestroySession() {
        return destroySession;
    }

    /**
     * Set whether to destroy the HTTP session.
     * @param destroySession destroy session flag
     */
    public void setDestroySession(final Boolean destroySession) {
        this.destroySession = destroySession;
    }

    /**
     * Whether to perform central logout at the identity provider.
     * @return true for central logout
     */
    public Boolean getCentralLogout() {
        return centralLogout;
    }

    /**
     * Set whether to perform central logout at the identity provider.
     * @param centralLogout central logout flag
     */
    public void setCentralLogout(final Boolean centralLogout) {
        this.centralLogout = centralLogout;
    }
}
