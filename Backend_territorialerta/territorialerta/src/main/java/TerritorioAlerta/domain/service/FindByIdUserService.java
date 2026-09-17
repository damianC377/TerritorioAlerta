package TerritorioAlerta.domain.service;

import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.port.UserPort;

public class FindByIdUserService {

    private final UserPort userPort;

    public FindByIdUserService(UserPort userPort) {
        this.userPort = userPort;
    }

    public User findByIdUser(Long userId) throws Exception {
        User user = userPort.findById(userId);

        if (user == null) {
            throw new Exception("Usuario con ID " + userId + " no encontrado.");
        }

        return user;
    }
}
