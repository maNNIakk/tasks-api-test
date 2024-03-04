package br.ce.wcaquino.tasks.apitest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

public class APITest {

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8001/tasks-backend";
    }

    @Test
    public void deveRetornarTarefas() {
        RestAssured.given()
                .when()
                .get("/todo")
                .then()
                .statusCode(200);
    }

    @Test
    public void deveAdicionarTarefaComSucesso() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{ \"task\": \"Teste via API\",\n" +
                        "        \"dueDate\": \"2025-12-30\"\n" +
                        "    \n" +
                        "        }\n")
                .when()
                .post("/todo")
                .then()
                .statusCode(201);

    }

    @Test
    public void NaodeveAdicionarTarefaInvalida() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{ \"task\": \"Teste via API\",\n" +
                        "        \"dueDate\": \"2020-12-30\"\n" +
                        "    \n" +
                        "        }\n")
                .when()
                .post("/todo")
                .then()
                .statusCode(400)
                .body("message", CoreMatchers.is("Due date must not be in past"));

    }

    @Test
    public void deveRemoverTarefaComSucesso() {
        Integer id = RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{ \"task\": \"Teste para deletar via API\",\n" +
                        "        \"dueDate\": \"2025-12-30\"\n" +
                        "    \n" +
                        "        }\n")
                .when()
                .post("/todo")
                .then()
                .log().all()
                .statusCode(201)
                .extract().path("id");
        RestAssured.given()
                .when()
                .delete("/todo/" +id)
                .then()
                .statusCode(204);
    }
}
