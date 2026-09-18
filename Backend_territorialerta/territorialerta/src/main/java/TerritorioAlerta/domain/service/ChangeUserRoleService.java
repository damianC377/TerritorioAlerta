package TerritorioAlerta.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.model.Enums.Role;
import TerritorioAlerta.domain.port.UserPort;

@Service
public class ChangeUserRoleService {

    @Autowired
    private UserPort userPort;

    public void changeUserRole(User user, Role newRole) throws Exception {
        if (user == null) {
            throw new Exception("Usuario no encontrado.");
        }

        user.setRole(newRole);
        userPort.save(user);
    }
}
