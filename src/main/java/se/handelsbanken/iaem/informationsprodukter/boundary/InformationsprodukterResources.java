package se.handelsbanken.iaem.informationsprodukter.boundary;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonCollectors;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import se.handelsbanken.iaem.informationsprodukter.control.Informationsprodukter;
import se.handelsbanken.iaem.informationsprodukter.entity.InformationsproduktInput;
import se.handelsbanken.iaem.informationsprodukter.entity.InformationsproduktRow;

import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

@Path("/informationsprodukter")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class InformationsprodukterResources {

    @Inject
    Informationsprodukter informationsprodukter;

    @GET
    public Response list(@QueryParam("land") String land) {
        var items = informationsprodukter.list(land)
                .stream()
                .map(InformationsproduktRow::toJSON)
                .collect(JsonCollectors.toJsonArray());
        return Response.ok(items).build();
    }

    @POST
    @Transactional
    public Response create(JsonObject body) {
        var input = InformationsproduktInput.fromJSON(body);
        var created = informationsprodukter.create(input);
        return Response.status(Response.Status.CREATED).entity(created.toJSON()).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(
            @PathParam("id") String id,
            JsonObject body) {
        var input = InformationsproduktInput.fromJSON(body);
        return informationsprodukter.update(id, input)
                .map(r -> Response.ok(r.toJSON()).build())
                .orElse(Response.status(NOT_FOUND).build());
    }
}
