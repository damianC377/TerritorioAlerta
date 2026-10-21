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

// Activa Mockito para mantener estas pruebas completamente aisladas.
@ExtendWith(MockitoExtension.class)
class AdminServiceUnitTest {

    // Simula el puerto de usuarios para no usar la base de datos.
    @Mock
    private UserPort userPort;

    // Inyecta el mock en el servicio administrativo real.
    @InjectMocks
    private AdminService adminService;

    // Comprueba que el rol Jal se asigna y el usuario se guarda.
    @Test
    void mockito_adminService_usuarioValido_asignaJalYGuarda() throws Exception {
        // Prepara el usuario que recibirá el cambio de rol.
        User user = new User();
        // Simula que el puerto devuelve el usuario guardado.
        when(userPort.save(user)).thenReturn(user);

        // Ejecuta el cambio administrativo al rol Jal.
        adminService.changeUserRole(user, Role.Jal);

        // Comprueba que el modelo recibió el rol solicitado.
        assertEquals(Role.Jal, user.getRole());
        // Comprueba que el servicio pidió guardar ese usuario.
        verify(userPort).save(user);
        System.out.println("Mockito: rol Jal asignado y usuario guardado satisfactoriamente.");
    }

    // Comprueba que también se puede asignar el rol Analyst.
    @Test
    void mockito_adminService_usuarioValido_asignaAnalystYGuarda() throws Exception {
        // Prepara el usuario para el escenario Analyst.
        User user = new User();
        // Simula el resultado de guardar el usuario modificado.
        when(userPort.save(user)).thenReturn(user);

        // Ejecuta el cambio de rol solicitado.
        adminService.changeUserRole(user, Role.Analyst);

        // Comprueba que el rol Analyst quedó en el usuario.
        assertEquals(Role.Analyst, user.getRole());
        // Comprueba que se delegó el guardado al puerto.
        verify(userPort).save(user);
        System.out.println("Mockito: rol Analyst asignado y usuario guardado satisfactoriamente.");
    }

    // Comprueba que un usuario nulo produce un error sin intentar guardar.
    @Test
    void mockito_adminService_usuarioNulo_lanzaExcepcion() {
        // Comprueba que el servicio rechaza una entrada nula.
        Exception error = assertThrows(Exception.class, () -> adminService.changeUserRole(null, Role.User));
        System.out.println("Mensaje de error Mockito: " + error.getMessage());
        // Comprueba que la ruta inválida no llamó al guardado.
        verify(userPort, never()).save(any(User.class));
    }
}