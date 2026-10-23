package TerritorioAlerta.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.model.Enums.Role;

/** Pruebas JUnit del cambio de rol usando los servicios y PostgreSQL reales. */
// Inicia Spring para utilizar el servicio y el repositorio reales.
@SpringBootTest
// Revierte el usuario y el cambio al concluir cada prueba.
@Transactional
class ChangeUserRoleServiceTest {

    // Inyecta el servicio de cambio de rol bajo prueba.
    @Autowired
    private ChangeUserRoleService changeUserRoleService;

    // Inyecta el servicio real que crea los usuarios de prueba.
    @Autowired
    private CreateUserService createUserService;

    // Inyecta el servicio de consulta para verificar el cambio persistido.
    @Autowired
    private FindByIdUserService findByIdUserService;

    // Comprueba que el servicio guarda un nuevo rol y que se puede leer después.
    @Test
    void cambiarRol_usuarioPersistido_actualizaRolAnalyst() throws Exception {
        // Inserta el usuario inicial en PostgreSQL.
        User saved = createUserService.createUser(newUser());
        // Cambia el rol usando el servicio real.
        changeUserRoleService.changeUserRole(saved, Role.Analyst);
        // Vuelve a consultar el usuario desde la base de datos.
        User updated = findByIdUserService.findByIdUser(saved.getId_user());
        // Comprueba que el nuevo rol quedó persistido.
        assertEquals(Role.Analyst, updated.getRole());
        System.out.println("JUnit: rol Analyst asignado satisfactoriamente.");
    }

    // Comprueba que también persiste correctamente el rol de administrador.
    @Test
    void cambiarRol_usuarioPersistido_actualizaRolAdmin() throws Exception {
        // Inserta un usuario real para modificarlo.
        User saved = createUserService.createUser(newUser());
        // Asigna el rol de administrador y guarda el cambio.
        changeUserRoleService.changeUserRole(saved, Role.Admin);
        // Lee otra vez el usuario a través de su servicio de búsqueda.
        User updated = findByIdUserService.findByIdUser(saved.getId_user());
        // Comprueba que el rol recuperado es el de administrador.
        assertEquals(Role.Admin, updated.getRole());
        System.out.println("JUnit: rol Admin asignado satisfactoriamente.");
    }

    // Comprueba el camino negativo cuando no se proporciona un usuario.
    @Test
    void cambiarRol_usuarioNulo_lanzaExcepcion() {
        // Confirma que el servicio rechaza el usuario nulo.
        assertThrows(Exception.class, () -> changeUserRoleService.changeUserRole(null, Role.Admin));
        System.out.println("JUnit: no se pudo cambiar el rol porque el usuario es nulo.");
    }

    // Prepara un usuario completo con un email único para la base de datos.
    private User newUser() {
        // Crea el objeto que se insertará.
        User user = new User();
        // Asigna un nombre de prueba.
        user.setName("JUnit");
        // Asigna un apellido de prueba.
        user.setLastname("Integration");
        // Genera un email irrepetible.
        user.setEmail("junit-" + UUID.randomUUID() + "@example.test");
        // Completa la contraseña.
        user.setPassword("test-password");
        // Completa la comuna.
        user.setCommune("Test commune");
        // Completa el barrio.
        user.setNeighborhood("Test neighborhood");
        // Asigna un rol inicial válido.
        user.setRole(Role.User);
        // Completa la fecha de creación.
        user.setCreation_date(LocalDateTime.now());
        // Devuelve el usuario preparado.
        return user;
    }
}