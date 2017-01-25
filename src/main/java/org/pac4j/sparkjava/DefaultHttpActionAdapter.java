package org.pac4j.sparkjava;

import org.pac4j.core.context.HttpConstants;
import org.pac4j.core.http.HttpActionAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.halt;

/**
 * Default HTTP action adapter.
 *
 * @author Jerome Leleu
 * @since 1.1.0
 */
public class DefaultHttpActionAdapter implements HttpActionAdapter<Object, SparkWebContext> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Object adapt(int code, SparkWebContext context) {
        logger.debug("requires HTTP action: {}", code);
        if (code == HttpConstants.UNAUTHORIZED) {
            halt(HttpConstants.UNAUTHORIZED, "authentication required");
        } else if (code == HttpConstants.FORBIDDEN) {
            halt(HttpConstants.FORBIDDEN, "forbidden");
        } else if (code == HttpConstants.OK) {
            halt(HttpConstants.OK, context.getBody());
        } else if (code == HttpConstants.TEMP_REDIRECT) {
            context.getSparkResponse().redirect(context.getLocation());
        }
        return null;
    }
}
