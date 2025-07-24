package verso.caixa.dto;

import java.time.Instant;
import java.util.UUID;

public record MaintenanceDTO(
        UUID maintenanceId,
        String problemDescription,
        Instant createdAt
) {}

