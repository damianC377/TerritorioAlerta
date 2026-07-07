package TerritorioAlerta.domain.port;

import TerritorioAlerta.domain.model.Reporte;

import java.util.List;
import java.util.Optional;

public interface ReportePort {

    List<Reporte> listarTodos();
    Optional<Reporte> buscarPorId(Long id);
    List<Reporte> buscarPorUsuario(Long idUsuario);
    List<Reporte> buscarPorCategoria(Long idCategoria);
    Reporte guardar(Reporte reporte);
    void eliminar(Long id);
}
