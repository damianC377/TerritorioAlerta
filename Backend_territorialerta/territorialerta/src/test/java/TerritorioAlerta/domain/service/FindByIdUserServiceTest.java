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
    }

    // Comprueba el caso negativo para una clave que no existe.
    @Test
    void buscarUsuario_idInexistente_lanzaExcepcion() {
        // Confirma que el servicio lanza una excepción al no encontrar el ID.
        assertThrows(Exception.class, () -> findByIdUserService.findByIdUser(Long.MAX_VALUE));
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
    // ==================== AQUÍ EMPIEZAN LAS PRUEBAS MOCKITO ====================

    // Caso Mockito exitoso: devuelve al usuario encontrado por su ID.
    @Test
    void mockito_buscarUsuario_existente_devuelveUsuario() throws Exception {
        // Crea un puerto falso para controlar la respuesta de búsqueda.
        TerritorioAlerta.domain.port.UserPort mockPort = org.mockito.Mockito.mock(
                TerritorioAlerta.domain.port.UserPort.class);
        // Construye el servicio de búsqueda conectado al puerto falso.
        FindByIdUserService mockService = new FindByIdUserService(mockPort);
        // Prepara el usuario que devolverá la búsqueda.
        User expected = new User();
        // Asigna el ID solicitado al usuario esperado.
        expected.setId_user(14L);
        // Configura el mock para devolver ese usuario al buscar el ID 14.
        org.mockito.Mockito.when(mockPort.findById(14L)).thenReturn(expected);
        // Ejecuta la búsqueda usando el servicio.
        User result = mockService.findByIdUser(14L);
        // Comprueba que se devolvió la instancia preparada.
        org.junit.jupiter.api.Assertions.assertSame(expected, result);
        // Comprueba que el servicio consultó ese ID en el puerto.
        org.mockito.Mockito.verify(mockPort).findById(14L);
    }

    // Caso Mockito exitoso adicional: devuelve otro usuario encontrado.
    @Test
    void mockito_buscarUsuario_otroIdExistente_devuelveUsuario() throws Exception {
        // Crea otro puerto falso para este escenario independiente.
        TerritorioAlerta.domain.port.UserPort mockPort = org.mockito.Mockito.mock(
                TerritorioAlerta.domain.port.UserPort.class);
        // Construye otra instancia del servicio con el mock.
        FindByIdUserService mockService = new FindByIdUserService(mockPort);
        // Prepara el usuario del segundo escenario.
        User expected = new User();
        // Asigna el identificador 27 al usuario preparado.
        expected.setId_user(27L);
        // Configura el mock para encontrar el usuario con ID 27.
        org.mockito.Mockito.when(mockPort.findById(27L)).thenReturn(expected);
        // Busca el usuario usando el segundo ID.
        User result = mockService.findByIdUser(27L);
        // Comprueba que el servicio devolvió el ID correcto.
        org.junit.jupiter.api.Assertions.assertEquals(27L, result.getId_user());
        // Comprueba que la búsqueda se delegó al puerto con ese ID.
        org.mockito.Mockito.verify(mockPort).findById(27L);
    }

    // Caso Mockito negativo: el servicio falla si el usuario no existe.
    @Test
    void mockito_buscarUsuario_inexistente_lanzaExcepcion() {
        // Crea un puerto falso para simular una búsqueda sin resultado.
        TerritorioAlerta.domain.port.UserPort mockPort = org.mockito.Mockito.mock(
                TerritorioAlerta.domain.port.UserPort.class);
        // Construye el servicio que usará la respuesta simulada.
        FindByIdUserService mockService = new FindByIdUserService(mockPort);
        // Configura el mock para responder null al ID inexistente.
        org.mockito.Mockito.when(mockPort.findById(404L)).thenReturn(null);
        // Comprueba que el servicio convierte el resultado nulo en excepción.
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class,
                () -> mockService.findByIdUser(404L));
        // Comprueba que sí se consultó el ID indicado.
        org.mockito.Mockito.verify(mockPort).findById(404L);
    }
}