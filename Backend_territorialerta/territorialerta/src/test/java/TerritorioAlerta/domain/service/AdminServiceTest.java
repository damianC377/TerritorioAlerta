package TerritorioAlerta.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.model.Enums.Role;
import TerritorioAlerta.domain.port.UserPort;

/** Prueba la operación administrativa de asignación de roles sin base de datos. */
@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UserPort userPort;

    @InjectMocks
    private AdminService adminService;

    /** Comprueba que asigna el rol solicitado y guarda el usuario mediante el Port. */
    @Test
    void cambiarRol_usuarioValido_actualizaYGuarda() throws Exception {
        User user = new User();

        adminService.changeUserRole(user, Role.Analyst);

        assertEquals(Role.Analyst, user.getRole());
        verify(userPort).save(same(user));
    }

    /** Comprueba que rechaza un usuario nulo y no invoca la persistencia. */
    @Test
    void cambiarRol_usuarioNulo_lanzaExcepcionYNoGuarda() {
        assertThrows(Exception.class, () -> adminService.changeUserRole(null, Role.Analyst));

        verify(userPort, never()).save(any());
    }
}