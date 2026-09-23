package TerritorioAlerta.domain.service;

import org.springframework.stereotype.Service;

import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.port.UserPort;

@Service
/** Servicio de dominio para consultar un usuario por su identificador. */
public class FindByIdUserService {

    private final UserPort userPort;

    /** Construye el servicio con el puerto de usuarios requerido. */
    public FindByIdUserService(UserPort userPort) {
        this.userPort = userPort;
    }

    /** Busca un usuario y falla si el identificador no corresponde a uno existente. */
    public User findByIdUser(Long userId) throws Exception {
        User user = userPort.findById(userId);

        if (user == null) {
            throw new Exception("Usuario con ID " + userId + " no encontrado.");
        }

        return user;
    }
}
