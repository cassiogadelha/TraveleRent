package verso.caixa.service;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.ws.rs.core.Response;
import verso.caixa.dto.UpdateVehicleStatusRequestBody;
import verso.caixa.dto.VehicleRequestBody;
import verso.caixa.dto.VehicleResponseBody;
import verso.caixa.model.VehicleModel;
import verso.caixa.exception.VehicleDeletionException;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@ApplicationScoped //cria somente uma instancia durante todo o ciclo de vida da aplicação
public class VehicleService {

    public Response createVehicle(VehicleRequestBody vehicleRequestBody){
        try {
            VehicleModel newVehicleModel = new VehicleModel(
                    vehicleRequestBody.getBrand(),
                    vehicleRequestBody.getModel(),
                    vehicleRequestBody.getYear(),
                    vehicleRequestBody.getEngine());

            newVehicleModel.persist();

            URI location = URI.create("/api/v1/vehicles/" + newVehicleModel.getVehicleId());

            return Response.created(location)
                    .entity(newVehicleModel)
                    .build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Response findById(UUID vehicleId){
        VehicleModel vehicleModelFound = VehicleModel.findById(vehicleId);

        if (vehicleModelFound == null) {
            return Response.status(404).build();
        }

        VehicleResponseBody vehicleResponseBody = new VehicleResponseBody(vehicleModelFound);

        return Response.ok(
                vehicleResponseBody
        ).build();
    }

    public void deleteById(UUID vehicleId){
        VehicleModel vehicleToDelete = VehicleModel.findById(vehicleId);

        if(vehicleToDelete == null)
            throw new VehicleDeletionException("Veículo não encontrado!");

        if (vehicleToDelete.isRented()) {
            throw new VehicleDeletionException("Veículo não pode ser deletado pois está alugado!");
        }

        vehicleToDelete.delete();
    }

    public Response getVehicleList(int page, int size) {
        PanacheQuery<VehicleModel> vehicles = VehicleModel.findAll();
        vehicles.page(Page.of(page, size));

        if (vehicles.list().isEmpty()) {
            Map<String, String> response = Map.of("mensagem", "A lista de veículos está vazia."); //cria um map imutavel para ser convertido facilmente em Json
            return Response.status(Response.Status.OK).entity(response).build();
        } else {
            return Response.ok(vehicles.list().stream().map(VehicleResponseBody::new).toList()).build();
        }
    }

    public Response updateVehicle(UUID vehicleId, UpdateVehicleStatusRequestBody body) {
        VehicleModel vehicleModel = VehicleModel.findById(vehicleId);

        if (vehicleModel == null) return Response.status(404).build();

        try {
            vehicleModel.setStatus(body.status());
        } catch (RuntimeException e) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        return Response.noContent().build();
    }
}



