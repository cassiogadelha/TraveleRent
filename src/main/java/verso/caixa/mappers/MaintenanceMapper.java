package verso.caixa.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import verso.caixa.dto.MaintenanceResponse;
import verso.caixa.dto.VehicleInfo;
import verso.caixa.model.MaintenanceModel;
import verso.caixa.model.VehicleModel;

@Mapper(componentModel = "cdi")
public interface MaintenanceMapper {
    @Mapping(target = "vehicleInfo", expression = "java(toVehicleInfo(model.getVehicleModel()))")
    MaintenanceResponse toResponse(MaintenanceModel model);

    default VehicleInfo toVehicleInfo(VehicleModel vehicle) {
        return new VehicleInfo(
                vehicle.getVehicleId(),
                vehicle.getModel(),
                vehicle.getBrand(),
                vehicle.getYear(),
                vehicle.getEngine(),
                vehicle.getStatus(),
                vehicle.getCarTitle()
        );
    }

}

