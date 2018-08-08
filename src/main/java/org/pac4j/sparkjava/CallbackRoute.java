package org.pac4j.sparkjava;

import org.pac4j.core.config.Config;
import org.pac4j.core.engine.CallbackLogic;
import org.pac4j.core.engine.DefaultCallbackLogic;
import spark.Request;
import spark.Response;
import spark.Route;

import static org.pac4j.core.util.CommonHelper.assertNotNull;

/**
 * <p>This route finishes the login process for an indirect client, based on the {@link #callbackLogic}.</p>
 *
 * <p>The configuration can be provided via constructors and setter methods:</p>
 * <ul>
 *     <li><code>{@link #CallbackRoute(Config)}</code> (security configuration)</li>
 *     <li><code>{@link #setDefaultUrl(String)}</code> (default url after login if none was requested)</li>
 *     <li><code>{@link #setSaveInSession(Boolean)}</code> (whether the profile should be saved into the session)</li>
 *     <li><code>{@link #setMultiProfile(Boolean)}</code> (whether multiple profiles should be kept)</li>
 *     <li><code>{@link #setRenewSession(Boolean)}</code> (whether the session must be renewed after login)</li>
 *     <li><code>{@link #setDefaultClient(String)}</code> (the default client if none is provided on the URL)</li>
 * </ul> *
 * @author Jerome Leleu
 * @since 1.0.0
 */
public class CallbackRoute implements Route {

    private CallbackLogic<Object, SparkWebContext> callbackLogic = new DefaultCallbackLogic<>();

    private Config config;

    private String defaultUrl;

    private Boolean saveInSession;

    private Boolean multiProfile;

    private Boolean renewSession;

    private String defaultClient;

    public CallbackRoute(final Config config) {
        this(config, null);
    }

    public CallbackRoute(final Config config, final String defaultUrl) {
        this(config, defaultUrl, null);
    }

    public CallbackRoute(final Config config, final String defaultUrl, final Boolean multiProfile) {
        this(config, defaultUrl, multiProfile, null);
    }

    public CallbackRoute(final Config config, final String defaultUrl, final Boolean multiProfile, final Boolean renewSession) {
        this.config = config;
        this.defaultUrl = defaultUrl;
        this.multiProfile = multiProfile;
        this.renewSession = renewSession;
    }

    @Override
    public Object handle(final Request request, final Response response) throws Exception {

        assertNotNull("callbackLogic", callbackLogic);
        assertNotNull("config", config);
        final SparkWebContext context = new SparkWebContext(request, response, config.getSessionStore());

        callbackLogic.perform(context, config, config.getHttpActionAdapter(), this.defaultUrl, this.saveInSession,
                this.multiProfile, this.renewSession, this.defaultClient);
        return null;
    }

    public CallbackLogic<Object, SparkWebContext> getCallbackLogic() {
        return callbackLogic;
    }

    public void setCallbackLogic(CallbackLogic<Object, SparkWebContext> callbackLogic) {
        this.callbackLogic = callbackLogic;
    }

    public String getDefaultUrl() {
        return this.defaultUrl;
    }

    public void setDefaultUrl(final String defaultUrl) {
        this.defaultUrl = defaultUrl;
    }

    public Boolean getMultiProfile() {
        return multiProfile;
    }

    public void setMultiProfile(final Boolean multiProfile) {
        this.multiProfile = multiProfile;
    }

    public Boolean getRenewSession() {
        return renewSession;
    }

    public void setRenewSession(final Boolean renewSession) {
        this.renewSession = renewSession;
    }

    public Boolean getSaveInSession() {
        return saveInSession;
    }

    public void setSaveInSession(final Boolean saveInSession) {
        this.saveInSession = saveInSession;
    }

    public String getDefaultClient() {
        return defaultClient;
    }

    public void setDefaultClient(final String defaultClient) {
        this.defaultClient = defaultClient;
    }
}
