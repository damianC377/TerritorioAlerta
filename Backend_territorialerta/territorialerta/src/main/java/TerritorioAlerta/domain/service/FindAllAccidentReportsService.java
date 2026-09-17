package TerritorioAlerta.domain.service;

import java.util.List;

import TerritorioAlerta.domain.model.Accident_report;
import TerritorioAlerta.domain.port.Accident_reportPort;

public class FindAllAccidentReportsService {

    private final Accident_reportPort accidentReportPort;

    public FindAllAccidentReportsService(Accident_reportPort accidentReportPort) {
        this.accidentReportPort = accidentReportPort;
    }

    public List<Accident_report> findAllAccidentReports() {
        return accidentReportPort.findAll();
    }
}
