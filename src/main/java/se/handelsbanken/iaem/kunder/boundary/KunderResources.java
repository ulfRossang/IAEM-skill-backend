package se.handelsbanken.iaem.kunder.boundary;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import se.handelsbanken.iaem.kunder.control.Kunder;

import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

@Path("/kunder")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class KunderResources {

    @Inject
    Kunder kunder;

    @GET
    @Path("/{kundnr}")
    public Response getKund(
            @PathParam("kundnr") String kundnr,
            @QueryParam("land") String land) {

        var kund = kunder.find(kundnr);
        if (kund.isEmpty()) return Response.status(NOT_FOUND).build();

        var k = kund.get();
        if (land != null && !land.isBlank() && !land.equalsIgnoreCase(k.land())) {
            return Response.status(NOT_FOUND).build();
        }

        return Response.ok(k.toJSON()).build();
    }
}
