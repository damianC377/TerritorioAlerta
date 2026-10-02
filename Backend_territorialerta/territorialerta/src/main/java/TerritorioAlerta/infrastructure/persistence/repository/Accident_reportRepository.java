package TerritorioAlerta.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import TerritorioAlerta.infrastructure.persistence.entities.Accident_reportEntity;

@Repository
/** Repositorio JPA para entidades de reportes de accidentes. */
public interface Accident_reportRepository extends JpaRepository<Accident_reportEntity, Long> {
    /** Busca todas las entidades de reporte asociadas al usuario indicado. */
    List<Accident_reportEntity> findAllByidUser(Long idUser);
}
