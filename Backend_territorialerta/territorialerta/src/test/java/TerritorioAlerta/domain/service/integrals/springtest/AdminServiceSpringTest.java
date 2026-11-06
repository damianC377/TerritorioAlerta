package TerritorioAlerta.domain.service.integrals.springtest;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import TerritorioAlerta.domain.model.Enums.Role;
import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.service.AdminService;
import TerritorioAlerta.domain.service.FindByIdUserService;

@SpringBootTest
@Transactional
public final class AdminServiceSpringTest extends SpringIntegrationTestSupport {

    // Ejecuta esta clase como una prueba independiente desde Java.
    public static void main(String[] args) {
        // Inicia Spring y revierte los datos al finalizar este caso.
        runCheck("AdminService", environment -> {
            // Crea un usuario real cuyo rol se actualizará.
            User user = environment.persistUser();
            // Obtiene el servicio administrativo real.
            AdminService adminService = environment.getBean(AdminService.class);
            // Obtiene el servicio para volver a leer el usuario actualizado.
            FindByIdUserService findUserService = environment.getBean(FindByIdUserService.class);

            // Cambia el rol del usuario a Jal mediante la operación administrativa.
            adminService.changeUserRole(user, Role.Jal);
            // Fuerza la escritura del nuevo rol y limpia la caché JPA.
            environment.flushAndClear();

            // Recupera el usuario actualizado desde la persistencia.
            User updatedUser = findUserService.findByIdUser(user.getId_user());
            // Comprueba que la base de datos contiene el rol Jal.
            check(updatedUser.getRole() == Role.Jal, "AdminService no persistió el rol Jal.");
        });
    }
}
