package TerritorioAlerta.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.port.UserPort;

@Service
public class FindByIdUserService {

    @Autowired
    private UserPort userPort;

    public User findByIdUser(Long userId) throws Exception {
        User user = userPort.findById(userId);

        if (user == null) {
            throw new Exception("Usuario con ID " + userId + " no encontrado.");
        }

        return user;
    }
}
