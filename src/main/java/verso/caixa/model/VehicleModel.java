package verso.caixa.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import verso.caixa.enums.VehicleStatusEnum;

import java.util.*;

@Entity
@Table(name = "tb_vehicle")
public class VehicleModel extends PanacheEntityBase{

    private static final Map<VehicleStatusEnum, Set<VehicleStatusEnum>> VEHICLE_STATUS = new HashMap<>() {
    };

    static {
        VEHICLE_STATUS.put(VehicleStatusEnum.AVAILABLE, Set.of(VehicleStatusEnum.RENTED, VehicleStatusEnum.UNDER_MAINTENANCE));
        VEHICLE_STATUS.put(VehicleStatusEnum.RENTED, Set.of(VehicleStatusEnum.AVAILABLE, VehicleStatusEnum.UNDER_MAINTENANCE));
        VEHICLE_STATUS.put(VehicleStatusEnum.UNDER_MAINTENANCE, Set.of(VehicleStatusEnum.AVAILABLE));
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID vehicleId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String carTitle;

    private String brand;

    @Column(name = "vehicle_year")

    private Integer year;

    private String engine;

    @Enumerated(EnumType.STRING)
    private VehicleStatusEnum status = VehicleStatusEnum.AVAILABLE;
    private String model;

    protected VehicleModel() {}

    public VehicleModel(String brand, String model, int year, String engine) {
        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("model must not be null");
        }
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.engine = engine;
    }

    public UUID getVehicleId() {
        return vehicleId;
    }

    public String getModel() {
        return model;
    }

    public VehicleStatusEnum getStatus() {
        return status;
    }

    public Integer getYear() {
        return year;
    }

    public String getEngine() {
        return engine;
    }

    public boolean isRented() {
        return this.getStatus().equals(VehicleStatusEnum.RENTED);
    }

    public void setStatus(VehicleStatusEnum incomingStatus) {
        Set<VehicleStatusEnum> possibleStatus = VEHICLE_STATUS.get(this.status);

        if (incomingStatus.equals(this.status)) {
            return;
        }

        if (possibleStatus.contains(incomingStatus)) {
            this.status = incomingStatus;
        } else {
            throw new IllegalArgumentException("Validation error, possible status are: " + possibleStatus);
        }
    }

    public String getBrand() {
        return brand;
    }
}

