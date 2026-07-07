package TerritorioAlerta.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Categoria {

    private Long id;
    private String nombre;
    private String descripcion;
    private String icono;
    private String prioridadDefault;
    private LocalDateTime createdAt;
    
}
