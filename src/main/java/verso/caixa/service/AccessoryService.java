package verso.caixa.service;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import verso.caixa.dto.AddAccessoryRequest;
import verso.caixa.model.AccessoryModel;
import verso.caixa.model.VehicleModel;

import java.util.UUID;

@ApplicationScoped
public class AccessoryService {

    public Response addAccessory(UUID id, AddAccessoryRequest request) {

        VehicleModel vehicle = VehicleModel.findById(id);

        if (vehicle == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        AccessoryModel accessory = new AccessoryModel(request.name());

        accessory.persist();

        vehicle.addAccessory(accessory);

        Log.info(vehicle.getAccessories());

        return Response.noContent().build();
    }
}
