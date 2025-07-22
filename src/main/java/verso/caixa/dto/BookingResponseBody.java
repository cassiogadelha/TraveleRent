package verso.caixa.dto;

import verso.caixa.enums.BookingStatusEnum;
import verso.caixa.model.BookingModel;

import java.time.LocalDate;
import java.util.UUID;

public record BookingResponseBody(
        UUID bookingId,
        UUID vehicleId,
        String customerName,
        LocalDate startDate,
        LocalDate endDate,
        BookingStatusEnum status
) {
    public BookingResponseBody(BookingModel bookingModel) {

        this(bookingModel.getBookingId(), bookingModel.getVehicleId(), bookingModel.getCustomerName(),
                bookingModel.getStartDate(), bookingModel.getEndDate(), bookingModel.getStatus());
    }
}
