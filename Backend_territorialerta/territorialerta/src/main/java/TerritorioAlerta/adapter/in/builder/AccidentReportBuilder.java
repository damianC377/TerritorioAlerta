package TerritorioAlerta.adapter.in.builder;

import java.time.LocalDateTime;

import TerritorioAlerta.adapter.in.validator.AccidentReportValidator;
import TerritorioAlerta.domain.model.Accident_report;

public class AccidentReportBuilder {

    private AccidentReportValidator accidentReportValidator;

    public Accident_report buildAccidentReport(String accidentDate, String typeReport, String commune, String neighborhood, String address, String image, String description, String status) throws Exception {
        
        Accident_report accidentReport = new Accident_report();

        // Normalizamos y validamos los datos del reporte de accidente antes de asignarlos al objeto Accident_report

        accidentReport.setDate(accidentReportValidator.accidentDateValidator(accidentDate).atStartOfDay());
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
