package verso.caixa.resource;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import verso.caixa.dto.BookingRequestBody;
import verso.caixa.dto.UpdateBookingStatusRequestBody;
import verso.caixa.service.BookingService;

import java.util.UUID;

@Path("/api/v1/bookings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookingResource {
    private final BookingService bookingService;

    public BookingResource(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @POST
    @Transactional
    public Response createBooking(BookingRequestBody bookingRequestBody){
        return bookingService.createBooking(bookingRequestBody);
    }

    @GET
    public Response findAllBookings(){
        return bookingService.getBookingList();
    }

    @GET
    @Path("{id}")
    public Response findById(@PathParam("id") UUID vehicleId){
        return Response.ok(bookingService.findById(vehicleId)).build();
    }

    @PATCH
    @Path("{id}")
    @Transactional
    public Response updateVehiclePartially(@PathParam("id") UUID vehicleId, UpdateBookingStatusRequestBody body){
        return bookingService.updateVehicle(vehicleId, body);
    }
}
