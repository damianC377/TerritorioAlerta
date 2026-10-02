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
    // ==================== AQUÍ EMPIEZAN LAS PRUEBAS MOCKITO ====================

    // Caso Mockito exitoso: crea un usuario cuando el email está disponible.
    @Test
    void mockito_crearUsuario_emailLibre_guardaUsuario() throws Exception {
        // Crea un puerto falso para no consultar PostgreSQL.
        TerritorioAlerta.domain.port.UserPort mockPort = org.mockito.Mockito.mock(
                TerritorioAlerta.domain.port.UserPort.class);
        // Construye otro servicio que usa únicamente el puerto falso.
        CreateUserService mockService = new CreateUserService(mockPort);
        // Prepara el usuario que se pasará al servicio.
        User user = new User();
        // Define el email que el servicio debe comprobar.
        user.setEmail("mock-user@example.test");
        // Simula que el email todavía no está registrado.
        org.mockito.Mockito.when(mockPort.findByEmail("mock-user@example.test")).thenReturn(null);
        // Simula que el puerto devuelve el usuario después de guardarlo.
        org.mockito.Mockito.when(mockPort.save(user)).thenReturn(user);
        // Ejecuta la creación usando el servicio real y el puerto simulado.
        User saved = mockService.createUser(user);
        // Comprueba que se devolvió el usuario preparado.
        org.junit.jupiter.api.Assertions.assertSame(user, saved);
        // Comprueba que el mock recibió la orden de guardar.
        org.mockito.Mockito.verify(mockPort).save(user);
    }

    // Caso Mockito exitoso: el ID y el email están disponibles para el usuario.
    @Test
    void mockito_crearUsuario_idYEmailLibres_guardaUsuario() throws Exception {
        // Crea un puerto falso para simular las búsquedas y el guardado.
        TerritorioAlerta.domain.port.UserPort mockPort = org.mockito.Mockito.mock(
                TerritorioAlerta.domain.port.UserPort.class);
        // Construye una instancia del servicio con la dependencia simulada.
        CreateUserService mockService = new CreateUserService(mockPort);
        // Crea el usuario que se intentará guardar.
        User user = new User();
        // Define un ID explícito que todavía no existe.
        user.setId_user(52L);
        // Define el email libre que se validará.
        user.setEmail("mock-id@example.test");
        // Simula que la búsqueda por ID no encuentra usuarios.
        org.mockito.Mockito.when(mockPort.findById(52L)).thenReturn(null);
        // Simula que la búsqueda por email tampoco encuentra usuarios.
        org.mockito.Mockito.when(mockPort.findByEmail("mock-id@example.test")).thenReturn(null);
        // Simula el resultado del guardado.
        org.mockito.Mockito.when(mockPort.save(user)).thenReturn(user);
        // Ejecuta la creación del usuario.
        User saved = mockService.createUser(user);
        // Comprueba que el resultado conserva el ID recibido.
        org.junit.jupiter.api.Assertions.assertEquals(52L, saved.getId_user());
        // Comprueba que el puerto recibió la operación de guardado.
        org.mockito.Mockito.verify(mockPort).save(user);
    }

    // Caso Mockito negativo: el servicio rechaza un email duplicado.
    @Test
    void mockito_crearUsuario_emailDuplicado_lanzaExcepcion() {
        // Crea un puerto falso para simular un email ya registrado.
        TerritorioAlerta.domain.port.UserPort mockPort = org.mockito.Mockito.mock(
                TerritorioAlerta.domain.port.UserPort.class);
        // Construye el servicio bajo prueba con el puerto simulado.
        CreateUserService mockService = new CreateUserService(mockPort);
        // Prepara el usuario que intentará utilizar el email duplicado.
        User user = new User();
        // Asigna el email que el mock marcará como ocupado.
        user.setEmail("duplicado-mock@example.test");
        // Configura el mock para indicar que el email ya existe.
        org.mockito.Mockito.when(mockPort.findByEmail("duplicado-mock@example.test"))
                .thenReturn(new User());
        // Comprueba que el servicio lanza excepción ante el email repetido.
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> mockService.createUser(user));
        // Comprueba que no se intentó guardar el usuario inválido.
        org.mockito.Mockito.verify(mockPort, org.mockito.Mockito.never())
                .save(org.mockito.ArgumentMatchers.any(User.class));
    }
}