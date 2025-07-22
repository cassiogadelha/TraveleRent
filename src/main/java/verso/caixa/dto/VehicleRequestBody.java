package verso.caixa.dto;

import verso.caixa.enums.VehicleStatusEnum;

/**
 * VehicleDTO, VehicleRequest, CreateVehicleRequest, VehicleRequestBody
 */
public class VehicleRequestBody {

    private String model;
    private VehicleStatusEnum status;
    private int year;
    private String engine;
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
