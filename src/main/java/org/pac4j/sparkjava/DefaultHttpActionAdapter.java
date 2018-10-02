package org.pac4j.sparkjava;

import org.pac4j.core.context.HttpConstants;
import org.pac4j.core.http.adapter.HttpActionAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Service;
import spark.Spark;

/**
 * Default HTTP action adapter.
 *
 * @author Jerome Leleu
 * @author Marco Guenther
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
            stop(HttpConstants.UNAUTHORIZED, "authentication required");
        } else if (code == HttpConstants.FORBIDDEN) {
            stop(HttpConstants.FORBIDDEN, "forbidden");
        } else if (code == HttpConstants.OK) {
			stop(HttpConstants.OK, context.getSparkResponse().body());
		} else if (code == HttpConstants.NO_CONTENT) {
        	stop(HttpConstants.NO_CONTENT, "");
        } else if (code == HttpConstants.TEMP_REDIRECT) {
            context.getSparkResponse().redirect(context.getLocation());
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
