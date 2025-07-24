package verso.caixa.dto;

import verso.caixa.enums.VehicleStatusEnum;
import verso.caixa.model.VehicleModel;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record VehicleResponseBody(
        UUID id,
        String model,
        VehicleStatusEnum status,
        int year,
        String engine,
        String brand,
        String carTitle,
        Set<AccessoryDTO> accessories,
        List<MaintenanceDTO>maintenances
) {

    public VehicleResponseBody(VehicleModel vehicle) {

        this(
            vehicle.getVehicleId(),
            vehicle.getModel(),
            vehicle.getStatus(),
            vehicle.getYear(),
            vehicle.getEngine(),
            vehicle.getBrand(),
     vehicle.getModel() + " " + vehicle.getYear() + " " + vehicle.getEngine(),
            vehicle.getAccessories().stream()
                .map(a -> new AccessoryDTO(a.getAccessoryId(), a.getName()))
                .collect(Collectors.toSet()),
            vehicle.getMaintenances().stream()
                .map(m -> new MaintenanceDTO(m.getMaintenanceId(), m.getProblemDescription(), m.getCreatedAt()))
                .collect(Collectors.toList())
        );
    }
}