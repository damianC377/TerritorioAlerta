package TerritorioAlerta.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

// Configura Mockito para probar búsquedas sin inicializar Spring.
@ExtendWith(MockitoExtension.class)
class FindByIdUserServiceUnitTest {

    // Simula el puerto de usuarios.
    @Mock
    private UserPort userPort;

    // Inyecta el mock en el servicio real de búsqueda.
    @InjectMocks
    private FindByIdUserService findByIdUserService;

    // Comprueba que una búsqueda existente devuelve el usuario.
    @Test
    void mockito_buscarUsuario_existente_devuelveUsuario() throws Exception {
        // Prepara el usuario que simulará el puerto.
        User expected = new User();
        // Asigna el identificador que se consultará.
        expected.setId_user(14L);
        // Configura la respuesta para la búsqueda por ID 14.
        when(userPort.findById(14L)).thenReturn(expected);

        // Ejecuta la búsqueda mediante el servicio.
        User result = findByIdUserService.findByIdUser(14L);

        // Comprueba que el servicio devuelve el mismo usuario preparado.
        assertSame(expected, result);
        // Comprueba que el servicio consultó el ID 14.
        verify(userPort).findById(14L);
        System.out.println("Mockito: usuario con ID 14 encontrado satisfactoriamente.");
    }

    // Comprueba que otra búsqueda existente devuelve el ID correspondiente.
    @Test
    void mockito_buscarUsuario_otroIdExistente_devuelveUsuario() throws Exception {
        // Prepara el usuario para el segundo caso.
        User expected = new User();
        // Define el identificador del usuario esperado.
        expected.setId_user(27L);
        // Simula que el puerto encuentra ese usuario.
        when(userPort.findById(27L)).thenReturn(expected);

        // Ejecuta la búsqueda por el segundo ID.
        User result = findByIdUserService.findByIdUser(27L);

        // Comprueba que el resultado tiene el ID esperado.
        assertEquals(27L, result.getId_user());
        // Comprueba que el puerto recibió el ID 27.
        verify(userPort).findById(27L);
        System.out.println("Mockito: usuario con ID 27 encontrado satisfactoriamente.");
    }

    // Comprueba que un ID inexistente genera una excepción.
    @Test
    void mockito_buscarUsuario_inexistente_lanzaExcepcion() {
        // Simula que no existe un usuario con ID 404.
        when(userPort.findById(404L)).thenReturn(null);

        // Comprueba que el servicio informa que no encontró al usuario.
        Exception error = assertThrows(Exception.class, () -> findByIdUserService.findByIdUser(404L));
        System.out.println("Mensaje de error Mockito: " + error.getMessage());
        // Comprueba que la búsqueda se hizo usando el ID esperado.
        verify(userPort).findById(404L);
    }
}