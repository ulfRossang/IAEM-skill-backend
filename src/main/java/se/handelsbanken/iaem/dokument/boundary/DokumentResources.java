package se.handelsbanken.iaem.dokument.boundary;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.stream.JsonCollectors;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import se.handelsbanken.iaem.dokument.control.Dokument;
import se.handelsbanken.iaem.dokument.entity.DokumentRow;

@Path("/kunder/{kundnr}/dokument")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class DokumentResources {

    @Inject
    Dokument dokument;

    @GET
    public Response list(
            @PathParam("kundnr") String kundnr,
            @QueryParam("forbindelse") String forbindelse,
            @QueryParam("datumFran") String datumFran,
            @QueryParam("datumTill") String datumTill) {

        var all = dokument.list(kundnr);

        var filtered = all.stream()
                .filter(d -> forbindelse == null || forbindelse.isBlank()
                        || forbindelse.equalsIgnoreCase(d.forbindelse()))
                .filter(d -> datumFran == null || datumFran.isBlank()
                        || d.dokumentdatum().compareTo(datumFran) >= 0)
                .filter(d -> datumTill == null || datumTill.isBlank()
                        || d.dokumentdatum().compareTo(datumTill) <= 0)
                .map(DokumentRow::toJSON)
                .collect(JsonCollectors.toJsonArray());

        return Response.ok(filtered).build();
    }
}
