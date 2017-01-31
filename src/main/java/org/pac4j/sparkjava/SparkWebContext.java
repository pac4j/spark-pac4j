package org.pac4j.sparkjava;

import org.pac4j.core.context.HttpConstants;
import org.pac4j.core.context.J2EContext;
import org.pac4j.core.context.session.SessionStore;
import spark.Request;
import spark.Response;

/**
 * Web context specific to Sparkjava.
 *
 * @author Jerome Leleu
 * @since 1.0.0
 */
public class SparkWebContext extends J2EContext {

	private final Request request;

	private final Response response;

	private String location = null;

	public SparkWebContext(final Request request, final Response response) {
		this(request, response, null);
	}

	public SparkWebContext(final Request request, final Response response, final SessionStore sessionStore) {
		super(request.raw(), response.raw(), sessionStore);
		this.request = request;
		this.response = response;
	}

	@Override
	public void writeResponseContent(String content) {
		response.body(content);
	}

	@Override
	public void setResponseStatus(int code) {
		response.status(code);
	}

	public Response getSparkResponse() {
		return response;
	}

	public Request getSparkRequest() {
		return request;
	}

	@Override
	public String getPath() {
		return request.pathInfo();
	}

	@Override
	public void setResponseHeader(final String name, final String value) {
		if (HttpConstants.LOCATION_HEADER.equals(name)) {
			location = value;
		}
		super.setResponseHeader(name, value);
	}

	public String getLocation() {
		return location;
	}
}
