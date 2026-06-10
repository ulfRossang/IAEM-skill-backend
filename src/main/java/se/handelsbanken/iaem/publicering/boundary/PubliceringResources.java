package se.handelsbanken.iaem.publicering.boundary;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonCollectors;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import se.handelsbanken.iaem.publicering.control.Publicering;
import se.handelsbanken.iaem.publicering.entity.GodkannRequest;
import se.handelsbanken.iaem.publicering.entity.InformationssambandRow;
import se.handelsbanken.iaem.publicering.entity.PubliceringJobbRow;

import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

@Path("/publicering")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class PubliceringResources {

    @Inject
    Publicering publicering;

    @GET
    @Path("/installningar")
    public Response listInstallningar() {
        var items = publicering.listInformationssamband()
                .stream()
                .map(InformationssambandRow::toJSON)
                .collect(JsonCollectors.toJsonArray());
        return Response.ok(items).build();
    }

    @POST
    @Path("/installningar")
    @Transactional
    public Response createInstallning(JsonObject body) {
        var input = InformationssambandRow.fromJSON(body);
        var created = publicering.createInformationssamband(input);
        return Response.status(Response.Status.CREATED).entity(created.toJSON()).build();
    }

    @PUT
    @Path("/installningar/{id}")
    @Transactional
    public Response updateInstallning(
            @PathParam("id") String id,
            JsonObject body) {
        var input = InformationssambandRow.fromJSON(body);
        return publicering.updateInformationssamband(id, input)
                .map(r -> Response.ok(r.toJSON()).build())
                .orElse(Response.status(NOT_FOUND).build());
    }

    @DELETE
    @Path("/installningar/{id}")
    @Transactional
    public Response deleteInstallning(@PathParam("id") String id) {
        if (!publicering.deleteInformationssamband(id)) {
            return Response.status(NOT_FOUND).build();
        }
        return Response.noContent().build();
    }

    @GET
    @Path("/jobb")
    public Response listJobb() {
        var items = publicering.listJobb()
                .stream()
                .map(PubliceringJobbRow::toJSON)
                .collect(JsonCollectors.toJsonArray());
        return Response.ok(items).build();
    }

    @POST
    @Path("/jobb/{jobbId}/godkann")
    @Transactional
    public Response godkann(
            @PathParam("jobbId") String jobbId,
            JsonObject body) {
        var request = GodkannRequest.fromJSON(body);
        if (!publicering.godkannJobb(jobbId, request.godkand())) {
            return Response.status(NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}
