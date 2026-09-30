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

import TerritorioAlerta.domain.model.Accident_report;
import TerritorioAlerta.domain.port.Accident_reportPort;

/** Prueba la consulta de reportes por ID sin una base de datos real. */
@ExtendWith(MockitoExtension.class)
class FindAccidentReportByIdServiceTest {

    @Mock
    private Accident_reportPort accidentReportPort;

    @InjectMocks
    private FindAccidentReportByIdService findAccidentReportByIdService;

    /** Comprueba que devuelve el reporte encontrado por el Port simulado. */
    @Test
    void buscarReporte_existente_devuelveReporte() throws Exception {
        Accident_report report = new Accident_report();
        report.setId_accident_report(21L);
        when(accidentReportPort.findById(21L)).thenReturn(report);

        Accident_report result = findAccidentReportByIdService.findAccidentReportById(21L);

        assertSame(report, result);
        verify(accidentReportPort).findById(21L);
    }

    /** Comprueba que lanza excepción si el Port no encuentra el reporte solicitado. */
    @Test
    void buscarReporte_inexistente_lanzaExcepcion() {
        when(accidentReportPort.findById(404L)).thenReturn(null);

        assertThrows(Exception.class, () -> findAccidentReportByIdService.findAccidentReportById(404L));

        verify(accidentReportPort).findById(404L);
    }
}