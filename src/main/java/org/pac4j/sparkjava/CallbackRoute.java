package org.pac4j.sparkjava;

import org.pac4j.core.config.Config;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.engine.CallbackLogic;
import org.pac4j.core.engine.DefaultCallbackLogic;
import org.pac4j.core.http.adapter.HttpActionAdapter;
import org.pac4j.core.util.FindBest;
import org.pac4j.jee.context.session.JEESessionStore;
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

    private CallbackLogic callbackLogic;

    private Config config;

    private String defaultUrl;

    private Boolean renewSession;

    private String defaultClient;

    public CallbackRoute(final Config config) {
        this(config, null);
    }

    public CallbackRoute(final Config config, final String defaultUrl) {
        this(config, defaultUrl, null);
    }

    public CallbackRoute(final Config config, final String defaultUrl, final Boolean renewSession) {
        this.config = config;
        this.defaultUrl = defaultUrl;
        this.renewSession = renewSession;
    }

    @Override
    public Object handle(final Request request, final Response response) throws Exception {

        final SessionStore bestSessionStore = FindBest.sessionStore(null, config, JEESessionStore.INSTANCE);
        final HttpActionAdapter bestAdapter = FindBest.httpActionAdapter(null, config, SparkHttpActionAdapter.INSTANCE);
        final CallbackLogic bestLogic = FindBest.callbackLogic(callbackLogic, config, DefaultCallbackLogic.INSTANCE);

        final WebContext context = FindBest.webContextFactory(null, config, SparkContextFactory.INSTANCE).newContext(request, response);
        bestLogic.perform(context, bestSessionStore, config, bestAdapter, this.defaultUrl, this.renewSession, this.defaultClient);

        return null;
    }

    public CallbackLogic getCallbackLogic() {
        return callbackLogic;
    }

    public void setCallbackLogic(final CallbackLogic callbackLogic) {
        this.callbackLogic = callbackLogic;
    }

    public String getDefaultUrl() {
        return this.defaultUrl;
    }

    public void setDefaultUrl(final String defaultUrl) {
        this.defaultUrl = defaultUrl;
    }

    public Boolean getRenewSession() {
        return renewSession;
    }

    public void setRenewSession(final Boolean renewSession) {
        this.renewSession = renewSession;
    }

    public String getDefaultClient() {
        return defaultClient;
    }

    public void setDefaultClient(final String defaultClient) {
        this.defaultClient = defaultClient;
    }
}
