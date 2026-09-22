package TerritorioAlerta.domain.service;

import org.springframework.stereotype.Service;

import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.port.UserPort;

@Service
/** Servicio de dominio que aplica las reglas para crear usuarios. */
public class CreateUserService {

    private final UserPort userPort;

    /** Construye el servicio con el puerto de usuarios requerido. */
    public CreateUserService(UserPort userPort) {
        this.userPort = userPort;
    }

    /** Verifica la unicidad del ID y email, y persiste el usuario válido. */
    public User createUser(User user) throws Exception {
        if (user.getId_user() != null && userPort.findById(user.getId_user()) != null) {
            throw new Exception("El usuario con ID " + user.getId_user() + " ya existe.");
        }

        if (userPort.findByEmail(user.getEmail()) != null) {
            throw new Exception("Usuario con email " + user.getEmail() + " ya existe.");
        }

        return userPort.save(user);
    }
}
