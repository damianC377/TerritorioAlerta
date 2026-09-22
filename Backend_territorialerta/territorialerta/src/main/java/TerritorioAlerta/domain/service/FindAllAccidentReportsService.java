package TerritorioAlerta.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import TerritorioAlerta.domain.model.Accident_report;
import TerritorioAlerta.domain.port.Accident_reportPort;

@Service
/** Servicio de dominio que obtiene todos los reportes comunitarios. */
public class FindAllAccidentReportsService {

    private final Accident_reportPort accidentReportPort;

    /** Construye el servicio con el puerto de reportes requerido. */
    public FindAllAccidentReportsService(Accident_reportPort accidentReportPort) {
        this.accidentReportPort = accidentReportPort;
    }

    /** Devuelve todos los reportes disponibles para consulta ciudadana. */
    public List<Accident_report> findAllAccidentReports() {
        return accidentReportPort.findAll();
    }
}
