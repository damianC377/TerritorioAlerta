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

/** Prueba el listado de reportes de un usuario con el Port simulado. */
@ExtendWith(MockitoExtension.class)
class FindAccidentReportsByUserIdServiceTest {

    @Mock
    private Accident_reportPort accidentReportPort;

    @InjectMocks
    private FindAccidentReportsByUserIdService findAccidentReportsByUserIdService;

    /** Comprueba que devuelve la lista asociada al ID y delega la consulta al Port. */
    @Test
    void buscarReportesPorUsuario_listaExistente_devuelveLaLista() {
        List<Accident_report> reports = List.of(new Accident_report(), new Accident_report());
        when(accidentReportPort.findByIdUserList(12L)).thenReturn(reports);

        List<Accident_report> result = findAccidentReportsByUserIdService.findAccidentReportsByUserId(12L);

        assertSame(reports, result);
        verify(accidentReportPort).findByIdUserList(12L);
    }
}