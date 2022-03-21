package org.pac4j.sparkjava;

import org.pac4j.core.context.WebContextFactory;
import spark.Request;
import spark.Response;

/**
 * Specific Spark context factory.
 *
 * @author Jerome LELEU
 * @since 5.0.0
 */
public class SparkContextFactory implements WebContextFactory {

    public static final SparkContextFactory INSTANCE = new SparkContextFactory();

    @Override
    public SparkWebContext newContext(final Object... parameters) {
        return new SparkWebContext((Request) parameters[0], (Response) parameters[1]);
    }
}
