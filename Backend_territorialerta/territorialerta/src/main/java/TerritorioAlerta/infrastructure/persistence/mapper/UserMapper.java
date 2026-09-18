package TerritorioAlerta.infrastructure.persistence.mapper;

import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.model.Enums.Role;
import TerritorioAlerta.infrastructure.persistence.entities.UserEntity;

public class UserMapper {

    public static UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setId_user(user.getId_user());
        entity.setName(user.getName());
        entity.setLastname(user.getLastname());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPassword());
        entity.setCommune(user.getCommune());
        entity.setNeighborhood(user.getNeighborhood());
        entity.setCreation_date(user.getCreation_date());

        if (user.getRole() != null) {
            entity.setRole(user.getRole().name());
        }

        return entity;
    }

    public static User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        User user = new User();
        user.setId_user(entity.getId_user());
        user.setName(entity.getName());
        user.setLastname(entity.getLastname());
        user.setEmail(entity.getEmail());
        user.setPassword(entity.getPassword());
        user.setCommune(entity.getCommune());
        user.setNeighborhood(entity.getNeighborhood());
        user.setCreation_date(entity.getCreation_date());

        if (entity.getRole() != null) {
            user.setRole(Role.valueOf(entity.getRole()));
        }

        return user;
    }
}
