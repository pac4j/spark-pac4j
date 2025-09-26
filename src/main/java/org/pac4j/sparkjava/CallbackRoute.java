package org.pac4j.sparkjava;

import org.pac4j.core.adapter.FrameworkAdapter;
import org.pac4j.core.config.Config;
import org.pac4j.core.engine.CallbackLogic;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Route to finish the login process for an indirect client (pac4j v6 style).
 */
public class CallbackRoute implements Route {

    /**
     * The callback logic to execute (overrides the one from the config if set).
     */
    private CallbackLogic callbackLogic;

    /**
     * The pac4j security configuration.
     */
    private Config config;

    /**
     * Default URL to redirect to after successful authentication.
     */
    private String defaultUrl;

    /**
     * Whether the user session should be renewed after login.
     */
    private Boolean renewSession;

    /**
     * Default client to use for the callback when none is provided.
     */
    private String defaultClient;

    /**
     * Create a CallbackRoute with the provided config.
     * @param config the pac4j configuration
     */
    public CallbackRoute(final Config config) {
        this(config, null);
    }

    /**
     * Create a CallbackRoute with a default redirection URL.
     * @param config the pac4j configuration
     * @param defaultUrl the default URL after login
     */
    public CallbackRoute(final Config config, final String defaultUrl) {
        this(config, defaultUrl, null);
    }

    /**
     * Create a CallbackRoute with default URL and session renewal flag.
     * @param config the pac4j configuration
     * @param defaultUrl the default URL after login
     * @param renewSession whether to renew the session
     */
    public CallbackRoute(final Config config, final String defaultUrl, final Boolean renewSession) {
        this.config = config;
        this.defaultUrl = defaultUrl;
        this.renewSession = renewSession;
    }

    @Override
    public Object handle(final Request request, final Response response) {

        FrameworkAdapter.INSTANCE.applyDefaultSettingsIfUndefined(config);

        config.getCallbackLogic().perform(
                config,
                this.defaultUrl,
                this.renewSession,
                this.defaultClient,
                new SparkFrameworkParameters(request, response)
        );

        return null;
    }

    /**
     * Get the callback logic used by this route.
     * @return the callback logic
     */
    public CallbackLogic getCallbackLogic() {
        return callbackLogic;
    }

    /**
     * Set a specific callback logic (optional).
     * @param callbackLogic the callback logic to use
     */
    public void setCallbackLogic(final CallbackLogic callbackLogic) {
        this.callbackLogic = callbackLogic;
    }

    /**
     * Get the default redirection URL.
     * @return the default URL
     */
    public String getDefaultUrl() {
        return this.defaultUrl;
    }

    /**
     * Set the default redirection URL.
     * @param defaultUrl the default URL
     */
    public void setDefaultUrl(final String defaultUrl) {
        this.defaultUrl = defaultUrl;
    }

    /**
     * Indicates whether the session is renewed after login.
     * @return true if renewed, otherwise false
     */
    public Boolean getRenewSession() {
        return renewSession;
    }

    /**
     * Set whether the session should be renewed after login.
     * @param renewSession renew session flag
     */
    public void setRenewSession(final Boolean renewSession) {
        this.renewSession = renewSession;
    }

    /**
     * Get the default client name.
     * @return the default client
     */
    public String getDefaultClient() {
        return defaultClient;
    }

    /**
     * Set the default client name.
     * @param defaultClient the client name
     */
    public void setDefaultClient(final String defaultClient) {
        this.defaultClient = defaultClient;
    }
}
