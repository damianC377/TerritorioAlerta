package TerritorioAlerta.domain.service;

import java.util.List;

import TerritorioAlerta.domain.model.Accident_report;
import TerritorioAlerta.domain.port.Accident_reportPort;

public class FindAccidentReportsByUserIdService {

    private final Accident_reportPort accidentReportPort;

    public FindAccidentReportsByUserIdService(Accident_reportPort accidentReportPort) {
        this.accidentReportPort = accidentReportPort;
    }

    public List<Accident_report> findAccidentReportsByUserId(int userId) {
        return accidentReportPort.findByIdUserList(userId);
    }
}
