package TerritorioAlerta.domain.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import TerritorioAlerta.domain.model.Accident_report;
import TerritorioAlerta.domain.port.Accident_reportPort;

// Inicializa Mockito sin crear contexto de Spring ni conexión de base de datos.
@ExtendWith(MockitoExtension.class)
class FindAccidentReportsByUserIdServiceUnitTest {

    // Simula la consulta de reportes del puerto.
    @Mock
    private Accident_reportPort accidentReportPort;

    // Inyecta el puerto falso en el servicio de búsqueda.
    @InjectMocks
    private FindAccidentReportsByUserIdService findAccidentReportsByUserIdService;

    // Comprueba que se devuelven los reportes del usuario solicitado.
    @Test
    void mockito_listarReportesPorUsuario_listaConDatos_devuelveLista() {
        // Prepara una respuesta con dos reportes.
        List<Accident_report> expected = List.of(new Accident_report(), new Accident_report());
        // Simula esos resultados para el usuario 12.
        when(accidentReportPort.findByIdUserList(12L)).thenReturn(expected);

        // Ejecuta la búsqueda de reportes por usuario.
        List<Accident_report> result = findAccidentReportsByUserIdService.findAccidentReportsByUserId(12L);

        // Comprueba que el servicio devuelve la lista simulada.
        assertSame(expected, result);
        // Comprueba que el ID se envió al puerto correctamente.
        verify(accidentReportPort).findByIdUserList(12L);
    }

    // Comprueba que el servicio acepta una lista vacía.
    @Test
    void mockito_listarReportesPorUsuario_listaVacia_devuelveListaVacia() {
        // Prepara una lista vacía como respuesta esperada.
        List<Accident_report> expected = List.of();
        // Simula cero reportes para el usuario 19.
        when(accidentReportPort.findByIdUserList(19L)).thenReturn(expected);

        // Ejecuta la búsqueda para el usuario sin reportes.
        List<Accident_report> result = findAccidentReportsByUserIdService.findAccidentReportsByUserId(19L);

        // Comprueba que se conserva la lista vacía devuelta por el puerto.
        assertSame(expected, result);
        // Comprueba que se consultó el ID 19.
        verify(accidentReportPort).findByIdUserList(19L);
    }

    // Comprueba que un error del puerto se propaga al llamador.
    @Test
    void mockito_listarReportesPorUsuario_errorDelPuerto_propagaExcepcion() {
        // Configura una excepción para la búsqueda del usuario 99.
        when(accidentReportPort.findByIdUserList(99L))
                .thenThrow(new IllegalStateException("Error de consulta"));

        // Comprueba que el servicio propaga el error configurado.
        assertThrows(IllegalStateException.class,
                () -> findAccidentReportsByUserIdService.findAccidentReportsByUserId(99L));
        // Comprueba que el servicio llegó a invocar el puerto.
        verify(accidentReportPort).findByIdUserList(99L);
    }
}