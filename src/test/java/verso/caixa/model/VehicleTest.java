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
        VehicleModel vehicle = new VehicleModel("Mobi", "Renault", 2025, "1.0");

        // assertions
        Assertions.assertNotNull(vehicle.getVehicleId());
    }

    @Test
    void shouldCreateVehicleWithStatusAvailable() {
        VehicleModel vehicle = new VehicleModel("Mobi", "Renault", 2025, "1.0");

        // assertions
        Assertions.assertEquals(VehicleStatusEnum.AVAILABLE, vehicle.getStatus());
    }

    @Test
    void shouldCreateVehicleWithoutNullOrEmptyFields() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new VehicleModel(null, "Renault", 2025, "1.0");
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new VehicleModel("", "Renault", 2025, "1.0");
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new VehicleModel("    ", "Renault", 2025, "1.0");
        });

    }

    @Test
    void shouldChangeFromAvailableToRented() {
        VehicleModel vehicle = new VehicleModel("Fiesta", "Ford", 2019, "1.6");
        vehicle.setStatus(VehicleStatusEnum.RENTED);
        Assertions.assertEquals(
                VehicleStatusEnum.RENTED,
                vehicle.getStatus() //AVAILABLE → RENTED deve ser permitido
        );
    }

    @Test
    void shouldChangeFromAvailableToUnderMaintenance() {
        VehicleModel vehicle = new VehicleModel("Civic", "Honda", 2021, "2.0");
        vehicle.setStatus(VehicleStatusEnum.UNDER_MAINTENANCE);
        Assertions.assertEquals(
                VehicleStatusEnum.UNDER_MAINTENANCE,
                vehicle.getStatus() //"AVAILABLE → UNDER_MAINTENANCE deve ser permitido"
        );
    }

    @Test
    void shouldNotAllowUnderMaintenanceToRented() {
        VehicleModel vehicle = new VehicleModel("Corolla", "Toyota", 2022, "1.8");
        vehicle.setStatus(VehicleStatusEnum.UNDER_MAINTENANCE);

        IllegalArgumentException ex = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> vehicle.setStatus(VehicleStatusEnum.RENTED)
        );
        Assertions.assertTrue(
                ex.getMessage().contains("possible status")
        );
    }

    @Test
    void settingSameStatusShouldBeNoOp() {
        VehicleModel vehicle = new VehicleModel("Gol", "VW", 2018, "1.0");
        vehicle.setStatus(VehicleStatusEnum.AVAILABLE);
        // não deve lançar exceção nem alterar valor
        Assertions.assertEquals(
                VehicleStatusEnum.AVAILABLE,
                vehicle.getStatus()
        );
    }

    @Test
    void shouldChangeFromRentedBackToAvailable() {
        VehicleModel vehicle = new VehicleModel("Onix", "Chevrolet", 2023, "1.4");
        vehicle.setStatus(VehicleStatusEnum.RENTED);
        vehicle.setStatus(VehicleStatusEnum.AVAILABLE);
        Assertions.assertEquals(
                VehicleStatusEnum.AVAILABLE,
                vehicle.getStatus()// RENTED → AVAILABLE deve ser permitido
        );
    }

    @Test
    void shouldChangeFromRentedToUnderMaintenance() {
        VehicleModel vehicle = new VehicleModel("Renegade", "Jeep", 2021, "2.0");
        vehicle.setStatus(VehicleStatusEnum.RENTED);
        vehicle.setStatus(VehicleStatusEnum.UNDER_MAINTENANCE);
        Assertions.assertEquals(
                VehicleStatusEnum.UNDER_MAINTENANCE,
                vehicle.getStatus() //RENTED → UNDER_MAINTENANCE deve ser permitido

        );
    }
}
