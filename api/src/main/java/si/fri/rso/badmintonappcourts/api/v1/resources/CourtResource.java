package si.fri.rso.badmintonappcourts.api.v1.resources;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.logs.cdi.Log;
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

@Log
@ApplicationScoped
@Path("/courts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOrigin(supportedMethods = "GET, POST, HEAD, DELETE, OPTIONS, PUT")
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
        log.info("Get all courts.");
        List<Court> courts = courtBean.getCourts(uriInfo);

        log.info("Returning courts.");
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

        log.info("Get info for court with id " + courtId);

        Court cort = courtBean.getCourt(courtId);

        if (cort == null) {
            log.info("No court found.");
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        log.info("Returning data for court with id " + courtId);
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

        log.info("Called method for new court");
        if (cort.getLocation() == null || cort.getNumber() == null ) {
            log.info("New court not added. Bad request.");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else {
            cort = courtBean.createCourt(cort);
        }

        log.info("New court added");
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

        log.info("Called method to delete court");
        boolean deleted = courtBean.deleteCourt(courtId);

        if (deleted) {
            log.info("Court not deleted. Bad request.");
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        else {
            log.info("Deleted court with id " + courtId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Operation(description = "Update data for a court.", summary = "Update court")
    @APIResponses({
            @APIResponse(
                    responseCode = "301",
                    description = "Court successfully updated."
            )
    })
    @PUT
    @Path("{courtId}")
    public Response putCourt(@Parameter(description = "Court ID.", required = true)
                                 @PathParam("courtId") Integer courtId,
                             @RequestBody(
                                     description = "DTO object with court data.",
                                     required = true,
                                     content = @Content(schema = @Schema(implementation = Court.class)))
                                     Court court){

        log.info("Called method to update court");
        court = courtBean.putCourt(courtId, court);

        if (court == null) {
            log.info("Court not updated. Bad request.");
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        log.info("Updated court with id " + courtId);
        return Response.status(Response.Status.NOT_MODIFIED).build();

    }

}
