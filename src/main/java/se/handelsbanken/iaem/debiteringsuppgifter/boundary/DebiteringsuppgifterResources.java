package se.handelsbanken.iaem.debiteringsuppgifter.boundary;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonCollectors;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import se.handelsbanken.iaem.debiteringsuppgifter.control.Debiteringsuppgifter;
import se.handelsbanken.iaem.debiteringsuppgifter.entity.DebiteringsuppgiftInput;
import se.handelsbanken.iaem.debiteringsuppgifter.entity.DebiteringsuppgiftRow;

import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

@Path("/debiteringsuppgifter")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class DebiteringsuppgifterResources {

    @Inject
    Debiteringsuppgifter debiteringsuppgifter;

    @GET
    public Response list() {
        var items = debiteringsuppgifter.list()
                .stream()
                .map(DebiteringsuppgiftRow::toJSON)
                .collect(JsonCollectors.toJsonArray());
        return Response.ok(items).build();
    }

    @POST
    @Transactional
    public Response create(JsonObject body) {
        var input = DebiteringsuppgiftInput.fromJSON(body);
        var created = debiteringsuppgifter.create(input);
        return Response.status(Response.Status.CREATED).entity(created.toJSON()).build();
    }

    @PUT
    @Path("/{produktid}")
    @Transactional
    public Response update(
            @PathParam("produktid") String produktid,
            JsonObject body) {
        var input = DebiteringsuppgiftInput.fromJSON(body);
        return debiteringsuppgifter.update(produktid, input)
                .map(r -> Response.ok(r.toJSON()).build())
                .orElse(Response.status(NOT_FOUND).build());
    }

    @DELETE
    @Path("/{produktid}")
    @Transactional
    public Response delete(@PathParam("produktid") String produktid) {
        if (!debiteringsuppgifter.delete(produktid)) {
            return Response.status(NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}
