package org.pac4j.framework.adapter;

import org.pac4j.core.config.Config;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.jee.adapter.JEEFrameworkAdapter;
import org.pac4j.sparkjava.SparkContextFactory;
import org.pac4j.sparkjava.SparkFrameworkParameters;
import org.pac4j.sparkjava.SparkHttpActionAdapter;

/**
 * Specific config startup for Spark.
 */
public class FrameworkAdapterImpl extends JEEFrameworkAdapter {

    /** Default constructor. */
    public FrameworkAdapterImpl() {
    }

    @Override
    public void applyDefaultSettingsIfUndefined(final Config config) {
        CommonHelper.assertNotNull("config", config);

        config.setWebContextFactoryIfUndefined(fp -> SparkContextFactory.INSTANCE.newContext((SparkFrameworkParameters) fp));
        config.setHttpActionAdapterIfUndefined(SparkHttpActionAdapter.INSTANCE);

        super.applyDefaultSettingsIfUndefined(config);
    }

    @Override
    public String toString() {
        return "Spark";
    }
}
