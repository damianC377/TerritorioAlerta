package TerritorioAlerta.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import TerritorioAlerta.domain.model.Accident_report;
import TerritorioAlerta.domain.port.Accident_reportPort;

@Service
public class CreateAccidentReportService {

    @Autowired
    private Accident_reportPort accidentReportPort;

    public Accident_report createAccidentReport(Accident_report accidentReport) throws Exception {
        if (accidentReport.getId_accident_report() != null
                && accidentReportPort.findById(accidentReport.getId_accident_report()) != null) {
            throw new Exception("El reporte de accidente con ID "
                    + accidentReport.getId_accident_report() + " ya existe.");
        }

        if (accidentReport.getId_user() == null) {
            throw new Exception("El ID del usuario es requerido.");
        }

        return accidentReportPort.save(accidentReport);
    }
}
