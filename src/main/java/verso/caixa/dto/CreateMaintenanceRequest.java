package verso.caixa.dto;

import jakarta.validation.constraints.NotNull;

public record CreateMaintenanceRequest(

        @NotNull(message = "Informe a descrição do problema")
        String problemDescription
) {
}
