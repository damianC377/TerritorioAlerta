package TerritorioAlerta.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import TerritorioAlerta.infrastructure.persistence.entities.Accident_reportEntity;

@Repository
public interface Accident_reportRepository extends JpaRepository<Accident_reportEntity, Long> {
    
    List<Accident_reportEntity> findAllByIdUser(Long id_user);
}
