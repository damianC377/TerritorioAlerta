package TerritorioAlerta.adapter.in.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import TerritorioAlerta.adapter.in.validator.AccidentReportValidator;
import TerritorioAlerta.domain.model.Accident_report;

@Component
public class AccidentReportBuilder {

    @Autowired
    private AccidentReportValidator accidentReportValidator;

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
