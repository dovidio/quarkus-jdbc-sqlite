/*
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements.  See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* The ASF licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License.  You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.dovidio.quarkus.jdbc.sqlite.it;

import org.sqlite.SQLiteDataSource;

import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

@Path("/quarkus-jdbc-sqlite")
@ApplicationScoped
public class QuarkusJdbcSqliteResource {

    @GET
    public String hello() throws IOException, SQLException {
        final DataSource dc = createDatasource();

        String result = "";

        try (final Connection c = dc.getConnection()) {
            executeStm(c, "CREATE TABLE FOO(MSG VARCHAR(255), ACTIVE BOOLEAN)");
            executeStm(c, "INSERT INTO FOO VALUES ('It Works from SQLITE!!!', true)");
            try (var ps = c.prepareStatement("SELECT * FROM FOO ")) {
                try (final ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result += String.format("msg=%s, active=%b", rs.getString("MSG"), rs.getString("ACTIVE"));
                    }
                }
            }
        }

        return result;
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
