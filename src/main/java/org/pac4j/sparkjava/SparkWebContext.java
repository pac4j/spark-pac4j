/*
  Copyright 2015 - 2015 pac4j organization

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
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

	private int status = 0;

	private String body = null;

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
		body = content;
	}

	@Override
	public void setResponseStatus(int code) {
		response.status(code);
		status = code;
	}

	public String getBody() {
		return body;
	}

	public int getStatus() {
		return status;
	}

	public Response getSparkResponse() {
		return response;
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
