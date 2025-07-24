package verso.caixa.service;

import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import verso.caixa.dto.CreateMaintenanceRequest;
import verso.caixa.dto.MaintenanceResponse;
import verso.caixa.mappers.MaintenanceMapper;
import verso.caixa.mappers.VehicleMapper;
import verso.caixa.model.MaintenanceModel;
import verso.caixa.model.VehicleModel;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class MaintenanceService {
    @Inject
    MaintenanceMapper maintenanceMapper;

    public Response addMaintenance(UUID vehicleId, CreateMaintenanceRequest request) {
        Optional<VehicleModel> possibleVehicle = VehicleModel.findByIdOptional(vehicleId);

        if (possibleVehicle.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        VehicleModel vehicle = possibleVehicle.get();
        MaintenanceModel maintenance = new MaintenanceModel(
                request.problemDescription(),
                vehicle
        );

        maintenance.persist();

        vehicle.moveForMaintenance(maintenance);

        return Response.created(URI.create("/api/v1/vehicles/%s/maintenances/%s".formatted(
                vehicleId, maintenance.getMaintenanceId()
        ))).build();
    }

    public Response findById(UUID vehicleId, UUID maintenanceId) {

        MaintenanceModel maintenance = MaintenanceModel.findByVehicleAndMaintenanceId(vehicleId, maintenanceId);

        if (maintenance == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        MaintenanceResponse response = maintenanceMapper.toResponse(maintenance);

        return Response.ok(response).build();
    }
}
