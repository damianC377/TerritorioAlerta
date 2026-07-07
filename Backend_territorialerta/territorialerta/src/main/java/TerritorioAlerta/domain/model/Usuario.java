package TerritorioAlerta.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    private Long id;
    private String nombre;
    private String correo;
    private String contrasena;
    private String rol;
    private Boolean activo;
    private LocalDateTime createdAt;
}
