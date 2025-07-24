package verso.caixa.resource;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import verso.caixa.dto.UpdateVehicleStatusRequestBody;
import verso.caixa.dto.VehicleRequestBody;
import verso.caixa.exception.VehicleDeletionException;
import verso.caixa.service.VehicleService;

import java.util.UUID;

@Path("/api/v1/vehicles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VehicleResource {

    private final VehicleService vehicleService;

    public VehicleResource(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @POST
    @Transactional
    public Response createVehicle(@Valid VehicleRequestBody vehicleRequestBody){

        return vehicleService.createVehicle(vehicleRequestBody);
    }

    @GET
    public Response findAllVehicles(@QueryParam("page") @DefaultValue("0") int page,
                                    @QueryParam("size") @DefaultValue("10") int size){
        return vehicleService.getVehicleList(page, size);
    }

    @GET
    @Path("{id}")
    public Response findById(@PathParam("id") UUID vehicleId){
        return vehicleService.findById(vehicleId);
    }

    @PATCH
    @Path("{id}")
    @Transactional
    public Response updateVehiclePartially(@PathParam("id") UUID vehicleId, UpdateVehicleStatusRequestBody body){
        return vehicleService.updateVehicle(vehicleId, body);
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteById(@PathParam("id") UUID vehicleId){
        vehicleService.deleteById(vehicleId);
        return Response.noContent().build();
    }
}
