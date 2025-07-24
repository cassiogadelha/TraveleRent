package verso.caixa.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import verso.caixa.dto.AccessoryCreateDTO;
import verso.caixa.dto.MaintenanceCreateDTO;
import verso.caixa.dto.VehicleRequestBody;
import verso.caixa.model.AccessoryModel;
import verso.caixa.model.MaintenanceModel;
import verso.caixa.model.VehicleModel;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "cdi")
public interface VehicleMapper {
    @Mapping(target = "status", constant = "AVAILABLE")
    VehicleModel toEntity(VehicleRequestBody dto);

    AccessoryModel toEntity(AccessoryCreateDTO dto);

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now().atZone(java.time.ZoneId.systemDefault()).toInstant())")
    MaintenanceModel toEntity(MaintenanceCreateDTO dto);

    List<MaintenanceModel> toMaintenanceEntities(List<MaintenanceCreateDTO> dtoList);
    Set<AccessoryModel> toAccessoryEntities(Set<AccessoryCreateDTO> dtoSet);

    @AfterMapping
    default void linkVehicle(@MappingTarget VehicleModel vehicle) {
        if (vehicle.getMaintenances() != null) {
            for (MaintenanceModel m : vehicle.getMaintenances()) {
                m.setVehicle(vehicle);
            }
        }
    }

}


