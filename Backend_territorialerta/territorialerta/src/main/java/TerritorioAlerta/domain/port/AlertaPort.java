package TerritorioAlerta.domain.port;

import TerritorioAlerta.domain.model.Alerta;

import java.util.List;
import java.util.Optional;

public interface AlertaPort {

    List<Alerta> listarTodas();
    List<Alerta> listarActivas();
    Optional<Alerta> buscarPorId(Long id);
    Alerta guardar(Alerta alerta);
    void eliminar(Long id);
}
