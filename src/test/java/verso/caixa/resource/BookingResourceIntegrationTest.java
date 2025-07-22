package verso.caixa.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.Matchers.*;

@QuarkusTest
public class BookingResourceIntegrationTest {

    @Test
    void shouldCreateBookingSuccessfully() {
        var requestBody = """
            {
              "vehicleId": "%s",
              "customerName": "Bruno Farias",
              "startDate": "%s",
              "endDate": "%s"
            }
            """.formatted(
                UUID.randomUUID(),
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(5)
        );

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/v1/bookings")
                .then()
                .statusCode(201)
                .body("customerName", equalTo("Bruno Farias"))
                .body("startDate", equalTo(LocalDate.now().plusDays(1).toString()))
                .body("endDate", equalTo(LocalDate.now().plusDays(5).toString()))
                .body("status", equalTo("CREATED"));
    }

    @Test
    void shouldReturnBadRequestForBookingWithInvalidStartDate() {

        var requestBody = """
            {
              "vehicleId": "%s",
              "customerName": "Talita Borges",
              "startDate": "%s",
              "endDate": "%s"
            }
            """.formatted(
                UUID.randomUUID(),
                LocalDate.now().minusDays(2),
                LocalDate.now().plusDays(3)
        );

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/v1/bookings")
                .then()
                .statusCode(400)
                .body(equalTo("A data de início não pode ser anterior a hoje."));
    }

    @Test
    void shouldCancelBookingSuccessfully() {
        var createJson = """
            {
              "vehicleId": "%s",
              "customerName": "Marcos Vinícius",
              "startDate": "%s",
              "endDate": "%s"
            }
            """.formatted(
                UUID.randomUUID(),
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(4)
        );

        //Executa POST e extrai o Location
        String location = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(createJson)
                .when()
                .post("/api/v1/bookings")
                .then()
                .log().all()
                .statusCode(201)
                .header("Location", startsWith("http://localhost:8081/api/v1/bookings/"))
                .extract()
                .header("Location");

        // Obtém o ID extraindo a parte final da URL
        String bookingId = location.substring(location.lastIndexOf("/") + 1);

        // Monta o JSON de cancelamento
        var cancelJson = """
            {
              "status": "CANCELED"
            }
            """;

        // Chama o PATCH para cancelar e verifica 204 No Content
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(cancelJson)
                .when()
                .patch("/api/v1/bookings/{id}", bookingId)
                .then()
                .statusCode(204);

        //  confirmar que o status realmente mudou (opcional).
        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get("/api/v1/bookings/{id}", bookingId)
                .then()
                .statusCode(200)
                .body("entity.status", equalTo("CANCELED"))
                .body("entity.bookingId", equalTo(bookingId));
    }

    @Test
    void shouldFailToCancelAlreadyCanceledBooking() {
        var createJson = """
      {
        "vehicleId": "%s",
        "customerName": "Larissa Araújo",
        "startDate": "%s",
        "endDate": "%s"
      }
      """.formatted(
            UUID.randomUUID(),
            LocalDate.now().plusDays(1),
            LocalDate.now().plusDays(4)
        );

        String location =
            RestAssured.given()
                .contentType(ContentType.JSON)
                    .body(createJson)
                .when()
                .post("/api/v1/bookings")
                .then()
                .statusCode(201)
                .header("Location", startsWith("http://localhost:8081/api/v1/bookings/"))
                .extract()
                .header("Location");

        String bookingId = location.substring(location.lastIndexOf('/') + 1);

        //Cancela a reserva pela primeira vez → deve ser 204
        var cancelJson = """
            {
              "status": "CANCELED"
            }
            """;

        RestAssured.given()
        .contentType(ContentType.JSON)
        .body(cancelJson)
        .when()
        .patch("/api/v1/bookings/{id}", bookingId)
        .then()
        .statusCode(204);

        // Tenta cancelar de novo → falha 409
        RestAssured.given()
        .contentType(ContentType.JSON)
        .body(cancelJson)
        .when()
        .patch("/api/v1/bookings/{id}", bookingId)
        .then()
        .statusCode(409)
        .body(containsString("possible status"));
    }

    @Test
    void shouldFinalizeBookingSuccessfully() {
        var createJson = """
            {
              "vehicleId": "%s",
              "customerName": "Beatriz Costa",
              "startDate": "%s",
              "endDate": "%s"
            }
            """.formatted(
                UUID.randomUUID(),
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(5)
        );

        String location =
                RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(createJson)
                        .when()
                        .post("/api/v1/bookings")
                        .then()
                        .statusCode(201)
                        .header("Location", startsWith("http://localhost:8081/api/v1/bookings/"))
                        .extract()
                        .header("Location");

        String bookingId = location.substring(location.lastIndexOf('/') + 1);

        //Finaliza a reserva com status FINISHED → espera 204 No Content
        var finishJson = """
            { "status": "FINISHED" }
            """;

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(finishJson)
                .when()
                .patch("/api/v1/bookings/{id}", bookingId)
                .then()
                .statusCode(204);

        //GET para confirmar que o status mudou para FINISHED
        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get("/api/v1/bookings/{id}", bookingId)
                .then()
                //.log().body()
                .statusCode(200)
                .body("entity.bookingId", equalTo(bookingId))
                .body("entity.status", equalTo("FINISHED"));
    }

}
