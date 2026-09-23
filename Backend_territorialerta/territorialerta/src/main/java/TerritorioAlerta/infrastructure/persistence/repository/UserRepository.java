package TerritorioAlerta.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import TerritorioAlerta.infrastructure.persistence.entities.UserEntity;

@Repository
/** Repositorio JPA para entidades de usuario. */
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    /** Busca la entidad de usuario asociada al email indicado. */
    UserEntity findByEmail(String email);
}
