package TerritorioAlerta.domain.service;

import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.port.UserPort;

public class AdminService {

    private UserPort userPort;
    
    // Cambiar rol de usuario
    public void changeUserRole(User user, String newRole) throws Exception {
        if (user == null) {
            throw new Exception("Usuario no encontrado.");
        }

        user.setRole(newRole);
        
        userPort.save(user);
    }
}
