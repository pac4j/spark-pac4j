package org.pac4j.sparkjava;

import org.pac4j.core.context.JEEContext;
import org.pac4j.core.context.session.JEESessionStore;
import org.pac4j.core.context.session.SessionStore;
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

	public SparkWebContext(final Request request, final Response response) {
		this(request, response, JEESessionStore.INSTANCE);
	}

	public SparkWebContext(final Request request, final Response response, final SessionStore sessionStore) {
		super(request.raw(), response.raw(), sessionStore);
		this.request = request;
		this.response = response;
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
}
