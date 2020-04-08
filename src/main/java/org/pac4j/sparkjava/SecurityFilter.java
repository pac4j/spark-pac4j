package org.pac4j.sparkjava;

import org.pac4j.core.config.Config;
import org.pac4j.core.context.session.JEESessionStore;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.engine.DefaultSecurityLogic;
import org.pac4j.core.engine.SecurityLogic;
import org.pac4j.core.http.adapter.HttpActionAdapter;
import org.pac4j.core.util.FindBest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Filter;
import spark.Request;
import spark.Response;

import static spark.Spark.halt;

/**
 * <p>This filter protects an url.</p>
 *
 * @author Jerome Leleu
 * @since 1.0.0
 */
public class SecurityFilter implements Filter {

    private static final String SECURITY_GRANTED_ACCESS = "SECURITY_GRANTED_ACCESS";

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private SecurityLogic<Object, SparkWebContext> securityLogic;

    private Config config;

    private String clients;

    private String authorizers;

    private String matchers;

    private Boolean multiProfile;

    public SecurityFilter(final Config config, final String clients) {
        this(config, clients, null, null);
    }

    public SecurityFilter(final Config config, final String clients, final String authorizers) {
        this(config, clients, authorizers, null);
    }

    public SecurityFilter(final Config config, final String clients, final String authorizers, final String matchers) {
        this(config, clients, authorizers, matchers, null);
    }

    public SecurityFilter(final Config config, final String clients, final String authorizers, final String matchers, final Boolean multiProfile) {
        this.config = config;
        this.clients = clients;
        this.authorizers = authorizers;
        this.matchers = matchers;
        this.multiProfile = multiProfile;
    }

    @Override
    public void handle(final Request request, final Response response) {

        final SessionStore<SparkWebContext> bestSessionStore = FindBest.sessionStore(null, config, JEESessionStore.INSTANCE);
        final HttpActionAdapter<Object, SparkWebContext> bestAdapter = FindBest.httpActionAdapter(null, config, SparkHttpActionAdapter.INSTANCE);
        final SecurityLogic<Object, SparkWebContext> bestLogic = FindBest.securityLogic(securityLogic, config, DefaultSecurityLogic.INSTANCE);

        final SparkWebContext context = new SparkWebContext(request, response, bestSessionStore);
        final Object result = bestLogic.perform(context, this.config, (ctx, profiles, parameters) -> SECURITY_GRANTED_ACCESS,
                bestAdapter, this.clients, this.authorizers, this.matchers, this.multiProfile);
        if (result == SECURITY_GRANTED_ACCESS) {
            // It means that the access is granted: continue
            logger.debug("Received SECURITY_GRANTED_ACCESS -> continue");
        } else {
            logger.debug("Halt the request processing");
            // stop the processing if no SECURITY_GRANTED_ACCESS has been received
            halt();
        }
    }

    public SecurityLogic<Object, SparkWebContext> getSecurityLogic() {
        return securityLogic;
    }

    public void setSecurityLogic(final SecurityLogic<Object, SparkWebContext> securityLogic) {
        this.securityLogic = securityLogic;
    }

    public String getAuthorizers() {
        return authorizers;
    }

    public void setAuthorizers(final String authorizers) {
        this.authorizers = authorizers;
    }

    public String getMatchers() {
        return matchers;
    }

    public void setMatchers(final String matchers) {
        this.matchers = matchers;
    }

    public Boolean getMultiProfile() {
        return multiProfile;
    }

    public void setMultiProfile(final Boolean multiProfile) {
        this.multiProfile = multiProfile;
    }
}
