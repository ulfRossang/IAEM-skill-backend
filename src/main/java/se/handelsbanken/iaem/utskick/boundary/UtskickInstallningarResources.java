package se.handelsbanken.iaem.utskick.boundary;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonCollectors;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import se.handelsbanken.iaem.utskick.control.Utskick;
import se.handelsbanken.iaem.utskick.entity.UtskickInstallningRow;

import java.util.List;

@Path("/kunder/{kundnr}/utskick-installningar")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class UtskickInstallningarResources {

    @Inject
    Utskick utskick;

    @GET
    public Response list(@PathParam("kundnr") String kundnr) {
        var items = utskick.listInstallningar(kundnr)
                .stream()
                .map(UtskickInstallningRow::toJSON)
                .collect(JsonCollectors.toJsonArray());
        return Response.ok(items).build();
    }

    @PUT
    @Transactional
    public Response save(
            @PathParam("kundnr") String kundnr,
            JsonArray body) {

        List<UtskickInstallningRow> settings = body.stream()
                .map(v -> v.asJsonObject())
                .map(o -> new UtskickInstallningRow(
                        o.getString("kategori", ""),
                        o.getString("avser", ""),
                        o.getString("forbindelse", ""),
                        o.getBoolean("papper", false),
                        o.getBoolean("internet", false)))
                .toList();

        utskick.saveInstallningar(kundnr, settings);
        return Response.noContent().build();
    }
}
