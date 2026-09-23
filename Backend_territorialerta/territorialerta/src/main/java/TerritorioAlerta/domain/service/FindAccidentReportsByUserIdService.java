package TerritorioAlerta.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import TerritorioAlerta.domain.model.Accident_report;
import TerritorioAlerta.domain.port.Accident_reportPort;

@Service
/** Servicio de dominio que obtiene los reportes creados por un usuario. */
public class FindAccidentReportsByUserIdService {

    private final Accident_reportPort accidentReportPort;

    /** Construye el servicio con el puerto de reportes requerido. */
    public FindAccidentReportsByUserIdService(Accident_reportPort accidentReportPort) {
        this.accidentReportPort = accidentReportPort;
    }

    /** Devuelve la lista de reportes asociados al usuario recibido. */
    public List<Accident_report> findAccidentReportsByUserId(Long userId) {
        return accidentReportPort.findByIdUserList(userId);
    }
}
