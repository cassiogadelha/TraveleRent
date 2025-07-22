package verso.caixa.dto;

import verso.caixa.enums.VehicleStatusEnum;

public record UpdateVehicleStatusRequestBody(
        VehicleStatusEnum status
) {
}
