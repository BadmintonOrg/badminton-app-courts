package si.fri.rso.badmintonappcourts.services.beans;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import si.fri.rso.badmintonappcourts.lib.Court;
import si.fri.rso.badmintonappcourts.models.converters.CourtConverter;
import si.fri.rso.badmintonappcourts.models.entities.CourtEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;
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

    public Court getCourt(Integer id) {

        CourtEntity courtEntity = em.find(CourtEntity.class, id);

        if (courtEntity == null) {
            throw new NotFoundException();
        }

        Court cort = CourtConverter.toDto(courtEntity);

        return cort;
    }

    public Court createCourt(Court cort) {

        CourtEntity courtEntity = CourtConverter.toEntity(cort);

        try {
            beginTx();
            em.persist(courtEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        if (courtEntity.getId() == null) {
            throw new RuntimeException("Entity was not persisted");
        }

        return CourtConverter.toDto(courtEntity);
    }

    public boolean deleteCourt(Integer id) {

        CourtEntity cort = em.find(CourtEntity.class, id);

        if (cort != null) {
            try {
                beginTx();
                em.remove(cort);
                commitTx();
            }
            catch (Exception e) {
                rollbackTx();
            }
        }
        else {
            return false;
        }

        return true;
    }

    public Court putCourt(Integer id, Court cort) {

        CourtEntity c = em.find(CourtEntity.class, id);

        if (c == null) {
            return null;
        }

        CourtEntity updatedCourtEntity = CourtConverter.toEntity(cort);

        try {
            beginTx();
            updatedCourtEntity.setId(c.getId());
            updatedCourtEntity = em.merge(updatedCourtEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        return CourtConverter.toDto(updatedCourtEntity);
    }

    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}
