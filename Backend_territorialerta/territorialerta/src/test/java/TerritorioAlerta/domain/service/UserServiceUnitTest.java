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
    }
}
