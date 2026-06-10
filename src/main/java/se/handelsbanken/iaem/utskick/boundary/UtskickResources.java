package se.handelsbanken.iaem.utskick.boundary;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.stream.JsonCollectors;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import se.handelsbanken.iaem.utskick.control.Utskick;
import se.handelsbanken.iaem.utskick.entity.UtskickRow;

import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

@Path("/kunder/{kundnr}/utskick")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class UtskickResources {

    @Inject
    Utskick utskick;

    @GET
    public Response list(
            @PathParam("kundnr") String kundnr,
            @QueryParam("kategori") String kategori,
            @QueryParam("from") String from,
            @QueryParam("to") String to,
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("10") int size) {

        var all = utskick.list(kundnr);

        var filtered = all.stream()
                .filter(u -> kategori == null || kategori.isBlank() || kategori.equalsIgnoreCase(u.kategori()))
                .filter(u -> from == null || from.isBlank() || u.datum().compareTo(from) >= 0)
                .filter(u -> to == null || to.isBlank() || u.datum().compareTo(to + "T23:59:59") <= 0)
                .toList();

        int total = filtered.size();
        int fromIndex = Math.min((page - 1) * size, total);
        int toIndex = Math.min(fromIndex + size, total);

        var items = filtered.subList(fromIndex, toIndex)
                .stream()
                .map(UtskickRow::toJSON)
                .collect(JsonCollectors.toJsonArray());

        var result = Json.createObjectBuilder()
                .add("total", total)
                .add("items", items)
                .build();

        return Response.ok(result).build();
    }

    @GET
    @Path("/{utskickId}")
    public Response find(
            @PathParam("kundnr") String kundnr,
            @PathParam("utskickId") String utskickId) {

        return utskick.find(kundnr, utskickId)
                .map(r -> Response.ok(r.toJSON()).build())
                .orElse(Response.status(NOT_FOUND).build());
    }
}
