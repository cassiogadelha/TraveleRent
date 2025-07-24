package verso.caixa.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import verso.caixa.enums.VehicleStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;


/**
 * VehicleDTO, VehicleRequest, CreateVehicleRequest, VehicleRequestBody
 */
@Getter
@Setter
public class VehicleRequestBody {

    @NotBlank(message = "model não pode ser vazio")
    private String model;
    private VehicleStatusEnum status;
    @NotNull(message = "year não pode ser nulo")
    @Min(value = 1980, message = "year deve ser no mínimo 1980")
    private Integer year;
    @NotBlank(message = "engine não pode ser vazio")
    private String engine;
    @NotBlank(message = "brand não pode ser vazio")
    private String brand;
    List<AccessoryCreateDTO> accessories;
    List<MaintenanceCreateDTO> maintenances;
}
