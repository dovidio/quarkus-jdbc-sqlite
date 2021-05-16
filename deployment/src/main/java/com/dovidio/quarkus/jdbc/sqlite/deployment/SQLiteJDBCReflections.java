package com.dovidio.quarkus.jdbc.sqlite.deployment;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;
import org.sqlite.JDBC;

/**
 * Registers the {@code org.sqlite.JDBC} Driver so that it can be loaded
 * by reflection, as commonly expected.
 *
 */
public class SQLiteJDBCReflections {

    @BuildStep
    void build(BuildProducer<ReflectiveClassBuildItem> reflectiveClass) {
        // Not strictly necessary when using Agroal, as it also registers
        // any JDBC driver being configured explicitly through its configuration.
        // We register it for the sake of people not using Agroal.
        reflectiveClass.produce(new ReflectiveClassBuildItem(false, false, JDBC.class));
    }
}
