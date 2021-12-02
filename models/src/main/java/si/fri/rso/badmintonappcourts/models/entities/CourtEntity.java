package si.fri.rso.badmintonappcourts.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "badminton_courts")
@NamedQueries(value =
        {
                @NamedQuery(name = "CourtEntity.getAll",
                        query = "SELECT oe FROM CourtEntity oe")
        })
public class CourtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "number")
    private Integer number;

    @Column(name = "location")
    private String location;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer name) {
        this.number = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}