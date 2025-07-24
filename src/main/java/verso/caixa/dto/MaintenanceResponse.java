package verso.caixa.dto;

import java.time.Instant;
import java.util.UUID;

public record MaintenanceResponse(
        UUID maintenanceId,
        String problemDescription,
        Instant createdAt,
        VehicleInfo vehicleInfo
) {}

