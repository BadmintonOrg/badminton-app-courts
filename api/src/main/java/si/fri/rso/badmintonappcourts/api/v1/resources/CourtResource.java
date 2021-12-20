package si.fri.rso.badmintonappcourts.api.v1.resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import si.fri.rso.badmintonappcourts.lib.Court;
import si.fri.rso.badmintonappcourts.services.beans.CourtBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
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

    @Operation(description = "Get all courts in a list", summary = "Get all courts")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "List of courts",
                    content = @Content(schema = @Schema(implementation = Court.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Number of objects in list")}
            )})
    @GET
    public Response getCourts() {
        List<Court> courts = courtBean.getCourts(uriInfo);

        return Response.status(Response.Status.OK).entity(courts).build();
    }

    @Operation(description = "Get data for a court.", summary = "Get data for a court")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Court",
                    content = @Content(
                            schema = @Schema(implementation = Court.class))
            )})
    @GET
    @Path("/{courtId}")
    public Response getCourt(@Parameter(description = "Court ID.", required = true)
                                 @PathParam("courtId") Integer courtId) {

        Court cort = courtBean.getCourt(courtId);

        if (cort == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(cort).build();
    }

    @Operation(description = "Add court.", summary = "Add court")
    @APIResponses({
            @APIResponse(responseCode = "201",
                    description = "Court successfully added."
            ),
            @APIResponse(responseCode = "405", description = "Validation error .")
    })
    @POST
    public Response createCourt(@RequestBody(
            description = "DTO object with court data.",
            required = true, content = @Content(
            schema = @Schema(implementation = Court.class))) Court cort) {

        if (cort.getLocation() == null || cort.getNumber() == null ) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else {
            cort = courtBean.createCourt(cort);
        }

        return Response.status(Response.Status.CONFLICT).entity(cort).build();

    }

    @Operation(description = "Delete court.", summary = "Delete court")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Court successfully deleted."
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Not found."
            )
    })
    @DELETE
    @Path("{courtId}")
    public Response deleteCourt(@Parameter(description = "Court ID.", required = true)
                                    @PathParam("courtId") Integer courtId){

        boolean deleted = courtBean.deleteCourt(courtId);

        if (deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Operation(description = "Update data for a court.", summary = "Update court")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Court successfully updated."
            )
    })
    @PUT
    @Path("{courtId}")
    public Response putCourt(@Parameter(description = "Court ID.", required = true)
                                 @PathParam("courtId") Integer courtId,
                             @RequestBody(
                                     description = "DTO object with court data.",
                                     required = true, content = @Content(
                                     schema = @Schema(implementation = Court.class)))
                                     Court court){

        court = courtBean.putCourt(courtId, court);

        if (court == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.NOT_MODIFIED).build();

    }

}
