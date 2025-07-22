package verso.caixa.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import verso.caixa.dto.BookingRequestBody;
import verso.caixa.dto.BookingResponseBody;
import verso.caixa.dto.UpdateBookingStatusRequestBody;
import verso.caixa.model.BookingModel;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@ApplicationScoped //cria somente uma instancia durante todo o ciclo de vida da aplicação
public class BookingService {
    public Response createBooking(@NotNull BookingRequestBody bookingRequestBody) {
        try {
            if (bookingRequestBody.getStartDate().isBefore(LocalDate.now()))
                throw new IllegalArgumentException("A data de início não pode ser anterior a hoje.");

            if (bookingRequestBody.getEndDate().isBefore(bookingRequestBody.getStartDate()))
                throw new IllegalArgumentException("A data de término não pode ser anterior a de início");

            if (bookingRequestBody.getVehicleId() == null)
                throw new IllegalArgumentException("O ID do veículo não é válido!");

            BookingModel newBookingModel = new BookingModel(
                    bookingRequestBody.getVehicleId(),
                    bookingRequestBody.getCustomerName(),
                    bookingRequestBody.getStartDate(),
                    bookingRequestBody.getEndDate());

            newBookingModel.persist();

            URI location = URI.create("/api/v1/bookings/" + newBookingModel.getVehicleId());

            return Response.created(location)
                    .entity(newBookingModel)
                    .build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    public Response getBookingList() {
        List<BookingModel> bookings = BookingModel.listAll();

        if (bookings.isEmpty()) {
            Map<String, String> response = Map.of("mensagem", "A lista de agendamentos está vazia."); //cria um map imutavel para ser convertido facilmente em Json
            return Response.status(Response.Status.OK).entity(response).build();
        } else {
            return Response.ok(bookings.stream().map(BookingResponseBody::new).toList()).build();
        }
    }

    public Response findById(UUID bookingId) {
        BookingModel bookingModelFound = BookingModel.findById(bookingId);

        if (bookingModelFound == null) {
            return Response.status(404).build();
        }

        BookingResponseBody bookingResponseBody = new BookingResponseBody(bookingModelFound);

        return Response.ok(
                bookingResponseBody
        ).build();
    }

    public Response updateVehicle(UUID bookingId, UpdateBookingStatusRequestBody body) {
        BookingModel bookingModel = BookingModel.findById(bookingId);

        if (bookingModel == null) return Response.status(404).build();

        try {
            bookingModel.setStatus(body.status());
        } catch (RuntimeException e) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        return Response.noContent().build();
    }
}
