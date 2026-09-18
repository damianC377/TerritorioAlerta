package TerritorioAlerta.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.port.UserPort;

@Service
public class CreateUserService {

    @Autowired
    private UserPort userPort;

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
