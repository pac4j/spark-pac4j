package org.pac4j.sparkjava;

import org.pac4j.core.config.Config;
import org.pac4j.core.engine.CallbackLogic;
import org.pac4j.core.engine.J2ERenewSessionCallbackLogic;
import spark.Request;
import spark.Response;
import spark.Route;

import static org.pac4j.core.util.CommonHelper.assertNotNull;

/**
 * <p>This route finishes the login process for an indirect client, based on the {@link #callbackLogic}.</p>
 *
 * <p>The configuration can be provided via the following parameters: <code>config</code> (security configuration),
 * <code>defaultUrl</code> (default url after login if none was requested), <code>multiProfile</code> (whether multiple profiles should be kept)
 * and <code>renewSession</code> (whether the session must be renewed after login).</p>
 *
 * @author Jerome Leleu
 * @since 1.0.0
 */
public class CallbackRoute implements Route {

    private CallbackLogic<Object, SparkWebContext> callbackLogic = new J2ERenewSessionCallbackLogic<>();

    private Config config;

    private String defaultUrl;

    private Boolean multiProfile;

    private Boolean renewSession;

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

        callbackLogic.perform(context, config, config.getHttpActionAdapter(), this.defaultUrl, this.multiProfile, this.renewSession);
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
}
