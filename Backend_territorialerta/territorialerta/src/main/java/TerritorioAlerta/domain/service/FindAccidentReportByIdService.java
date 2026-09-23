package TerritorioAlerta.domain.service;

import org.springframework.stereotype.Service;

import TerritorioAlerta.domain.model.Accident_report;
import TerritorioAlerta.domain.port.Accident_reportPort;

@Service
/** Servicio de dominio para consultar un reporte por su identificador. */
public class FindAccidentReportByIdService {

    private final Accident_reportPort accidentReportPort;

    /** Construye el servicio con el puerto de reportes requerido. */
    public FindAccidentReportByIdService(Accident_reportPort accidentReportPort) {
        this.accidentReportPort = accidentReportPort;
    }

    /** Busca un reporte y falla si no existe. */
    public Accident_report findAccidentReportById(Long accidentReportId) throws Exception {
        Accident_report accidentReport = accidentReportPort.findById(accidentReportId);

        if (accidentReport == null) {
            throw new Exception("Reporte de accidente con ID " + accidentReportId + " no encontrado.");
        }

        return accidentReport;
    }
}
