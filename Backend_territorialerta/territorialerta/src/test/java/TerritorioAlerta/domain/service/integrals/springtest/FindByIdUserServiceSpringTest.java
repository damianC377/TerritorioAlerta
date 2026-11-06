package TerritorioAlerta.domain.service.integrals.springtest;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.service.FindByIdUserService;

@SpringBootTest
@Transactional
public final class FindByIdUserServiceSpringTest extends SpringIntegrationTestSupport {

    // Ejecuta esta clase como una prueba independiente desde Java.
    public static void main(String[] args) {
        // Inicia Spring y revierte los datos al finalizar este caso.
        runCheck("FindByIdUserService", environment -> {
            // Crea un usuario persistido que servirá como dato de búsqueda.
            User savedUser = environment.persistUser();
            // Fuerza su escritura y limpia la caché JPA.
            environment.flushAndClear();

            // Obtiene el servicio real que busca usuarios por ID.
            FindByIdUserService service = environment.getBean(FindByIdUserService.class);
            // Consulta el usuario usando el ID generado por la base.
            User foundUser = service.findByIdUser(savedUser.getId_user());
            // Comprueba que el resultado corresponde al usuario solicitado.
            check(savedUser.getId_user().equals(foundUser.getId_user()),
                    "La búsqueda no devolvió el usuario solicitado.");
        });
    }
}
