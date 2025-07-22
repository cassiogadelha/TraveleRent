package verso.caixa.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import verso.caixa.enums.BookingStatusEnum;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "tb_booking")
public class BookingModel extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID bookingId;

    public UUID vehicleId;

    private String customerName;
    private LocalDate startDate;
    private LocalDate endDate;

    private BookingStatusEnum status = BookingStatusEnum.CREATED;

    public BookingModel(UUID vehicleId, String customerName, LocalDate startDate, LocalDate endDate) {
        this.vehicleId = vehicleId;
        this.customerName = customerName;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
