package TerritorioAlerta.domain.Service;

import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.port.UserPort;

public class UserService {

    private UserPort userPort;

    public User createUser(User user) throws Exception{
        
        if (user.getId_user() != null && userPort.findById(user.getId_user()) != null) {
            throw new Exception("El usuario con ID " + user.getId_user() + " ya existe.");
        }

        if (userPort.findByEmail(user.getEmail()) != null) {
            throw new Exception("Usuario con email " + user.getEmail() + " ya existe.");
        }

        
        return userPort.save(user);
    }

    public User findByIdUser(Long userId) throws Exception{
        User user = userPort.findById(userId);

        if (user == null) {
            throw new Exception("Usuario con ID " + userId + " no encontrado.");
        }

        return user;
    }
}
