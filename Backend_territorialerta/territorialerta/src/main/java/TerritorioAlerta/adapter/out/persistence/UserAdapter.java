package TerritorioAlerta.adapter.out.persistence;

import org.springframework.stereotype.Service;

import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.port.UserPort;
import TerritorioAlerta.infrastructure.persistence.entities.UserEntity;
import TerritorioAlerta.infrastructure.persistence.mapper.UserMapper;
import TerritorioAlerta.infrastructure.persistence.repository.UserRepository;

@Service
/** Adapta las operaciones de usuarios del dominio al repositorio JPA. */
public class UserAdapter implements UserPort {
    
    private final UserRepository userRepository;

    /** Construye el adapter con el repositorio JPA de usuarios. */
    public UserAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override 
    /** Busca un usuario por ID y convierte la entidad encontrada al dominio. */
    public User findById(Long id_user) {
        UserEntity entity = userRepository.findById(id_user).orElse(null);
        return UserMapper.toDomain(entity);
    }

    @Override
    /** Busca un usuario por email y convierte la entidad encontrada al dominio. */
    public User findByEmail(String email) {
        UserEntity entity = userRepository.findByEmail(email);
        return UserMapper.toDomain(entity);
    }

    @Override
    /** Persiste un usuario de dominio y devuelve el usuario resultante. */
    public User save(User user) {
        UserEntity entity = userRepository.save(UserMapper.toEntity(user));
        return UserMapper.toDomain(entity);
    }

}
