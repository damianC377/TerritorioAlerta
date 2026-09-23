package TerritorioAlerta.domain.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.port.UserPort;

/**
 * Prueba la regla de negocio de CreateUserService al crear usuarios.
 * No se prueba una base de datos real: UserPort está simulado con Mockito.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserPort userPort;

    @InjectMocks
    private CreateUserService userService;

    // Verifica que un usuario con ID y email disponibles se guarda sin errores; se mockean ambas búsquedas para simular que no existen.
    @Test
    void crearUsuario_datosValidos_seGuardaCorrectamente() {
        User user = new User();
        user.setId_user(1L);
        user.setEmail("usuario@ejemplo.com");

        when(userPort.findById(1L)).thenReturn(null);
        when(userPort.findByEmail("usuario@ejemplo.com")).thenReturn(null);
        when(userPort.save(user)).thenReturn(user);

        assertDoesNotThrow(() -> userService.createUser(user));

        verify(userPort).save(user);
    }

    // Verifica que un email duplicado detiene la creación; se mockea un usuario existente para impedir que el Port guarde otro.
    @Test
    void crearUsuario_emailDuplicado_lanzaExcepcionYNoGuarda() {
        User user = new User();
        user.setId_user(2L);
        user.setEmail("duplicado@ejemplo.com");

        when(userPort.findByEmail("duplicado@ejemplo.com")).thenReturn(new User());

        assertThrows(Exception.class, () -> userService.createUser(user));

        verify(userPort, never()).save(any());
    }

    // ⚠️ TEST INTENCIONALMENTE FALLIDO — ejercicio de aprendizaje.
    // Este test afirma algo que el código NO hace, a propósito, para ver cómo
    // se ve un ❌ en el runner de pruebas. El código de producción está bien;
    // el error está en la aserción de este test, no en UserService/
    // AccidentReportService. Se puede borrar este método cuando ya no se
    // necesite para el ejercicio.
    @Test
    void crearUsuario_datosValidos_pruebaFallidaAProposito() {
        User user = new User();
        user.setId_user(1L);
        user.setEmail("usuario@ejemplo.com");

        when(userPort.findById(1L)).thenReturn(null);
        when(userPort.findByEmail("usuario@ejemplo.com")).thenReturn(null);
        when(userPort.save(user)).thenReturn(user);

        assertDoesNotThrow(() -> userService.createUser(user));

        verify(userPort, times(2)).save(any());
    }
}