package TerritorioAlerta.domain.service;

import org.springframework.stereotype.Service;

import TerritorioAlerta.domain.model.Accident_report;
import TerritorioAlerta.domain.port.Accident_reportPort;

@Service
/** Servicio de dominio que valida las reglas de creación de reportes. */
public class CreateAccidentReportService {

    private final Accident_reportPort accidentReportPort;

    /** Construye el servicio con el puerto de reportes requerido. */
    public CreateAccidentReportService(Accident_reportPort accidentReportPort) {
        this.accidentReportPort = accidentReportPort;
    }

    /** Rechaza reportes duplicados o sin usuario y guarda los reportes válidos. */
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
