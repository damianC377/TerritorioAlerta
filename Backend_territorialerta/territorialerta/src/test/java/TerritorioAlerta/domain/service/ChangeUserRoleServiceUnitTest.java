package TerritorioAlerta.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.model.Enums.Role;
import TerritorioAlerta.domain.port.UserPort;

// Configura Mockito para cada prueba de esta clase.
@ExtendWith(MockitoExtension.class)
class ChangeUserRoleServiceUnitTest {

    // Simula el puerto de persistencia de usuarios.
    @Mock
    private UserPort userPort;

    // Crea el servicio bajo prueba con el puerto simulado.
    @InjectMocks
    private ChangeUserRoleService changeUserRoleService;

    // Comprueba que el servicio asigna y guarda el rol Analyst.
    @Test
    void mockito_cambiarRol_usuarioValido_asignaAnalystYGuarda() throws Exception {
        // Prepara el usuario que se modificará.
        User user = new User();
        // Simula la respuesta del guardado.
        when(userPort.save(user)).thenReturn(user);

        // Ejecuta el cambio al rol Analyst.
        changeUserRoleService.changeUserRole(user, Role.Analyst);

        // Comprueba que el nuevo rol quedó asignado.
        assertEquals(Role.Analyst, user.getRole());
        // Comprueba que el usuario se envió al puerto para guardarse.
        verify(userPort).save(user);
    }

    // Comprueba que el servicio asigna y guarda el rol Admin.
    @Test
    void mockito_cambiarRol_usuarioValido_asignaAdminYGuarda() throws Exception {
        // Prepara el usuario para el escenario de administración.
        User user = new User();
        // Simula que el puerto devuelve el usuario guardado.
        when(userPort.save(user)).thenReturn(user);

        // Ejecuta el cambio al rol Admin.
        changeUserRoleService.changeUserRole(user, Role.Admin);

        // Comprueba que el usuario recibió el rol Admin.
        assertEquals(Role.Admin, user.getRole());
        // Comprueba que el servicio delegó el guardado.
        verify(userPort).save(user);
    }

    // Comprueba que un usuario nulo se rechaza antes de guardar.
    @Test
    void mockito_cambiarRol_usuarioNulo_lanzaExcepcion() {
        // Comprueba que el servicio genera una excepción para usuario nulo.
        assertThrows(Exception.class, () -> changeUserRoleService.changeUserRole(null, Role.Admin));
        // Comprueba que ningún usuario fue enviado al puerto.
        verify(userPort, never()).save(any(User.class));
    }
}