package TerritorioAlerta.domain.service.integrals.springtest;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import TerritorioAlerta.domain.model.Enums.Role;
import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.service.ChangeUserRoleService;
import TerritorioAlerta.domain.service.FindByIdUserService;

@SpringBootTest
@Transactional
public final class ChangeUserRoleServiceSpringTest extends SpringIntegrationTestSupport {

    // Ejecuta esta clase como una prueba independiente desde Java.
    public static void main(String[] args) {
        // Inicia Spring y revierte los datos al finalizar este caso.
        runCheck("ChangeUserRoleService", environment -> {
            // Crea un usuario real cuyo rol se actualizará.
            User user = environment.persistUser();
            // Obtiene el servicio real dedicado al cambio de rol.
            ChangeUserRoleService changeRoleService =
                    environment.getBean(ChangeUserRoleService.class);
            // Obtiene el servicio para volver a leer el usuario actualizado.
            FindByIdUserService findUserService = environment.getBean(FindByIdUserService.class);

            // Asigna al usuario el rol Analyst mediante el servicio bajo prueba.
            changeRoleService.changeUserRole(user, Role.Analyst);
            // Fuerza la escritura del nuevo rol y limpia la caché JPA.
            environment.flushAndClear();

            // Recupera el usuario actualizado desde la persistencia.
            User updatedUser = findUserService.findByIdUser(user.getId_user());
            // Comprueba que la base de datos contiene el rol Analyst.
            check(updatedUser.getRole() == Role.Analyst,
                    "ChangeUserRoleService no persistió el rol Analyst.");
        });
    }
}
