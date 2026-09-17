package TerritorioAlerta.domain.service;

import TerritorioAlerta.domain.model.Accident_report;
import TerritorioAlerta.domain.port.Accident_reportPort;

public class FindAccidentReportByIdService {

    private final Accident_reportPort accidentReportPort;

    public FindAccidentReportByIdService(Accident_reportPort accidentReportPort) {
        this.accidentReportPort = accidentReportPort;
    }

    public Accident_report findAccidentReportById(Long accidentReportId) throws Exception {
        Accident_report accidentReport = accidentReportPort.findById(accidentReportId);

        if (accidentReport == null) {
            throw new Exception("Reporte de accidente con ID " + accidentReportId + " no encontrado.");
        }

        return accidentReport;
    }
}
