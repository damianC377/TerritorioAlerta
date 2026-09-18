package TerritorioAlerta.adapter.rest.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import TerritorioAlerta.adapter.in.builder.AccidentReportBuilder;
import TerritorioAlerta.adapter.rest.request.Accident_reportRequest;
import TerritorioAlerta.adapter.rest.response.AccidentReportResponse;
import TerritorioAlerta.domain.model.Accident_report;

@Component
public class AccidentReportRestMapper {

    @Autowired
    private AccidentReportBuilder accidentReportBuilder;

    public Accident_report toDomain(Accident_reportRequest req) throws Exception {
        return accidentReportBuilder.buildAccidentReport(
                req.getId_user(),
                req.getDate(),
                req.getType_report(),
                req.getCommune(),
                req.getNeighborhood(),
                req.getAddress(),
                req.getImage(),
                req.getDescription(),
                req.getStatus()
        );
    }

    public AccidentReportResponse toResponse(Accident_report accidentReport) {
        AccidentReportResponse res = new AccidentReportResponse();
        res.setId_accident_report(accidentReport.getId_accident_report());
        res.setId_user(accidentReport.getId_user());
        res.setDate(accidentReport.getDate());
        res.setType_report(accidentReport.getType_report() != null ? accidentReport.getType_report().name() : null);
        res.setCommune(accidentReport.getCommune());
        res.setNeighborhood(accidentReport.getNeighborhood());
        res.setAddress(accidentReport.getAddress());
        res.setImage(accidentReport.getImage());
        res.setDescription(accidentReport.getDescription());
        res.setStatus(accidentReport.getStatus() != null ? accidentReport.getStatus().name() : null);
        res.setCreation_date(accidentReport.getCreation_date());
        return res;
    }
}
