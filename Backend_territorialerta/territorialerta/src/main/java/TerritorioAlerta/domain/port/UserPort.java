package TerritorioAlerta.domain.port;

import TerritorioAlerta.domain.model.User;

public interface UserPort {
    
    User findById(Long id_user);

    User findByEmail(String email);

    User save(User user);
}
