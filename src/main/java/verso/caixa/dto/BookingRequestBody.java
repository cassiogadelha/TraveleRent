package verso.caixa.dto;

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
    LocalDate startDate;
    LocalDate endDate;
    BookingStatusEnum status;
}
