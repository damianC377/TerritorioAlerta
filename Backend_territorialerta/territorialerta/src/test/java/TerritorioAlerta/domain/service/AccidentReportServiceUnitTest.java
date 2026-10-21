package TerritorioAlerta.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
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

import TerritorioAlerta.domain.model.Accident_report;
import TerritorioAlerta.domain.port.Accident_reportPort;

// Activa Mockito para crear y limpiar los mocks de cada prueba.
@ExtendWith(MockitoExtension.class)
class AccidentReportServiceUnitTest {

    // Simula el puerto sin conectarse al repositorio o a PostgreSQL.
    @Mock
    private Accident_reportPort accidentReportPort;

    // Construye el servicio real inyectándole el puerto simulado.
    @InjectMocks
    private CreateAccidentReportService accidentReportService;

    // Comprueba que un reporte asociado a un usuario se guarda correctamente.
    @Test
    void mockito_crearReporte_usuarioIndicado_guardaReporte() throws Exception {
        // Prepara un reporte con propietario, requerido por la regla de negocio.
        Accident_report report = new Accident_report();
        // Asigna el identificador del usuario dueño del reporte.
        report.setId_user(8L);
        // Simula que el puerto devuelve el mismo reporte al guardar.
        when(accidentReportPort.save(report)).thenReturn(report);

        // Ejecuta el servicio real con el puerto simulado.
        Accident_report saved = accidentReportService.createAccidentReport(report);

        // Comprueba que el resultado es el mismo objeto preparado.
        assertSame(report, saved);
        // Comprueba que el servicio delegó el guardado al puerto.
        verify(accidentReportPort).save(report);
    }

    // Comprueba que un identificador de reporte libre se puede guardar.
    @Test
    void mockito_crearReporte_idNoExistente_guardaReporte() throws Exception {
        // Prepara un reporte con identificador explícito.
        Accident_report report = new Accident_report();
        // Asigna el identificador que se verificará como libre.
        report.setId_accident_report(63L);
        // Asigna un usuario válido para el reporte.
        report.setId_user(8L);
        // Simula que no existe un reporte con ese identificador.
        when(accidentReportPort.findById(63L)).thenReturn(null);
        // Simula el resultado exitoso de guardar el reporte.
        when(accidentReportPort.save(report)).thenReturn(report);

        // Ejecuta la creación mediante el servicio real.
        Accident_report saved = accidentReportService.createAccidentReport(report);

        // Comprueba que el identificador se conserva.
        assertEquals(63L, saved.getId_accident_report());
        // Comprueba que el servicio verificó si el identificador existía.
        verify(accidentReportPort).findById(63L);
        // Comprueba que el reporte válido sí fue guardado.
        verify(accidentReportPort).save(report);
    }

    // Comprueba que el servicio rechaza reportes sin propietario.
    @Test
    void mockito_crearReporte_sinIdUsuario_lanzaExcepcion() {
        // Prepara un reporte sin identificador de usuario.
        Accident_report report = new Accident_report();

        // Comprueba que la regla de negocio genera una excepción.
        assertThrows(Exception.class, () -> accidentReportService.createAccidentReport(report));
        // Comprueba que el reporte inválido nunca se envía a guardar.
        verify(accidentReportPort, never()).save(any(Accident_report.class));
    }
}