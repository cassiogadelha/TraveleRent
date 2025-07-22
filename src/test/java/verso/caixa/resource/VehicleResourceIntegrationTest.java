package verso.caixa.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import verso.caixa.dto.VehicleRequestBody;
import verso.caixa.enums.VehicleStatusEnum;
import verso.caixa.model.VehicleModel;

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
        VehicleModel vehicle = new VehicleModel("Mobi", 2025, "1.0");
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
}
