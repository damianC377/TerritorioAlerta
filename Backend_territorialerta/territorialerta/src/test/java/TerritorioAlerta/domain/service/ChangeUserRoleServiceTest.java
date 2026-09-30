package TerritorioAlerta.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

/** Prueba el cambio de rol de usuarios con el Port de persistencia simulado. */
@ExtendWith(MockitoExtension.class)
class ChangeUserRoleServiceTest {

    @Mock
    private UserPort userPort;

    @InjectMocks
    private ChangeUserRoleService changeUserRoleService;

    /** Comprueba que asigna el rol pedido y persiste el mismo usuario. */
    @Test
    void cambiarRol_usuarioValido_actualizaYGuarda() throws Exception {
        User user = new User();

        changeUserRoleService.changeUserRole(user, Role.Admin);

        assertEquals(Role.Admin, user.getRole());
        verify(userPort).save(same(user));
    }

    /** Comprueba que un usuario nulo produce excepción y no intenta guardar. */
    @Test
    void cambiarRol_usuarioNulo_lanzaExcepcionYNoGuarda() {
        assertThrows(Exception.class, () -> changeUserRoleService.changeUserRole(null, Role.Admin));

        verify(userPort, never()).save(org.mockito.ArgumentMatchers.any());
    }
}