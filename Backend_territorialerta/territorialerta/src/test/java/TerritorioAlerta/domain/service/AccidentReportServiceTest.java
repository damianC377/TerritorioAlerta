package TerritorioAlerta.domain.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import TerritorioAlerta.domain.model.Accident_report;
import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.port.Accident_reportPort;
import TerritorioAlerta.domain.port.UserPort;

/**
 * Prueba la regla de negocio de CreateAccidentReportService al crear reportes.
 * No se prueba una base de datos real: los Ports están simulados con Mockito.
 */
@ExtendWith(MockitoExtension.class)
class AccidentReportServiceTest {

    @Mock
    private UserPort userPort;

    @Mock
    private Accident_reportPort accidentReportPort;

    @InjectMocks
    private CreateAccidentReportService accidentReportService;

    // Verifica que un reporte nuevo de un usuario existente se guarda; se mockea UserPort para validar la existencia del usuario.
    @Test
    void crearReporte_usuarioExistente_seGuardaCorrectamente() {
        User user = new User();
        user.setId_user(1L);

        Accident_report report = new Accident_report();
        report.setId_user(1L);
        report.setId_accident_report(null);

        lenient().when(userPort.findById(1L)).thenReturn(user);
        when(accidentReportPort.save(report)).thenReturn(report);

        assertDoesNotThrow(() -> accidentReportService.createAccidentReport(report));

        verify(accidentReportPort).save(report);
    }

    // Verifica que un reporte cuyo usuario no existe lanza una excepción y no se guarda; se mockea UserPort para devolver null.
    @Test
    void crearReporte_usuarioNoExiste_lanzaExcepcionYNoGuarda() {
        Accident_report report = new Accident_report();
        report.setId_user(99L);

        lenient().when(userPort.findById(99L)).thenReturn(null);

        assertThrows(Exception.class, () -> accidentReportService.createAccidentReport(report));

        verify(accidentReportPort, never()).save(any());
    }
}