package TerritorioAlerta.adapter.rest.mapper;

import org.springframework.stereotype.Component;

import TerritorioAlerta.adapter.in.builder.UserBuilder;
import TerritorioAlerta.adapter.rest.request.UserRequest;
import TerritorioAlerta.adapter.rest.response.UserResponse;
import TerritorioAlerta.domain.model.User; 

@Component
/** Convierte entre solicitudes/respuestas REST y usuarios de dominio. */
public class UserRestMapper {

    private final UserBuilder userBuilder;

    /** Construye el mapper con el builder de usuarios. */
    public UserRestMapper(UserBuilder userBuilder) {
        this.userBuilder = userBuilder;
    }

    /** Convierte una solicitud HTTP validada en un usuario de dominio. */
    public User toDomain(UserRequest req) throws Exception {
        return userBuilder.buildUser(
                req.getName(),
                req.getLastname(),
                req.getEmail(),
                req.getPassword(),
                req.getCommune(),
                req.getNeighborhood()
        );
    }

    /** Convierte un usuario de dominio en la respuesta pública del API. */
    public UserResponse toResponse(User user){

        UserResponse res = new UserResponse();
        res.setId_user(user.getId_user());
        res.setName(user.getName());
        res.setLastname(user.getLastname());
        res.setEmail(user.getEmail());
        res.setCommune(user.getCommune());
        res.setNeighborhood(user.getNeighborhood());
        res.setRole(user.getRole() != null ? user.getRole().name() : null);
        res.setCreation_date(user.getCreation_date());
        return res;
    }
}
