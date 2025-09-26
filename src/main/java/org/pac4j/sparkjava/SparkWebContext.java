package org.pac4j.sparkjava;

import org.pac4j.core.util.CommonHelper;
import org.pac4j.jee.context.JEEContext;
import spark.Request;
import spark.Response;

/**
 * Web context specific to Sparkjava.
 *
 * @author Jerome Leleu
 * @since 1.0.0
 */
public class SparkWebContext extends JEEContext {

	private final Request request;

	private final Response response;

	/**
	 * Construct a Spark-based JEEContext wrapper.
	 * @param request the Spark request
	 * @param response the Spark response
	 */
	public SparkWebContext(final Request request, final Response response) {
		super(request.raw(), response.raw());
		CommonHelper.assertNotNull("request", request);
		CommonHelper.assertNotNull("response", response);
		this.request = request;
		this.response = response;
	}

	/**
	 * Access the underlying Spark response.
	 * @return the Spark Response
	 */
	public Response getSparkResponse() {
		return response;
	}

	/**
	 * Access the underlying Spark request.
	 * @return the Spark Request
	 */
	public Request getSparkRequest() {
		return request;
	}

	@Override
	public String getPath() {
		return request.pathInfo();
	}
}
