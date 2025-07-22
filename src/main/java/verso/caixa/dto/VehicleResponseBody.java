package verso.caixa.dto;

import verso.caixa.enums.VehicleStatusEnum;
import verso.caixa.model.VehicleModel;

import java.util.UUID;

public record VehicleResponseBody(
        UUID id,
        String model,
        VehicleStatusEnum status,
        int year,
        String engine,
        String brand,
        String carTitle
) {

    public VehicleResponseBody(VehicleModel vehicle) {

        this(vehicle.getVehicleId(),vehicle.getModel(), vehicle.getStatus(), vehicle.getYear(), vehicle.getEngine(), vehicle.getBrand(),
                vehicle.getModel() + " " + vehicle.getYear() + " " + vehicle.getEngine());
    }
}