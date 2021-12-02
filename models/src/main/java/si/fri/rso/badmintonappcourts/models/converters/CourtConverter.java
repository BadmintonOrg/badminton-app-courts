package si.fri.rso.badmintonappcourts.models.converters;

import si.fri.rso.badmintonappcourts.lib.Court;
import si.fri.rso.badmintonappcourts.models.entities.CourtEntity;

public class CourtConverter {

    public static Court toDto(CourtEntity entity) {

        Court dto = new Court();
        dto.setLocation(entity.getLocation());
        dto.setNumber(entity.getNumber());
        dto.setId(entity.getId());

        return dto;

    }

    public static CourtEntity toEntity(Court dto) {

        CourtEntity entity = new CourtEntity();
        entity.setId(dto.getId());
        entity.setNumber(dto.getNumber());
        entity.setLocation(dto.getLocation());
        return entity;

    }

}