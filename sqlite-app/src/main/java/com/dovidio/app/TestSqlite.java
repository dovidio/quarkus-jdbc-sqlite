package com.dovidio.app;
import io.agroal.api.AgroalDataSource;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;

@Path("/quarkus-jdbc-sqlite")
@ApplicationScoped
public class TestSqlite {

    @Inject
    public AgroalDataSource dc;

    @GET
    public String hello() throws IOException, SQLException {
        try (final Connection c = dc.getConnection()) {
            executeStm(c, "CREATE TABLE FOO(MSG VARCHAR(255), ACTIVE BOOLEAN)");
            executeStm(c, "INSERT INTO FOO VALUES ('bar', true)");
            executeStm(c, "UPDATE FOO set MSG = 'baz' where MSG = 'bar'");
            executeStm(c, "DELETE FROM FOO where MSG='baz'");
        }

        return "connection works";
    }

    private void executeStm(Connection c, String sql) throws SQLException {
        try (final var stm = c.prepareStatement(sql)) {
            stm.executeUpdate();
        }
    }
}

