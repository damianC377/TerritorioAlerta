package TerritorioAlerta.domain.service.integrals.springtest;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.service.CreateUserService;
import TerritorioAlerta.domain.service.FindByIdUserService;

@SpringBootTest
@Transactional
public final class CreateUserServiceSpringTest extends SpringIntegrationTestSupport {

    // Ejecuta esta clase como una prueba independiente desde Java.
    public static void main(String[] args) {
        // Inicia Spring y revierte los datos al finalizar este caso.
        runCheck("CreateUserService", environment -> {
            // Obtiene el servicio real que crea usuarios.
            CreateUserService createService = environment.getBean(CreateUserService.class);
            // Obtiene el servicio real que vuelve a consultar usuarios.
            FindByIdUserService findService = environment.getBean(FindByIdUserService.class);

            // Crea un usuario con valores válidos y un correo irrepetible.
            User savedUser = createService.createUser(newUser());
            // Comprueba que la base de datos haya asignado un identificador.
            check(savedUser.getId_user() != null, "No se generó el ID del usuario.");

            // Fuerza el INSERT y limpia las entidades en memoria.
            environment.flushAndClear();
            // Vuelve a leer el usuario desde la persistencia.
            User foundUser = findService.findByIdUser(savedUser.getId_user());
            // Comprueba que el correo guardado coincida con el original.
            check(savedUser.getEmail().equals(foundUser.getEmail()), "El correo persistido no coincide.");
        });
    }
}
