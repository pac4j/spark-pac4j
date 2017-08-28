package org.pac4j.sparkjava;

import org.pac4j.core.context.HttpConstants;
import org.pac4j.core.http.HttpActionAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Service;
import spark.Spark;

/**
 * Default HTTP action adapter.
 *
 * @author Jerome Leleu
 * @author Marco Günther
 * @since 1.1.0
 */
public class DefaultHttpActionAdapter implements HttpActionAdapter<Object, SparkWebContext> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    protected Service service;

    public DefaultHttpActionAdapter() {
		super();
	}

	public DefaultHttpActionAdapter(Service service) {
		this.service = service;
	}

	@Override
    public Object adapt(int code, SparkWebContext context) {
        logger.debug("requires HTTP action: {}", code);
        if (code == HttpConstants.UNAUTHORIZED) {
            halt(HttpConstants.UNAUTHORIZED, "authentication required", context);
        } else if (code == HttpConstants.FORBIDDEN) {
            halt(HttpConstants.FORBIDDEN, "forbidden", context);
        } else if (code == HttpConstants.OK) {
            halt(HttpConstants.OK, context.getSparkResponse().body(), context);
        } else if (code == HttpConstants.TEMP_REDIRECT) {
            context.getSparkResponse().redirect(context.getLocation());
        }
        return null;
    }

	protected void halt(int status, String body, SparkWebContext context) {
		context.setResponseHeader("content-type", "text/plain");
		if (service == null) {
			Spark.halt(status, body);
		} else {
			service.halt(status, body);
		}
	}

}
