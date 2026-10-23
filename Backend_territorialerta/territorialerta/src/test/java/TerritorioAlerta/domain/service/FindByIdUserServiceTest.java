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

/** Pruebas JUnit del servicio de búsqueda conectadas a PostgreSQL. */
// Carga la aplicación y sus repositorios reales para estas pruebas.
@SpringBootTest
// Revierte los usuarios creados por cada método de prueba.
@Transactional
class FindByIdUserServiceTest {

    // Inyecta el servicio de búsqueda que se quiere probar.
    @Autowired
    private FindByIdUserService findByIdUserService;

    // Inyecta el servicio que guarda usuarios de prueba reales.
    @Autowired
    private CreateUserService createUserService;

    // Comprueba que el servicio devuelve el usuario guardado con su ID.
    @Test
    void buscarUsuario_idExistente_devuelveUsuario() throws Exception {
        // Inserta un usuario usando PostgreSQL.
        User saved = createUserService.createUser(newUser());
        // Busca el usuario con la clave generada por la base de datos.
        User found = findByIdUserService.findByIdUser(saved.getId_user());
        // Comprueba que se recuperó la misma clave.
        assertEquals(saved.getId_user(), found.getId_user());
        // Comprueba que también se recuperó el email persistido.
        assertEquals(saved.getEmail(), found.getEmail());
        System.out.println("JUnit: usuario encontrado satisfactoriamente con ID " + found.getId_user() + ".");
    }

    // Comprueba que puede recuperar correctamente otro usuario guardado.
    @Test
    void buscarUsuario_otroIdExistente_devuelveUsuarioCorrecto() throws Exception {
        // Guarda un primer usuario para que haya más de un registro de prueba.
        createUserService.createUser(newUser());
        // Guarda el usuario que luego se buscará.
        User expected = createUserService.createUser(newUser());
        // Recupera el usuario esperado por su ID autogenerado.
        User found = findByIdUserService.findByIdUser(expected.getId_user());
        // Comprueba que el servicio encontró el ID correcto.
        assertEquals(expected.getId_user(), found.getId_user());
        // Comprueba que encontró el email correcto.
        assertEquals(expected.getEmail(), found.getEmail());
        System.out.println("JUnit: se encontró correctamente el segundo usuario.");
    }

    // Comprueba el caso negativo para una clave que no existe.
    @Test
    void buscarUsuario_idInexistente_lanzaExcepcion() {
        // Confirma que el servicio lanza una excepción al no encontrar el ID.
        assertThrows(Exception.class, () -> findByIdUserService.findByIdUser(Long.MAX_VALUE));
        System.out.println("JUnit: no se pudo encontrar el usuario solicitado.");
    }

    // Prepara un usuario realista con email único para guardar en la base.
    private User newUser() {
        // Crea el objeto de usuario vacío.
        User user = new User();
        // Define el nombre para la fila de prueba.
        user.setName("JUnit");
        // Define el apellido para la fila de prueba.
        user.setLastname("Integration");
        // Genera un email único para evitar restricciones por duplicado.
        user.setEmail("junit-" + UUID.randomUUID() + "@example.test");
        // Completa la contraseña requerida por el modelo.
        user.setPassword("test-password");
        // Define la comuna del usuario.
        user.setCommune("Test commune");
        // Define el barrio del usuario.
        user.setNeighborhood("Test neighborhood");
        // Asigna un rol permitido por la aplicación.
        user.setRole(Role.User);
        // Define la fecha de creación.
        user.setCreation_date(LocalDateTime.now());
        // Devuelve el objeto listo para ser persistido.
        return user;
    }
}