package verso.caixa.service;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import verso.caixa.dto.UpdateVehicleStatusRequestBody;
import verso.caixa.dto.VehicleRequestBody;
import verso.caixa.dto.VehicleResponseBody;
import verso.caixa.enums.ErrorCode;
import verso.caixa.exception.VehicleDeletionException;
import verso.caixa.mappers.VehicleMapper;
import verso.caixa.model.MaintenanceModel;
import verso.caixa.model.VehicleModel;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@Getter
@ApplicationScoped //cria somente uma instancia durante todo o ciclo de vida da aplicação
public class VehicleService {

    @Inject
    VehicleMapper vehicleMapper;

    public Response createVehicle(VehicleRequestBody vehicleRequestBody){
        try {
            VehicleModel vehicle = vehicleMapper.toEntity(vehicleRequestBody);

            for (MaintenanceModel m : vehicle.getMaintenances()) {
                m.setVehicle(vehicle);
            }

            vehicle.setCarTitle(vehicle.getModel() + " " + vehicle.getYear() + " " + vehicle.getEngine());

            // Persistência em cascata
            vehicle.persist();

            URI location = URI.create("/api/v1/vehicles/" + vehicle.getVehicleId());
            return Response.created(location)
                    .entity(new VehicleResponseBody(vehicle))
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
            throw new VehicleDeletionException("Veículo não encontrado!", ErrorCode.VEHICLE_NOT_FOUND);

        if (vehicleToDelete.isRented()) {
            throw new VehicleDeletionException("Veículo não pode ser deletado pois está alugado!", ErrorCode.VEHICLE_RENTED_DELETE_DENIED);
        }

        vehicleToDelete.delete();
    }

    public Response getVehicleList(int page, int size) {
        PanacheQuery<VehicleModel> vehicles = VehicleModel.findAll();
        vehicles.page(Page.of(page, size));

        if (vehicles.list().isEmpty()) {
            Map<String, String> response = Map.of("mensagem", "A lista de veículos está vazia."); //cria um map imutavel para ser convertido facilmente em Json
            return Response.ok(response).build();
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



