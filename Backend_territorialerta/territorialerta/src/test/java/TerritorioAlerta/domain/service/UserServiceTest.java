package TerritorioAlerta.domain.service;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.model.Enums.Role;

/** Pruebas JUnit que crean usuarios usando el servicio y PostgreSQL reales. */
// Carga el contexto real para conectar servicios, adaptadores y repositorios.
@SpringBootTest
// Revierte los cambios de cada prueba al finalizar su ejecución.
@Transactional
class UserServiceTest {

    // Inyecta el servicio real de creación desde el contexto de Spring.
    @Autowired
    private CreateUserService userService;

    // Verifica que el usuario se guarda y PostgreSQL genera su identificador.
    @Test
    void crearUsuario_datosValidos_seGuardaCorrectamente() throws Exception {
        // Prepara un usuario nuevo con un email único para esta prueba.
        User user = newUser();
        // Ejecuta la creación a través del servicio conectado a PostgreSQL.
        User saved = userService.createUser(user);
        // Confirma que la base de datos generó el ID autoincremental.
        assertNotNull(saved.getId_user());
        // Confirma que se persistió el email enviado.
        assertEquals(user.getEmail(), saved.getEmail());
        System.out.println("JUnit: usuario creado satisfactoriamente.");
    }

    // Verifica que dos usuarios válidos reciben IDs distintos de PostgreSQL.
    @Test
    void crearUsuario_dosUsuariosValidos_asignaIdsDistintos() throws Exception {
        // Guarda el primer usuario en la base de datos.
        User first = userService.createUser(newUser());
        // Guarda un segundo usuario con email diferente.
        User second = userService.createUser(newUser());
        // Comprueba que el primer guardado recibió ID.
        assertNotNull(first.getId_user());
        // Comprueba que el segundo guardado recibió ID.
        assertNotNull(second.getId_user());
        // Comprueba que la base de datos no reutilizó el mismo ID.
        assertNotEquals(first.getId_user(), second.getId_user());
        System.out.println("JUnit: dos usuarios creados satisfactoriamente.");
    }

    // Verifica que el servicio rechaza el email duplicado.
    @Test
    void crearUsuario_emailDuplicado_lanzaExcepcion() throws Exception {
        // Crea el primer usuario para ocupar su email en la base de datos.
        User original = userService.createUser(newUser());
        // Prepara un segundo usuario con otros datos.
        User duplicate = newUser();
        // Asigna al segundo usuario el email ya persistido.
        duplicate.setEmail(original.getEmail());
        // Comprueba que el servicio rechaza la duplicación.
        assertThrows(Exception.class, () -> userService.createUser(duplicate));
        System.out.println("JUnit: no se pudo crear el usuario porque el email ya existe.");
    }

    // Construye datos válidos para que cada prueba use un email irrepetible.
    private User newUser() {
        // Crea una instancia nueva del modelo de usuario.
        User user = new User();
        // Define el nombre de prueba que se guardará en PostgreSQL.
        user.setName("JUnit");
        // Define el apellido de prueba.
        user.setLastname("Integration");
        // Usa UUID para evitar que el email colisione con datos existentes.
        user.setEmail("junit-" + UUID.randomUUID() + "@example.test");
        // Define una contraseña de prueba.
        user.setPassword("test-password");
        // Completa la comuna requerida por el modelo de usuario.
        user.setCommune("Test commune");
        // Completa el barrio requerido por el modelo de usuario.
        user.setNeighborhood("Test neighborhood");
        // Asigna un rol válido para persistir el usuario.
        user.setRole(Role.User);
        // Define la fecha de creación que se almacenará.
        user.setCreation_date(LocalDateTime.now());
        // Devuelve el usuario listo para guardar.
        return user;
    }
}