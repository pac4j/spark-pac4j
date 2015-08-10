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

import org.pac4j.core.context.WebContext;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Web context specific to Sparkjava.
 *
 * @author Jerome Leleu
 * @since 1.0.0
 */
public class SparkWebContext implements WebContext {

	private final Request request;

	private final Response response;

	private int status = 0;

	private String body = null;

	public SparkWebContext(final Request request, final Response response) {
		this.request = request;
		this.response = response;
	}

	@Override
	public String getRequestParameter(String name) {
		return request.queryParams(name);
	}

	@Override
	public Map<String, String[]> getRequestParameters() {
		Map<String, String[]> newParams = new HashMap<String, String[]>();
		Set<String> keys = request.queryParams();
		for (String key : keys) {
			String[] params = new String[1];
			params[0] = request.queryParams(key);
			newParams.put(key, params);
		}
		return newParams;
	}

	@Override
	public String getRequestHeader(String name) {
		return request.headers(name);
	}

	@Override
	public void setSessionAttribute(String name, Object value) {
		request.session().attribute(name, value);
	}

	@Override
	public Object getSessionAttribute(String name) {
		return request.session().attribute(name);
	}

	@Override
	public String getRequestMethod() {
		return request.requestMethod();
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

	@Override
	public void setResponseHeader(String name, String value) {
		response.header(name, value);
	}

	@Override
	public String getServerName() {
		return request.host();
	}

	@Override
	public int getServerPort() {
		return request.port();

	}

	@Override
	public String getScheme() {
		return request.scheme();
	}

	@Override
	public String getFullRequestURL() {
		StringBuilder requestURL = new StringBuilder(request.url());
		String queryString = request.queryString();
		if (queryString == null) {
			return requestURL.toString();
		}
		else {
			return requestURL.append('?').append(queryString).toString();
		}
	}

	public String getBody() {
		return body;
	}

	public int getStatus() {
		return status;
	}
}
