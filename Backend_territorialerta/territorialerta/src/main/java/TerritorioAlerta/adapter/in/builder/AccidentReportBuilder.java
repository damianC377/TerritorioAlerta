package TerritorioAlerta.adapter.in.builder;

import org.springframework.stereotype.Component;

import TerritorioAlerta.adapter.in.validator.AccidentReportValidator;
import TerritorioAlerta.domain.model.Accident_report;

@Component
/** Construye reportes de dominio a partir de datos validados de entrada. */
public class AccidentReportBuilder {

    private final AccidentReportValidator accidentReportValidator;

    /** Construye el builder con el validador de reportes. */
    public AccidentReportBuilder(AccidentReportValidator accidentReportValidator) {
        this.accidentReportValidator = accidentReportValidator;
    }

    /** Valida y transforma los datos recibidos en un reporte de accidente. */
    public Accident_report buildAccidentReport(String id_user, String accidentDate, String typeReport, String commune, String neighborhood, String address, String image, String description, String status) throws Exception {
        
        Accident_report accidentReport = new Accident_report();

        // Normalizamos y validamos los datos del reporte de accidente antes de asignarlos al objeto Accident_report

        accidentReport.setId_user(accidentReportValidator.idUserValidator(id_user));
        accidentReport.setDate(accidentReportValidator.dateValidator(accidentDate));
        accidentReport.setType_report(accidentReportValidator.typeReportValidator(typeReport));
        accidentReport.setCommune(accidentReportValidator.communeValidator(commune));
        accidentReport.setNeighborhood(accidentReportValidator.neighborhoodValidator(neighborhood));
        accidentReport.setAddress(accidentReportValidator.addressValidator(address));
        accidentReport.setImage(accidentReportValidator.imageValidator(image));
        accidentReport.setDescription(accidentReportValidator.descriptionValidator(description));
        accidentReport.setStatus(accidentReportValidator.statusValidator(status));

        return accidentReport;
    }
}
