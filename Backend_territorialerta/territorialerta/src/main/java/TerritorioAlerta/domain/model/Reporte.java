package TerritorioAlerta.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reporte {

    private Long id;
    private String titulo;
    private String descripcion;
    private String ubicacion;
    private String estado;
    private Long idUsuario;
    private Long idCategoria;
    private LocalDateTime createdAt;
}
