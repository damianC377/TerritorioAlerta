package TerritorioAlerta.domain.service;

import static org.junit.jupiter.api.Assertions.assertSame;
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

/** Prueba el listado comunitario de reportes sin acceder a PostgreSQL. */
@ExtendWith(MockitoExtension.class)
class FindAllAccidentReportsServiceTest {

    @Mock
    private Accident_reportPort accidentReportPort;

    @InjectMocks
    private FindAllAccidentReportsService findAllAccidentReportsService;

    /** Comprueba que devuelve todos los reportes suministrados por el Port. */
    @Test
    void buscarTodosLosReportes_listaExistente_devuelveLaLista() {
        List<Accident_report> reports = List.of(new Accident_report(), new Accident_report());
        when(accidentReportPort.findAll()).thenReturn(reports);

        List<Accident_report> result = findAllAccidentReportsService.findAllAccidentReports();

        assertSame(reports, result);
        verify(accidentReportPort).findAll();
    }
}