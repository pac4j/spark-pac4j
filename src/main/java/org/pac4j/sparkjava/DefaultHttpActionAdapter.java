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
import org.pac4j.core.context.WebContext;
import org.pac4j.core.http.HttpActionAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.halt;

/**
 * Default HTTP action adapter.
 *
 * @author Jerome Leleu
 * @since 1.8.2
 */
public class DefaultHttpActionAdapter implements HttpActionAdapter {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Object adapt(int code, WebContext context) {
        final SparkWebContext webContext = (SparkWebContext) context;
        logger.debug("requires HTTP action: {}", code);
        if (code == HttpConstants.UNAUTHORIZED) {
            halt(HttpConstants.UNAUTHORIZED, "authentication required");
        } else if (code == HttpConstants.FORBIDDEN) {
            halt(HttpConstants.FORBIDDEN, "forbidden");
        } else if (code == HttpConstants.OK) {
            halt(HttpConstants.OK, webContext.getBody());
        } else if (code == HttpConstants.TEMP_REDIRECT) {
            webContext.getSparkResponse().redirect(webContext.getLocation());
        }
        return null;
    }
}
