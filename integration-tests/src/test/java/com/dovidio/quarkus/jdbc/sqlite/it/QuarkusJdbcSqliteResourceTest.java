package com.dovidio.quarkus.jdbc.sqlite.it;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class QuarkusJdbcSqliteResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/quarkus-jdbc-sqlite")
                .then()
                .statusCode(200)
                .body(is("connection works"));
    }
}
