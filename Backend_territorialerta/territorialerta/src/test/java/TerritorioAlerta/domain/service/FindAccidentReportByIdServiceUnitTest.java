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

import TerritorioAlerta.domain.model.Accident_report;
import TerritorioAlerta.domain.port.Accident_reportPort;

// Activa Mockito para aislar las consultas del servicio.
@ExtendWith(MockitoExtension.class)
class FindAccidentReportByIdServiceUnitTest {

    // Simula el puerto de reportes sin conectarse a PostgreSQL.
    @Mock
    private Accident_reportPort accidentReportPort;

    // Inyecta el puerto simulado en el servicio de búsqueda.
    @InjectMocks
    private FindAccidentReportByIdService findAccidentReportByIdService;

    // Comprueba que se devuelve el reporte encontrado por su ID.
    @Test
    void mockito_buscarReporte_existente_devuelveReporte() throws Exception {
        // Prepara el reporte que el puerto encontrará.
        Accident_report expected = new Accident_report();
        // Asigna el ID que se buscará.
        expected.setId_accident_report(25L);
        // Simula que el puerto encuentra el reporte con ID 25.
        when(accidentReportPort.findById(25L)).thenReturn(expected);

        // Ejecuta la búsqueda por medio del servicio.
        Accident_report result = findAccidentReportByIdService.findAccidentReportById(25L);

        // Comprueba que se devuelve la instancia preparada.
        assertSame(expected, result);
        // Comprueba que la búsqueda se hizo con el ID correcto.
        verify(accidentReportPort).findById(25L);
        System.out.println("Mockito: reporte con ID 25 encontrado satisfactoriamente.");
    }

    // Comprueba que otro ID existente también se recupera correctamente.
    @Test
    void mockito_buscarReporte_otroIdExistente_devuelveReporte() throws Exception {
        // Prepara un segundo reporte como respuesta del puerto.
        Accident_report expected = new Accident_report();
        // Define el identificador del segundo escenario.
        expected.setId_accident_report(36L);
        // Configura la respuesta para la búsqueda por ID 36.
        when(accidentReportPort.findById(36L)).thenReturn(expected);

        // Busca el reporte usando el servicio real.
        Accident_report result = findAccidentReportByIdService.findAccidentReportById(36L);

        // Comprueba que el resultado contiene el identificador esperado.
        assertEquals(36L, result.getId_accident_report());
        // Comprueba que el puerto recibió la consulta por ID 36.
        verify(accidentReportPort).findById(36L);
        System.out.println("Mockito: reporte con ID 36 encontrado satisfactoriamente.");
    }

    // Comprueba que una búsqueda sin resultado provoca una excepción.
    @Test
    void mockito_buscarReporte_inexistente_lanzaExcepcion() {
        // Simula que no existe un reporte con ID 404.
        when(accidentReportPort.findById(404L)).thenReturn(null);

        // Comprueba que el servicio informa el resultado inexistente.
        Exception error = assertThrows(Exception.class,
                () -> findAccidentReportByIdService.findAccidentReportById(404L));
        System.out.println("Mensaje de error Mockito: " + error.getMessage());
        // Comprueba que el servicio consultó el ID solicitado.
        verify(accidentReportPort).findById(404L);
    }
}