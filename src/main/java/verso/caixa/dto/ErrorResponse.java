package verso.caixa.dto;

import java.time.Instant;

public record ErrorResponse(
        String title,
        String details,
        int statusCode,
        String path,
        Instant timestamp,
        String errorCode
) {}

