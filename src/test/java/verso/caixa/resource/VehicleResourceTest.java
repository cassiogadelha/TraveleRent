package verso.caixa.resource;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import verso.caixa.dto.VehicleRequestBody;
import verso.caixa.enums.VehicleStatusEnum;
import verso.caixa.service.VehicleService;

class VehicleResourceTest {

    @Test
    void shouldCreateAVehicle() {

        VehicleService mockService = Mockito.mock(VehicleService.class);
        Mockito.when(mockService.createVehicle(Mockito.any())).thenReturn(Response.status(201).build());

        VehicleResource vehicleResource = new VehicleResource(mockService);


        VehicleRequestBody vehicleRequestBody = new VehicleRequestBody();
        vehicleRequestBody.setStatus(VehicleStatusEnum.AVAILABLE);
        vehicleRequestBody.setModel("Mobi");
        vehicleRequestBody.setYear(2018);
        vehicleRequestBody.setEngine("1.0");

        Response response = vehicleResource.createVehicle(vehicleRequestBody);

        Assertions.assertEquals(201, response.getStatus());
    }
}