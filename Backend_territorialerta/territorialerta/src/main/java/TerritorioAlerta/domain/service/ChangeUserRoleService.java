package TerritorioAlerta.domain.service;

import org.springframework.stereotype.Service;

import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.model.Enums.Role;
import TerritorioAlerta.domain.port.UserPort;

@Service
/** Servicio de dominio para modificar el rol de un usuario. */
public class ChangeUserRoleService {

    private final UserPort userPort;

    /** Construye el servicio con el puerto de usuarios requerido. */
    public ChangeUserRoleService(UserPort userPort) {
        this.userPort = userPort;
    }

    /** Asigna el nuevo rol y guarda el usuario, rechazando usuarios nulos. */
    public void changeUserRole(User user, Role newRole) throws Exception {
        if (user == null) {
            throw new Exception("Usuario no encontrado.");
        }

        user.setRole(newRole);
        userPort.save(user);
    }
}
