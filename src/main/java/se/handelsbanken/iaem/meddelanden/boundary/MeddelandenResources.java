package se.handelsbanken.iaem.meddelanden.boundary;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.stream.JsonCollectors;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import se.handelsbanken.iaem.meddelanden.control.Meddelanden;
import se.handelsbanken.iaem.meddelanden.entity.MeddelandeRow;

import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

@Path("/kunder/{kundnr}/meddelanden")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class MeddelandenResources {

    @Inject
    Meddelanden meddelanden;

    @GET
    public Response list(
            @PathParam("kundnr") String kundnr,
            @QueryParam("from") String from,
            @QueryParam("to") String to,
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("10") int size) {

        var all = meddelanden.list(kundnr);

        var filtered = all.stream()
                .filter(m -> from == null || from.isBlank() || m.datum().compareTo(from) >= 0)
                .filter(m -> to == null || to.isBlank() || m.datum().compareTo(to + "T23:59:59") <= 0)
                .toList();

        int total = filtered.size();
        int fromIndex = Math.min((page - 1) * size, total);
        int toIndex = Math.min(fromIndex + size, total);

        var items = filtered.subList(fromIndex, toIndex)
                .stream()
                .map(MeddelandeRow::toJSON)
                .collect(JsonCollectors.toJsonArray());

        var result = Json.createObjectBuilder()
                .add("total", total)
                .add("items", items)
                .build();

        return Response.ok(result).build();
    }

    @GET
    @Path("/{meddId}")
    public Response find(
            @PathParam("kundnr") String kundnr,
            @PathParam("meddId") String meddId) {

        return meddelanden.find(kundnr, meddId)
                .map(r -> Response.ok(r.toJSON()).build())
                .orElse(Response.status(NOT_FOUND).build());
    }
}
