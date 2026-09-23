package TerritorioAlerta.domain.port;

import TerritorioAlerta.domain.model.User;

/** Puerto de salida para consultar y persistir usuarios. */
public interface UserPort {
    
    /** Busca un usuario por su identificador o devuelve null si no existe. */
    User findById(Long id_user);

    /** Busca un usuario por email o devuelve null si no existe. */
    User findByEmail(String email);

    /** Guarda un usuario y devuelve su representación persistida. */
    User save(User user);
}
