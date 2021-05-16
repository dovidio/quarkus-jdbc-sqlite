package com.dovidio.quarkus.jdbc.sqlite.it;

import org.sqlite.SQLiteDataSource;

import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;

@Path("/quarkus-jdbc-sqlite")
@ApplicationScoped
public class QuarkusJdbcSqliteResource {

    @GET
    public String hello() throws IOException, SQLException {
        final DataSource dc = createDatasource();

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

    private SQLiteDataSource createDatasource() throws IOException {
        final var dbFile = Files.createTempFile("sqlite", ".db");
        dbFile.toFile().deleteOnExit();
        final var dc = new SQLiteDataSource();
        dc.setUrl("jdbc:sqlite:" + dbFile);
        return dc;
    }
}
