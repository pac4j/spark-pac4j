package org.pac4j.sparkjava;

import lombok.val;
import org.pac4j.core.context.FrameworkParameters;
import org.pac4j.core.context.WebContextFactory;
import org.pac4j.jee.context.JEEContext;

/**
 * Specific Spark context factory for pac4j v6.
 */
public class SparkContextFactory implements WebContextFactory {

    /**
     * Singleton instance.
     */
    public static final SparkContextFactory INSTANCE = new SparkContextFactory();

    /**
     * Default constructor.
     */
    public SparkContextFactory() {
    }

    @Override
    public JEEContext newContext(final FrameworkParameters parameters) {
        val p = (SparkFrameworkParameters) parameters;
        return new SparkWebContext(p.getRequest(), p.getResponse());
    }
}
