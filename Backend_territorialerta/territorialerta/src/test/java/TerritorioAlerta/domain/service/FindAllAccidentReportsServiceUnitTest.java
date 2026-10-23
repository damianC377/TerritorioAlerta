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

// Activa Mockito para probar el listado sin base de datos.
@ExtendWith(MockitoExtension.class)
class FindAllAccidentReportsServiceUnitTest {

    // Simula la consulta de todos los reportes.
    @Mock
    private Accident_reportPort accidentReportPort;

    // Inyecta el puerto falso en el servicio de listado.
    @InjectMocks
    private FindAllAccidentReportsService findAllAccidentReportsService;

    // Comprueba que se devuelve una lista con reportes.
    @Test
    void mockito_listarReportes_listaConDatos_devuelveLista() {
        // Prepara una lista con dos reportes de ejemplo.
        List<Accident_report> expected = List.of(new Accident_report(), new Accident_report());
        // Simula la respuesta de la consulta general.
        when(accidentReportPort.findAll()).thenReturn(expected);

        // Ejecuta el listado mediante el servicio.
        List<Accident_report> result = findAllAccidentReportsService.findAllAccidentReports();

        // Comprueba que se devuelve la lista configurada.
        assertSame(expected, result);
        // Comprueba que el puerto recibió la consulta general.
        verify(accidentReportPort).findAll();
        System.out.println("Mockito: listado de reportes obtenido satisfactoriamente.");
    }

    // Comprueba que el servicio puede devolver una lista vacía.
    @Test
    void mockito_listarReportes_listaVacia_devuelveListaVacia() {
        // Prepara una lista vacía para el escenario sin reportes.
        List<Accident_report> expected = List.of();
        // Configura el puerto para devolver cero resultados.
        when(accidentReportPort.findAll()).thenReturn(expected);

        // Ejecuta el servicio de listado.
        List<Accident_report> result = findAllAccidentReportsService.findAllAccidentReports();

        // Comprueba que el servicio devuelve la lista vacía simulada.
        assertSame(expected, result);
        // Comprueba que se realizó la consulta al puerto.
        verify(accidentReportPort).findAll();
        System.out.println("Mockito: listado completado satisfactoriamente; no hay reportes.");
    }

    // Comprueba que un error del puerto se informa al llamador.
    @Test
    void mockito_listarReportes_errorDelPuerto_propagaExcepcion() {
        // Simula una falla al consultar todos los reportes.
        when(accidentReportPort.findAll()).thenThrow(new IllegalStateException("Error de consulta"));

        // Comprueba que el servicio propaga la excepción simulada.
        IllegalStateException error = assertThrows(IllegalStateException.class,
            () -> findAllAccidentReportsService.findAllAccidentReports());
        System.out.println("Mensaje de error Mockito: " + error.getMessage());
        // Comprueba que se intentó ejecutar la consulta.
        verify(accidentReportPort).findAll();
    }
}