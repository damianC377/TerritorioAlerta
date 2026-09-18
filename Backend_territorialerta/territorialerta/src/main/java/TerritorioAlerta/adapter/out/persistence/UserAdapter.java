package TerritorioAlerta.adapter.out.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.port.UserPort;
import TerritorioAlerta.infrastructure.persistence.entities.UserEntity;
import TerritorioAlerta.infrastructure.persistence.mapper.UserMapper;
import TerritorioAlerta.infrastructure.persistence.repository.UserRepository;

@Service
public class UserAdapter implements UserPort {
    
    @Autowired
    private UserRepository userRepository;

    @Override 
    public User findById(Long id_user) {
        UserEntity entity = userRepository.findById(id_user).orElse(null);
        return UserMapper.toDomain(entity);
    }

    @Override
    public User findByEmail(String email) {
        UserEntity entity = userRepository.findByEmail(email);
        return UserMapper.toDomain(entity);
    }

    @Override
    public User save(User user) {
        UserEntity entity = userRepository.save(UserMapper.toEntity(user));
        return UserMapper.toDomain(entity);
    }

}
