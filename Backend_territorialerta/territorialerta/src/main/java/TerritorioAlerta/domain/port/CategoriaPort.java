package TerritorioAlerta.domain.port;

import TerritorioAlerta.domain.model.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaPort {
    List<Categoria> listarTodas();
    Optional<Categoria> buscarPorId(Long id);
    Categoria guardar(Categoria categoria);
    void eliminar(Long id);
}
