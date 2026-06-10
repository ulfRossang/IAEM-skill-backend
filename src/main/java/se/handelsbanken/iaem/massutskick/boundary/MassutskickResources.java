package se.handelsbanken.iaem.massutskick.boundary;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonCollectors;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import se.handelsbanken.iaem.massutskick.control.Massutskick;
import se.handelsbanken.iaem.massutskick.entity.MassutskickInput;
import se.handelsbanken.iaem.massutskick.entity.MassutskickRow;

import static jakarta.ws.rs.core.Response.Status.CONFLICT;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

@Path("/massutskick")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class MassutskickResources {

    @Inject
    Massutskick massutskick;

    @GET
    public Response list() {
        var items = massutskick.list()
                .stream()
                .map(MassutskickRow::toJSON)
                .collect(JsonCollectors.toJsonArray());
        return Response.ok(items).build();
    }

    @POST
    @Transactional
    public Response create(JsonObject body) {
        var input = MassutskickInput.fromJSON(body);
        var created = massutskick.create(input);
        return Response.status(Response.Status.CREATED).entity(created.toJSON()).build();
    }

    @PUT
    @Path("/{meddId}")
    @Transactional
    public Response update(
            @PathParam("meddId") String meddId,
            JsonObject body) {
        var input = MassutskickInput.fromJSON(body);
        return massutskick.update(meddId, input)
                .map(r -> Response.ok(r.toJSON()).build())
                .orElse(Response.status(NOT_FOUND).build());
    }

    @DELETE
    @Path("/{meddId}")
    @Transactional
    public Response delete(@PathParam("meddId") String meddId) {
        if (!massutskick.delete(meddId)) {
            return Response.status(NOT_FOUND).build();
        }
        return Response.noContent().build();
    }

    @POST
    @Path("/{meddId}/klarmarkera")
    @Transactional
    public Response klarmarkera(@PathParam("meddId") String meddId) {
        return switch (massutskick.klarmarkera(meddId)) {
            case NOT_FOUND -> Response.status(NOT_FOUND).build();
            case CONFLICT -> Response.status(CONFLICT).build();
            case OK -> Response.noContent().build();
        };
    }
}
