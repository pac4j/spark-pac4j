package org.pac4j.sparkjava;

import org.pac4j.core.config.Config;
import org.pac4j.core.engine.DefaultSecurityLogic;
import org.pac4j.core.engine.SecurityLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Filter;
import spark.Request;
import spark.Response;

import static org.pac4j.core.util.CommonHelper.assertNotNull;

import static spark.Spark.halt;

/**
 * <p>This filter protects an url, based on the {@link #securityLogic}.</p>
 *
 * <p>The configuration can be provided via the following parameters: <code>config</code> (security configuration),
 * <code>clients</code> (list of clients for authentication), <code>authorizers</code> (list of authorizers),
 * <code>matchers</code> (list of matchers) and <code>multiProfile</code> (whether multiple profiles should be kept).</p>
 *
 * @author Jerome Leleu
 * @since 1.0.0
 */
public class SecurityFilter implements Filter {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private SecurityLogic<Object, SparkWebContext> securityLogic = new DefaultSecurityLogic<>();

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

        assertNotNull("securityLogic", securityLogic);
        assertNotNull("config", config);
        final SparkWebContext context = new SparkWebContext(request, response, config.getSessionStore());

        try {
            securityLogic.perform(context, this.config, (ctx, parameters) -> {
                throw new SecurityGrantedAccessException();
            }, config.getHttpActionAdapter(), this.clients, this.authorizers, this.matchers, this.multiProfile);
            logger.debug("Halt the request processing");
            // stop the processing if no success granted access exception has been raised
            halt();
        } catch (final SecurityGrantedAccessException e) {
            // ignore this exception, it meants the access is granted: continue
            logger.debug("Received SecurityGrantedAccessException -> continue");
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
