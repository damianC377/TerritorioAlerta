package TerritorioAlerta.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import TerritorioAlerta.domain.model.Accident_report;
import TerritorioAlerta.domain.port.Accident_reportPort;

@Service
public class FindAccidentReportByIdService {

    @Autowired
    private Accident_reportPort accidentReportPort;

    public Accident_report findAccidentReportById(Long accidentReportId) throws Exception {
        Accident_report accidentReport = accidentReportPort.findById(accidentReportId);

        if (accidentReport == null) {
            throw new Exception("Reporte de accidente con ID " + accidentReportId + " no encontrado.");
        }

        return accidentReport;
    }
}
