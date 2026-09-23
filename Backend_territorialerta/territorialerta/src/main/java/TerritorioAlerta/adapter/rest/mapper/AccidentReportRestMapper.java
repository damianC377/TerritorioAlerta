package TerritorioAlerta.adapter.rest.mapper;

import org.springframework.stereotype.Component;

import TerritorioAlerta.adapter.in.builder.AccidentReportBuilder;
import TerritorioAlerta.adapter.rest.request.Accident_reportRequest;
import TerritorioAlerta.adapter.rest.response.AccidentReportResponse;
import TerritorioAlerta.domain.model.Accident_report;

@Component
/** Convierte entre solicitudes/respuestas REST y reportes de dominio. */
public class AccidentReportRestMapper {

    private final AccidentReportBuilder accidentReportBuilder;

    /** Construye el mapper con el builder de reportes. */
    public AccidentReportRestMapper(AccidentReportBuilder accidentReportBuilder) {
        this.accidentReportBuilder = accidentReportBuilder;
    }

    /** Convierte una solicitud HTTP en un reporte de dominio validado. */
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

    /** Convierte un reporte de dominio en la respuesta pública del API. */
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
