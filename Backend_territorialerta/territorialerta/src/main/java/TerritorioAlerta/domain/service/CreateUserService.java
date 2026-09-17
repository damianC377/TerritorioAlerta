package TerritorioAlerta.domain.service;

import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.port.UserPort;

public class CreateUserService {

    private final UserPort userPort;

    public CreateUserService(UserPort userPort) {
        this.userPort = userPort;
    }

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
