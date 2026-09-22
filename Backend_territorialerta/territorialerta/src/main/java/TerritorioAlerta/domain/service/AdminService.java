package TerritorioAlerta.domain.service;

import org.springframework.stereotype.Service;

import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.model.Enums.Role;
import TerritorioAlerta.domain.port.UserPort;

@Service
/** Servicio de dominio para operaciones administrativas sobre usuarios. */
public class AdminService {

    private final UserPort userPort;

    /** Construye el servicio con el puerto de usuarios requerido. */
    public AdminService(UserPort userPort) {
        this.userPort = userPort;
    }
    
    // Cambiar rol de usuario
    /** Cambia el rol del usuario y persiste el cambio si el usuario existe. */
    public void changeUserRole(User user, Role newRole) throws Exception {
        if (user == null) {
            throw new Exception("Usuario no encontrado.");
        }

        user.setRole(newRole);
        
        userPort.save(user);
    }
}
