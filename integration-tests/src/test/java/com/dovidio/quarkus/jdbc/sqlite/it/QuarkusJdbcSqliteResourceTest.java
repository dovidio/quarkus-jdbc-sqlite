package com.dovidio.quarkus.jdbc.sqlite.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class QuarkusJdbcSqliteResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/quarkus-jdbc-sqlite")
                .then()
                .statusCode(200)
                .body(is("msg=It Works from SQLITE!!!, active=true"));
    }
}
