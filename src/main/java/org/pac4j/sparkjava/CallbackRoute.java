package org.pac4j.sparkjava;

import org.pac4j.core.config.Config;
import org.pac4j.core.context.session.JEESessionStore;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.engine.CallbackLogic;
import org.pac4j.core.engine.DefaultCallbackLogic;
import org.pac4j.core.http.adapter.HttpActionAdapter;
import org.pac4j.core.util.FindBest;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * <p>This route finishes the login process for an indirect client.</p>
 *
 * @author Jerome Leleu
 * @since 1.0.0
 */
public class CallbackRoute implements Route {

    private CallbackLogic<Object, SparkWebContext> callbackLogic;

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

        final SessionStore<SparkWebContext> bestSessionStore = FindBest.sessionStore(null, config, JEESessionStore.INSTANCE);
        final HttpActionAdapter<Object, SparkWebContext> bestAdapter = FindBest.httpActionAdapter(null, config, SparkHttpActionAdapter.INSTANCE);
        final CallbackLogic<Object, SparkWebContext> bestLogic = FindBest.callbackLogic(callbackLogic, config, DefaultCallbackLogic.INSTANCE);

        final SparkWebContext context = new SparkWebContext(request, response, bestSessionStore);
        bestLogic.perform(context, config, bestAdapter, this.defaultUrl, this.saveInSession,
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
