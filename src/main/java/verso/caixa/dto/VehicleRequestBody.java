package verso.caixa.dto;

import jakarta.validation.constraints.Min;
import verso.caixa.enums.VehicleStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


/**
 * VehicleDTO, VehicleRequest, CreateVehicleRequest, VehicleRequestBody
 */
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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public VehicleStatusEnum getStatus() {
        return status;
    }

    public void setStatus(VehicleStatusEnum status) {
        this.status = status;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public void setBrand(String brand) {this.brand = brand;}
    public String getBrand() { return brand; }
}
