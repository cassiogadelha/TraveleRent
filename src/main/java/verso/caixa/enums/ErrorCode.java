package verso.caixa.enums;

public enum ErrorCode {

    // Veículo
    VEHICLE_NOT_FOUND("VEHICLE-001"),
    VEHICLE_RENTED_DELETE_DENIED("VEHICLE-002");
    /*

    Manutenção
    MAINTENANCE_NOT_FOUND("MAINTENANCE-001"),
    MAINTENANCE_VEHICLE_REQUIRED("MAINTENANCE-002"),

    Acessório
    ACCESSORY_ALREADY_LINKED("ACCESSORY-001"),

    Genéricos
    BUSINESS_VALIDATION_FAILED("GENERIC-001");

     */

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}

