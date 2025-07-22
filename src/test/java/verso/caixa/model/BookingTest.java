package verso.caixa.model;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import verso.caixa.dto.BookingRequestBody;
import verso.caixa.dto.UpdateBookingStatusRequestBody;
import verso.caixa.enums.BookingStatusEnum;
import verso.caixa.enums.VehicleStatusEnum;
import verso.caixa.service.BookingService;

import java.time.LocalDate;
import java.util.UUID;

public class BookingTest {
    @Test
    void shouldCreateBookingSuccessfully() {

        UUID vehicleId = UUID.randomUUID();
        String customerName = "Fernando Lopes";
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(5);


        BookingModel booking = new BookingModel(vehicleId, customerName, startDate, endDate);


        Assertions.assertEquals(vehicleId, booking.getVehicleId());
        Assertions.assertEquals(customerName, booking.getCustomerName());
        Assertions.assertEquals(startDate, booking.getStartDate());
        Assertions.assertEquals(endDate, booking.getEndDate());
        Assertions.assertEquals(BookingStatusEnum.CREATED, booking.getStatus());
    }

    @Test
    void shouldFailToCreateBookingWithPastStartDate() {
        BookingService bookingService = new BookingService();

        BookingRequestBody bookingRequestBody = new BookingRequestBody();
        bookingRequestBody.setVehicleId(UUID.randomUUID());
        bookingRequestBody.setCustomerName("Camila Marques");
        bookingRequestBody.setStartDate(LocalDate.now().minusDays(2));
        bookingRequestBody.setEndDate(LocalDate.now().plusDays(3));

        Response response = bookingService.createBooking(bookingRequestBody);

        Assertions.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

    }

    @Test
    void shouldFailToCreateBookingWhenEndDateIsBeforeStartDate() {
        BookingService bookingService = new BookingService();

        BookingRequestBody bookingRequestBody = new BookingRequestBody();
        bookingRequestBody.setVehicleId(UUID.randomUUID());
        bookingRequestBody.setCustomerName("Patrícia Lima");
        bookingRequestBody.setStartDate(LocalDate.now().plusDays(5));
        bookingRequestBody.setEndDate(LocalDate.now().plusDays(2));

        Response response = bookingService.createBooking(bookingRequestBody);
        Assertions.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    void shouldFailToCreateBookingWhenVehicleIdIsNull() {
        BookingService bookingService = new BookingService();

        BookingRequestBody bookingRequestBody = new BookingRequestBody();
        bookingRequestBody.setVehicleId(null);
        bookingRequestBody.setCustomerName("Carlos Rocha");
        bookingRequestBody.setStartDate(LocalDate.now().plusDays(1));
        bookingRequestBody.setEndDate(LocalDate.now().plusDays(4));

        Response response = bookingService.createBooking(bookingRequestBody);

        Assertions.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    void shouldCancelBookingWithStatusCreatedSuccessfully() {
        BookingModel booking = new BookingModel(
                UUID.randomUUID(),
                "Isabela Martins",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3)
        );

        UpdateBookingStatusRequestBody body = new UpdateBookingStatusRequestBody(BookingStatusEnum.CANCELED);

        booking.setStatus(body.getStatus());

        Assertions.assertEquals(BookingStatusEnum.CANCELED, booking.getStatus());
    }

    @Test
    void shouldFailToCancelAlreadyCanceledBooking() {

        BookingModel booking = new BookingModel(
                UUID.randomUUID(),
                "Juliana Moura",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(4)
        );

        booking.setStatus(BookingStatusEnum.CANCELED);

        UpdateBookingStatusRequestBody body = new UpdateBookingStatusRequestBody(BookingStatusEnum.CANCELED);

        try {
            booking.setStatus(body.getStatus());
            Assertions.fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
            Assertions.assertTrue(e.getMessage().contains("possible status are"));
        }
    }

    @Test
    void shouldFailToCancelAlreadyFinishedBooking() {

        BookingModel booking = new BookingModel(
                UUID.randomUUID(),
                "Maurício Alves",
                LocalDate.now().plusDays(2),
                LocalDate.now().plusDays(5)
        );

        booking.setStatus(BookingStatusEnum.FINISHED);

        UpdateBookingStatusRequestBody body = new UpdateBookingStatusRequestBody(BookingStatusEnum.CANCELED);

        try {
            booking.setStatus(body.getStatus());
            Assertions.fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
            Assertions.assertTrue(e.getMessage().contains("possible status are"));
        }
    }

    @Test
    void shouldFinalizeBookingWithCreatedStatusSuccessfully() {
        BookingModel booking = new BookingModel(
                UUID.randomUUID(),
                "Rafael Nunes",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(4)
        );

        booking.setStatus(BookingStatusEnum.FINISHED);

        Assertions.assertEquals(BookingStatusEnum.FINISHED, booking.getStatus());
    }

    @Test
    void shouldFailToFinishAlreadyCanceledBooking() {

        BookingModel booking = new BookingModel(
                UUID.randomUUID(),
                "Juliana Moura",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(4)
        );

        booking.setStatus(BookingStatusEnum.CANCELED);

        UpdateBookingStatusRequestBody body = new UpdateBookingStatusRequestBody(BookingStatusEnum.FINISHED);

        try {
            booking.setStatus(body.getStatus());
            Assertions.fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
            Assertions.assertTrue(e.getMessage().contains("possible status are"));
        }
    }

    @Test
    void shouldFailToFinishAlreadyFinishedBooking() {

        BookingModel booking = new BookingModel(
                UUID.randomUUID(),
                "Maurício Alves",
                LocalDate.now().plusDays(2),
                LocalDate.now().plusDays(5)
        );

        booking.setStatus(BookingStatusEnum.FINISHED);

        UpdateBookingStatusRequestBody body = new UpdateBookingStatusRequestBody(BookingStatusEnum.FINISHED);

        try {
            booking.setStatus(body.getStatus());
            Assertions.fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
            Assertions.assertTrue(e.getMessage().contains("possible status are"));
        }
    }
}
