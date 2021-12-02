package si.fri.rso.badmintonappcourts.services.beans;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import si.fri.rso.badmintonappcourts.lib.Court;
import si.fri.rso.badmintonappcourts.models.converters.CourtConverter;
import si.fri.rso.badmintonappcourts.models.entities.CourtEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequestScoped
public class CourtBean {

    private Logger log = Logger.getLogger(CourtBean.class.getName());

    @Inject
    private EntityManager em;

    public List<Court> getCourts(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, CourtEntity.class, queryParameters).stream()
                .map(CourtConverter::toDto).collect(Collectors.toList());
    }

    //TO-DO
}
