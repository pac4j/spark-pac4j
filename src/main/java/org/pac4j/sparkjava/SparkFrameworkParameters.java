package org.pac4j.sparkjava;

import org.pac4j.core.context.FrameworkParameters;
import spark.Request;
import spark.Response;

/**
 * FrameworkParameters carrier for Spark.
 */
public final class SparkFrameworkParameters implements FrameworkParameters {
    private final Request request;
    private final Response response;

    /**
     * Construct a carrier of Spark Request/Response for pac4j.
     * @param request the Spark request
     * @param response the Spark response
     */
    public SparkFrameworkParameters(final Request request, final Response response) {
        this.request = request;
        this.response = response;
    }

    /**
     * Get the Spark request.
     * @return the request
     */
    public Request getRequest() { return request; }

    /**
     * Get the Spark response.
     * @return the response
     */
    public Response getResponse() { return response; }
}
