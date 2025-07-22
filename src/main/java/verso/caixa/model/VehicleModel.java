package verso.caixa.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import verso.caixa.enums.VehicleStatusEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tb_users")
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
    private int year;

    private String engine;
    private VehicleStatusEnum status = VehicleStatusEnum.AVAILABLE;
    private String model;

    protected VehicleModel() {}

    public VehicleModel(String model, String brand, int year, String engine) {
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

    public int getYear() {
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

