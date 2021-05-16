package com.dovidio.quarkus.jdbc.sqlite.deployment;

import com.dovidio.quarkus.jdbc.sqlite.runtime.SQLiteJDBCAgroalConnectionConfigurer;
import io.quarkus.agroal.spi.JdbcDriverBuildItem;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.processor.BuiltinScope;
import io.quarkus.datasource.deployment.spi.DefaultDataSourceDbKindBuildItem;
import io.quarkus.deployment.Capabilities;
import io.quarkus.deployment.Capability;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.JniRuntimeAccessBuildItem;
import io.quarkus.deployment.builditem.nativeimage.NativeImageResourceDirectoryBuildItem;
import org.sqlite.Function;
import org.sqlite.ProgressHandler;
import org.sqlite.core.NativeDB;

public class QuarkusJdbcSqliteProcessor {

    private static final String DB_KIND = "sqlite";
    private static final String FEATURE = "quarkus-jdbc-sqlite";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    void registerDriver(BuildProducer<JdbcDriverBuildItem> jdbcDriver) {
        jdbcDriver.produce(new JdbcDriverBuildItem(DB_KIND, "org.sqlite.JDBC", null));
    }

    @BuildStep
    void configureAgroalConnection(BuildProducer<AdditionalBeanBuildItem> additionalBeans,
                                   Capabilities capabilities) {
        if (capabilities.isPresent(Capability.AGROAL)) {
            additionalBeans.produce(new AdditionalBeanBuildItem.Builder().addBeanClass(SQLiteJDBCAgroalConnectionConfigurer.class)
                    .setDefaultScope(BuiltinScope.APPLICATION.getName())
                    .setUnremovable()
                    .build());
        }
    }

    @BuildStep
    void registerServiceBinding(BuildProducer<DefaultDataSourceDbKindBuildItem> dbKind) {
        dbKind.produce(new DefaultDataSourceDbKindBuildItem(DB_KIND));
    }

    @BuildStep
    void registerNativeBinaries(BuildProducer<NativeImageResourceDirectoryBuildItem> resource) {
        resource.produce(new NativeImageResourceDirectoryBuildItem("org/sqlite/native"));
    }

    @BuildStep
    void registerClassesThatAreAccessedViaJni(BuildProducer<JniRuntimeAccessBuildItem> jniRuntimeAccessibleClasses) {
        jniRuntimeAccessibleClasses
                .produce(new JniRuntimeAccessBuildItem(true, true, true, NativeDB.class, Function.class, Function.Aggregate.class, ProgressHandler.class, Function.Window.class, org.sqlite.core.DB.ProgressObserver.class));
    }
}
