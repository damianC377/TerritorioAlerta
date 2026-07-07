package TerritorioAlerta.domain.port;

import TerritorioAlerta.domain.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioPort {
    
    List<Usuario> listarTodos();
    Optional<Usuario> buscarPorId(Long id);
    Optional<Usuario> buscarPorCorreo(String correo);
    Usuario guardar(Usuario usuario);
    void eliminar(Long id);

}
