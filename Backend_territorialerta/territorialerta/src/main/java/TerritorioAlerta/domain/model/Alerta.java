package TerritorioAlerta.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Alerta {

    private Long id;
    private String titulo;
    private String descripcion;
    private String tipo;
    private Boolean activa;
    private Long idUsuario;
    private LocalDateTime createdAt;
}
