package TerritorioAlerta.infrastructure.persistence.mapper;

import TerritorioAlerta.infrastructure.persistence.entities.Accident_reportEntity;
import TerritorioAlerta.domain.model.Accident_report;
import TerritorioAlerta.domain.model.Enums.Status;
import TerritorioAlerta.domain.model.Enums.TypeReport;

/** Convierte reportes entre el modelo de dominio y la entidad JPA. */
public class AccidentReportMapper {

    /** Convierte un reporte de dominio a una entidad persistible. */
    public static Accident_reportEntity toEntity(Accident_report domain) {
        if (domain == null) {
            return null;
        }

        Accident_reportEntity entity = new Accident_reportEntity();
        entity.setId_accident_report(domain.getId_accident_report());
        entity.setId_user(domain.getId_user());
        entity.setDate(domain.getDate());
        entity.setCommune(domain.getCommune());
        entity.setNeighborhood(domain.getNeighborhood());
        entity.setAddress(domain.getAddress());
        entity.setImage(domain.getImage());
        entity.setDescription(domain.getDescription());
        entity.setCreation_date(domain.getCreation_date());

        if (domain.getType_report() != null) {
            entity.setType_report(domain.getType_report().name());
        }

        if (domain.getStatus() != null) {
            entity.setStatus(domain.getStatus().name());
        }

        return entity;
    }

    /** Convierte una entidad JPA a un reporte de dominio. */
    public static Accident_report toDomain(Accident_reportEntity entity) {
        if (entity == null) return null;

        Accident_report domain = new Accident_report();
        domain.setId_accident_report(entity.getId_accident_report());
        domain.setId_user(entity.getId_user());
        domain.setDate(entity.getDate());
        domain.setCommune(entity.getCommune());
        domain.setNeighborhood(entity.getNeighborhood());
        domain.setAddress(entity.getAddress());
        domain.setImage(entity.getImage());
        domain.setDescription(entity.getDescription());
        domain.setCreation_date(entity.getCreation_date());

        if (entity.getType_report() != null) {
            domain.setType_report(TypeReport.valueOf(entity.getType_report()));
        }
        if (entity.getStatus() != null) {
            domain.setStatus(Status.valueOf(entity.getStatus()));
        }

        return domain;
    }
}
