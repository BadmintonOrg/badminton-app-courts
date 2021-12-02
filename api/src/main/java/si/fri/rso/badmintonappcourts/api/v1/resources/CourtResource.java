package si.fri.rso.badmintonappcourts.api.v1.resources;

import si.fri.rso.badmintonappcourts.lib.Court;
import si.fri.rso.badmintonappcourts.services.beans.CourtBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/courts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CourtResource {

    private Logger log = Logger.getLogger(CourtResource.class.getName());

    @Inject
    private CourtBean courtBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getCourts() {
        List<Court> courts = courtBean.getCourts(uriInfo);

        return Response.status(Response.Status.OK).entity(courts).build();
    }

   /*@GET
    @Path("/{courtId}")
    public Response getImageMetadata(@PathParam("courtId") Integer courtId) {

        Court cort = courtBean.getCourt(courtId);

        if (cort == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(cort).build();
    }*/



}
