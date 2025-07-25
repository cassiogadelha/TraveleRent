package verso.caixa.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import verso.caixa.enums.BookingStatusEnum;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingRequestBody {
    public UUID vehicleId;
    String customerName;

    @NotNull(message = "A data de início não pode ser nula")
    @FutureOrPresent(message = "A data de início não pode ser anterior a hoje.")
    LocalDate startDate;

    @NotNull(message = "A data de término não pode ser nula")
    @Future(message = "A data de término não pode ser anterior a de início")
    LocalDate endDate;

    BookingStatusEnum status;
}
