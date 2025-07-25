package verso.caixa.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingRequestBody {

    @NotNull(message = "Veículo não informado!")
    public UUID vehicleId;

    @NotNull(message = "Informe o nome do cliente.")
    String customerName;

    @NotNull(message = "Informe a data de início do  aluguel.")
    @FutureOrPresent(message = "A data de início não pode ser anterior a hoje.")
    LocalDate startDate;

    @NotNull(message = "Informe a data de início término do aluguel.")
    @Future(message = "A data de término não pode ser anterior a de início")
    LocalDate endDate;
}
