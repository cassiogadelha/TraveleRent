package verso.caixa.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import verso.caixa.enums.VehicleStatusEnum;

public class VehicleTest {
    private Long id;
    private String model;
    private VehicleStatusEnum status;
    private int year;
    private String engine;

    /**
     * 1. Quando um vehicle for criado ele deve possuir um ID válido
     * 2. Quando um vehicle for criado ele deve ser criado com o status o AVAILABLE
     * 3. Quando o Vehicle AVAILABLE ele só pode ir pra RENTED ou UNDER_MAINTENANCE
     * 4. Quando eu criar um Vehicle ele não pode ter model, year, engine (vazios ou nulos)
     * 5. (opcional) o ano deve ser atual (2025 <= year)
     */

    @Test
    void shouldCreateVehicleWithValidID() {
        VehicleModel vehicle = new VehicleModel("Mobi", 2025, "1.0");

        // assertions
        Assertions.assertNotNull(vehicle.getVehicleId());
    }

    @Test
    void shouldCreateVehicleWithStatusAvailable() {
        VehicleModel vehicle = new VehicleModel("Mobi", 2025, "1.0");

        // assertions
        Assertions.assertEquals(VehicleStatusEnum.AVAILABLE, vehicle.getStatus());
    }

    @Test
    void shouldChangeStatusToRentedOrUnderMaintenanceWhenCurrentStatusIsAvailable() {
        VehicleModel vehicle = new VehicleModel("Mobi", 2025, "1.0");

        vehicle.setStatus(VehicleStatusEnum.UNDER_MAINTENANCE);

        Assertions.assertEquals(VehicleStatusEnum.UNDER_MAINTENANCE, vehicle.getStatus());
    }

    @Test
    void shouldThrowsChangeToRentedWhenTheCurrentStatusIsUnderMaintenance() {
        VehicleModel vehicle = new VehicleModel("Mobi", 2025, "1.0");
        vehicle.setStatus(VehicleStatusEnum.UNDER_MAINTENANCE);

        String message = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            vehicle.setStatus(VehicleStatusEnum.RENTED);
        }).getMessage();

        Assertions.assertTrue(message.contains("Validation error"));
    }

    @Test
    void shouldCreateVehicleWithoutNullOrEmptyFields() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new VehicleModel(null, 2025, "1.0");
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new VehicleModel("", 2025, "1.0");
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new VehicleModel("    ", 2025, "1.0");
        });

    }
}
