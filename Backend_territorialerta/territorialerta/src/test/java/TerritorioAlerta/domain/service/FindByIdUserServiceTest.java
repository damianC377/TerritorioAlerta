package TerritorioAlerta.domain.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.port.UserPort;

/** Prueba la consulta de usuarios del servicio sin usar una base de datos real. */
@ExtendWith(MockitoExtension.class)
class FindByIdUserServiceTest {

    @Mock
    private UserPort userPort;

    @InjectMocks
    private FindByIdUserService findByIdUserService;

    /** Comprueba que devuelve el usuario que encuentra el Port simulado. */
    @Test
    void buscarUsuario_existente_devuelveUsuario() throws Exception {
        User user = new User();
        user.setId_user(10L);
        when(userPort.findById(10L)).thenReturn(user);

        User result = findByIdUserService.findByIdUser(10L);

        assertSame(user, result);
        verify(userPort).findById(10L);
    }

    /** Comprueba que lanza excepción cuando el Port indica que el usuario no existe. */
    @Test
    void buscarUsuario_inexistente_lanzaExcepcion() {
        when(userPort.findById(404L)).thenReturn(null);

        assertThrows(Exception.class, () -> findByIdUserService.findByIdUser(404L));

        verify(userPort).findById(404L);
    }
}