package org.pac4j.sparkjava;

import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.exception.http.HttpAction;
import org.pac4j.core.exception.http.WithContentAction;
import org.pac4j.core.exception.http.WithLocationAction;
import org.pac4j.core.http.adapter.HttpActionAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Service;
import spark.Spark;

/**
 * Default HTTP action adapter.
 */
public class SparkHttpActionAdapter implements HttpActionAdapter {

    /** Default singleton instance. */
    public static final SparkHttpActionAdapter INSTANCE = new SparkHttpActionAdapter();

    /** Logger. */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /** Optional Spark Service reference. */
    protected Service service;

    /** Default constructor. */
    public SparkHttpActionAdapter() {}

    /**
     * Construct an adapter bound to a specific Spark Service.
     * @param service the Spark service
     */
    public SparkHttpActionAdapter(Service service) {
        this.service = service;
    }

    @Override
    public Object adapt(final HttpAction action, final WebContext context) {
        if (action != null) {
            final int code = action.getCode();
            logger.debug("requires HTTP action: {}", code);

            if (action instanceof WithContentAction) {
                stop(code, ((WithContentAction) action).getContent());
            } else if (action instanceof WithLocationAction) {
                ((SparkWebContext) context).getSparkResponse().redirect(((WithLocationAction) action).getLocation(), code);
            } else {
                stop(code, "");
            }
        } else {
            throw new TechnicalException("No action provided");
        }
        return null;
    }

    /**
     * Immediately stop the request.
     * @param code the HTTP action status code
     * @param body the HTTP action response body
     */
    protected void stop(int code, String body) {
        if (service == null) {
            Spark.halt(code, body);
        } else {
            service.halt(code, body);
        }
    }

}
