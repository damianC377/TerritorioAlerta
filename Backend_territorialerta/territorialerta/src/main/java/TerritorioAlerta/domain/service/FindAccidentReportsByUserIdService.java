package TerritorioAlerta.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import TerritorioAlerta.domain.model.Accident_report;
import TerritorioAlerta.domain.port.Accident_reportPort;

@Service
public class FindAccidentReportsByUserIdService {

    @Autowired
    private Accident_reportPort accidentReportPort;

    public List<Accident_report> findAccidentReportsByUserId(Long userId) {
        return accidentReportPort.findByIdUserList(userId);
    }
}
