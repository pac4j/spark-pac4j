package org.pac4j.sparkjava;

import org.pac4j.core.adapter.FrameworkAdapter;
import org.pac4j.core.config.Config;
import org.pac4j.core.engine.SecurityGrantedAccessAdapter;
import org.pac4j.core.engine.SecurityLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Filter;
import spark.Request;
import spark.Response;

import static spark.Spark.halt;

/**
 * Filter protecting an URL (pac4j v6 style).
 */
public class SecurityFilter implements Filter {

    /**
     * Logger for this filter.
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Optional custom security logic.
     */
    private SecurityLogic securityLogic;

    /**
     * The pac4j configuration.
     */
    private Config config;

    /**
     * The clients to use for authentication.
     */
    private String clients;

    /**
     * The authorizers to apply.
     */
    private String authorizers;

    /**
     * The matchers to evaluate before security.
     */
    private String matchers;

    /**
     * Create a SecurityFilter.
     * @param config the pac4j configuration
     * @param clients the clients to use
     */
    public SecurityFilter(final Config config, final String clients) {
        this(config, clients, null, null);
    }

    /**
     * Create a SecurityFilter with authorizers.
     * @param config the pac4j configuration
     * @param clients the clients to use
     * @param authorizers the authorizers to apply
     */
    public SecurityFilter(final Config config, final String clients, final String authorizers) {
        this(config, clients, authorizers, null);
    }

    /**
     * Create a SecurityFilter with authorizers and matchers.
     * @param config the pac4j configuration
     * @param clients the clients to use
     * @param authorizers the authorizers to apply
     * @param matchers the matchers to evaluate
     */
    public SecurityFilter(final Config config, final String clients, final String authorizers, final String matchers) {
        this.config = config;
        this.clients = clients;
        this.authorizers = authorizers;
        this.matchers = matchers;
    }

    @Override
    public void handle(final Request request, final Response response) {

        FrameworkAdapter.INSTANCE.applyDefaultSettingsIfUndefined(config);

        final SecurityLogic logic = (securityLogic != null) ? securityLogic : config.getSecurityLogic();

        final SecurityGrantedAccessAdapter granted = (context, store, profiles) -> null; // continue filter chain

        final Object result = logic.perform(
                config,
                granted,
                this.clients,
                this.authorizers,
                this.matchers,
                new SparkFrameworkParameters(request, response)
        );

        if (result == null) {
            logger.debug("Access granted -> continue");
        } else {
            logger.debug("Halt the request processing");
            halt();
        }
    }

    /**
     * Get the custom security logic (if any).
     * @return the security logic
     */
    public SecurityLogic getSecurityLogic() {
        return securityLogic;
    }

    /**
     * Set a custom security logic.
     * @param securityLogic the security logic
     */
    public void setSecurityLogic(final SecurityLogic securityLogic) {
        this.securityLogic = securityLogic;
    }

    /**
     * Get the authorizers.
     * @return the authorizers
     */
    public String getAuthorizers() {
        return authorizers;
    }

    /**
     * Set the authorizers.
     * @param authorizers the authorizers
     */
    public void setAuthorizers(final String authorizers) {
        this.authorizers = authorizers;
    }

    /**
     * Get the matchers.
     * @return the matchers
     */
    public String getMatchers() {
        return matchers;
    }

    /**
     * Set the matchers.
     * @param matchers the matchers
     */
    public void setMatchers(final String matchers) {
        this.matchers = matchers;
    }
}
