package verso.caixa.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import verso.caixa.dto.VehicleRequestBody;
import verso.caixa.enums.VehicleStatusEnum;
import verso.caixa.model.VehicleModel;

import java.util.UUID;

import static org.hamcrest.Matchers.*;

@QuarkusTest
public class VehicleResourceIntegrationTest {

    static ObjectMapper objectMapper = new ObjectMapper();

    private String createVehicle() throws JsonProcessingException {
        VehicleRequestBody vehicleRequestBody = new VehicleRequestBody();
        vehicleRequestBody.setStatus(VehicleStatusEnum.AVAILABLE);
        vehicleRequestBody.setEngine("1.0");
        vehicleRequestBody.setModel("Mobi");
        vehicleRequestBody.setYear(2022);

        String body = objectMapper.writeValueAsString(vehicleRequestBody);

        Response response = RestAssured.given()
                .contentType("application/json")
                .body(body)
                .post("api/v1/vehicles")
                .thenReturn();

        return response.getHeader("Location");
    }

    @Transactional
    public void createVehicleInDatabase() {
        VehicleModel vehicle = new VehicleModel("Mobi", "Renault", 2025, "1.0");
        vehicle.persist();
    }

    @BeforeEach
    void beforeEach() {
        Log.info("Executando antes de todos os testes");
    }

    @Test
    void shouldReturn201WhenSendAValidVehicle() {

        RestAssured.given()
                .contentType("application/json")
                .body("""
                       {
                         "brand": "Fiat",
                         "model": "Mobi",
                         "status": "RENTED",
                         "year": 2022,
                         "engine": "1.0"
                       }
                      """)
                .post("api/v1/vehicles")
                .then()
                .statusCode(201);
    }

    @Test
    void shouldGetVehicleByID() throws JsonProcessingException {
        String location = createVehicle();
        RestAssured.given()
                .get(location)
                .then()
                .statusCode(200);
    }

    @Test
    void shouldReceiveNotFoundWhenThereIsNoVehicleWithProvidedID() {
        RestAssured.given()
                .get("/api/v1/vehicles/1292929")
                .then()
                .statusCode(404);
    }

    @Test
    void shouldGetAll() {
        createVehicleInDatabase();
        RestAssured.given()
                .get("api/v1/vehicles")
                .then()
                .statusCode(200);
    }

    private String makeVehicleJson(String model, String brand, int year, String engine) {
        return """
          {
            "model": "%s",
            "brand": "%s",
            "year": %d,
            "engine": "%s"
          }
          """.formatted(model, brand, year, engine);
    }

    @Test
    void shouldReturnEmptyMessageWhenNoVehicles() {
        RestAssured.given()
            .accept(ContentType.JSON)
            .when()
            .get("/api/v1/vehicles")
            .then()
            .statusCode(200)
            .body("mensagem", equalTo("A lista de veículos está vazia."));
    }

    @Test
    void shouldReturnAllVehiclesSuccessfully() {

        String vehicle1 = makeVehicleJson("Ka", "Ford", 2020, "1.0");
        String vehicle2 = makeVehicleJson("Civic", "Honda", 2021, "2.0");

        String id1 =
            RestAssured.given()
                .contentType(ContentType.JSON)
                .body(vehicle1)
                .when()
                .post("/api/v1/vehicles")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");// captura o valor do cabeçalho Location da resposta

                //.replaceAll(".*/api/v1/vehicles/", ""); //- Aplica expressão regular para remover tudo
                                                                            // antes e incluindo /api/v1/vehicles/,
                                                                            // deixando apenas o UUID do veículo.
        String id2 =
            RestAssured.given()
                .contentType(ContentType.JSON)
                .body(vehicle2)
                .when()
                .post("/api/v1/vehicles")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");

                //.replaceAll(".*/api/v1/vehicles/", "");

        id1 = id1.substring(id1.lastIndexOf('/') + 1); // Localiza a posição do último / e retorna tudo o que vem depois.
        id2 = id2.substring(id2.lastIndexOf('/') + 1); // Localiza a posição do último / e retorna tudo o que vem depois.


        // GET /vehicles deve retornar 200 e lista contendo os dois registros
        RestAssured.given()
            .accept(ContentType.JSON)
            .when()
            .get("/api/v1/vehicles")
            .then()
                .log().body()
            .statusCode(200)
            .body("size()", is(2))
            // validação pontual de algum campo
            .body("[0].id", anyOf(equalTo(id1), equalTo(id2)))
            .body("[0].id", anyOf(equalTo(id1), equalTo(id2)));
    }

    @Test
    void shouldReturnNotFoundWhenVehicleDoesNotExist() {
        UUID nonexistentId = UUID.randomUUID();

        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get("/api/v1/vehicles/{id}", nonexistentId)
                .then()
                .statusCode(404);
    }
    @Test
    void shouldReturnBadRequestWhenModelIsBlank() {
        var json = """
        {
          "model": "",
          "brand": "Ford",
          "year": 2020,
          "engine": "1.0"
        }
        """;

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/v1/vehicles")
                .then()
                .statusCode(400)
                .body("violations.message", hasItem("model não pode ser vazio"));
    }

    @Test
    void shouldReturnBadRequestWhenBrandIsBlank() {
        var json = """
        {
          "model": "Ka",
          "brand": "",
          "year": 2020,
          "engine": "1.0"
        }
        """;

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/v1/vehicles")
                .then()
                .statusCode(400)
                .body("violations.message", hasItem("brand não pode ser vazio"));
    }

    @Test
    void shouldReturnBadRequestWhenEngineIsBlank() {
        var json = """
        {
          "model": "Ka",
          "brand": "Ford",
          "year": 2020,
          "engine": ""
        }
        """;

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/v1/vehicles")
                .then()
                .statusCode(400)
                .body("violations.message", hasItem("engine não pode ser vazio"));
    }

    @Test
    void shouldReturnBadRequestWhenYearIsMissing() {
        var json = """
        {
          "model": "Ka",
          "brand": "Ford",
          "engine": "1.0"
        }
        """;

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/v1/vehicles")
                .then()
                .statusCode(400)
                .body("violations.message", hasItem("year não pode ser nulo"));
    }

    @Test
    void shouldReturnBadRequestWhenAllFieldsAreInvalid() {
        var json = """
        {
          "model": "",
          "brand": "",
          "engine": ""
        }
        """;

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/v1/vehicles")
                .then()
                .log().body()
                .statusCode(400)
                .body("violations.message", hasItems(
                        "model não pode ser vazio",
                        "brand não pode ser vazio",
                        "engine não pode ser vazio",
                        "year não pode ser nulo"
                ));
    }

    @Test
    void shouldReturnPaginatedVehicleListSuccessfully() {

        for (int i = 1; i <= 15; i++) {
            var json = """
        {
          "model": "Model %d",
          "brand": "Brand %d",
          "year": %d,
          "engine": "1.0"
        }
        """.formatted(i, i, 2020 + i % 5);

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(json)
                    .when()
                    .post("/api/v1/vehicles")
                    .then()
                    .statusCode(201);
        }

        // Faz o GET paginado → página 1 (segunda página), com 5 veículos por página
        RestAssured.given()
                .accept(ContentType.JSON)
                .queryParam("page", 1)
                .queryParam("size", 5)
                .when()
                .get("/api/v1/vehicles")
                .then()
                .log().body()
                .statusCode(200)
                .body("size()", is(5))
                .body("[0].model", startsWith("Model"))
                .body("[0].brand", startsWith("Brand"))
                .body("[0].year", greaterThanOrEqualTo(2020))
                .body("[0].engine", equalTo("1.0"));
    }
}
