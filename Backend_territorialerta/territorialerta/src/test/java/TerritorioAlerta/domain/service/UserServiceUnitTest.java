package TerritorioAlerta.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.port.UserPort;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @Mock
    private UserPort userPort;

    @InjectMocks
    private CreateUserService userService;

    @Test
    void mockito_crearUsuario_idYEmailLibres_guardaUsuario() throws Exception {
        // Prepara un usuario con un identificador y un email disponibles.
        User user = new User();
        // Asigna el identificador que el mock reportará como no registrado.
        user.setId_user(52L);
        // Asigna el email que el mock reportará como no registrado.
        user.setEmail("mock-id@example.test");

        // Simula que la búsqueda por ID no encuentra ningún usuario.
        when(userPort.findById(52L)).thenReturn(null);
        // Simula que la búsqueda por email no encuentra ningún usuario.
        when(userPort.findByEmail("mock-id@example.test")).thenReturn(null);
        // Devuelve el usuario preparado para cualquier usuario que se guarde.
        when(userPort.save(any(User.class))).thenReturn(user);

        // Ejecuta el servicio real con el puerto simulado.
        User saved = userService.createUser(user);

        // Comprueba que el usuario devuelto conserva el identificador.
        assertEquals(52L, saved.getId_user());
        // Comprueba que el servicio pidió guardar un usuario.
        verify(userPort).save(any(User.class));
        System.out.println("Mockito: usuario con ID 52 creado satisfactoriamente.");
    }

    @Test
    void mockito_crearUsuario_emailLibre_guardaUsuario() throws Exception {
        // Prepara un usuario con un email disponible.
        User user = new User();
        // Asigna el email que el mock reportará como no registrado.
        user.setEmail("mock-user@example.test");

        // Simula que la búsqueda por email no encuentra ningún usuario.
        when(userPort.findByEmail("mock-user@example.test")).thenReturn(null);
        // Devuelve el usuario preparado para cualquier usuario que se guarde.
        when(userPort.save(any(User.class))).thenReturn(user);

        // Ejecuta el servicio real con el puerto simulado.
        User saved = userService.createUser(user);

        // Comprueba que el servicio devuelve exactamente el usuario guardado.
        assertSame(user, saved);
        // Comprueba que el servicio pidió guardar un usuario.
        verify(userPort).save(any(User.class));
        System.out.println("Mockito: usuario mock-user@example.test creado satisfactoriamente.");
    }

    // Verifica que un ID ya registrado provoca una excepción y no crea otro usuario.
    @Test
    void mockito_crearUsuario_idDuplicado_lanzaExcepcionYNoGuarda() {
        // Prepara un usuario cuyo ID ya existe según el puerto simulado.
        User user = new User();
        // Define el ID que se reportará como duplicado.
        user.setId_user(73L);
        // Asigna un correo que no necesita consultarse porque el ID ya está ocupado.
        user.setEmail("id-duplicado@example.test");
        // Simula que la búsqueda por ID encuentra un usuario existente.
        when(userPort.findById(73L)).thenReturn(new User());

        // Comprueba que el servicio rechaza el ID repetido con una excepción explicativa.
        Exception error = org.junit.jupiter.api.Assertions.assertThrows(Exception.class,
                () -> userService.createUser(user));
        // Imprime el mensaje capturado para verlo en la salida de pruebas.
        System.out.println("Mensaje de error Mockito: " + error.getMessage());
        // Comprueba el mensaje que explica el conflicto de identificador.
        assertEquals("El usuario con ID 73 ya existe.", error.getMessage());
        // Comprueba que un usuario duplicado nunca se envía a guardar.
        org.mockito.Mockito.verify(userPort, org.mockito.Mockito.never()).save(any(User.class));
    }

    // Verifica que un correo ya registrado provoca una excepción y no crea otro usuario.
    @Test
    void mockito_crearUsuario_emailDuplicado_lanzaExcepcionYNoGuarda() {
        // Prepara un usuario nuevo que intentará usar un correo existente.
        User user = new User();
        // Define el correo que el puerto reportará como duplicado.
        user.setEmail("correo-duplicado@example.test");
        // Simula que la búsqueda por correo encuentra otro usuario.
        when(userPort.findByEmail("correo-duplicado@example.test")).thenReturn(new User());

        // Comprueba que el servicio rechaza el correo repetido con una excepción.
        Exception error = org.junit.jupiter.api.Assertions.assertThrows(Exception.class,
                () -> userService.createUser(user));
        // Imprime el mensaje capturado para verlo en la salida de pruebas.
        System.out.println("Mensaje de error Mockito: " + error.getMessage());
        // Comprueba el mensaje que identifica el correo en conflicto.
        assertEquals("Usuario con email correo-duplicado@example.test ya existe.", error.getMessage());
        // Comprueba que la ruta de error no intenta guardar al usuario duplicado.
        org.mockito.Mockito.verify(userPort, org.mockito.Mockito.never()).save(any(User.class));
    }
}
