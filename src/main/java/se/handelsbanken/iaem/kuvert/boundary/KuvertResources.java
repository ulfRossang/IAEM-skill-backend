package se.handelsbanken.iaem.kuvert.boundary;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import se.handelsbanken.iaem.kuvert.control.Kuvert;

import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

@Path("/kuvert")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class KuvertResources {

    @Inject
    Kuvert kuvert;

    @GET
    @Path("/{kuvertId}")
    public Response find(@PathParam("kuvertId") String kuvertId) {
        return kuvert.find(kuvertId)
                .map(r -> Response.ok(r.toJSON()).build())
                .orElse(Response.status(NOT_FOUND).build());
    }
}
