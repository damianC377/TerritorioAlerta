package TerritorioAlerta.adapter.in.validator;

import java.time.LocalDate;

import TerritorioAlerta.application.exception.InputsException;
import TerritorioAlerta.domain.model.Enums.Status;
import TerritorioAlerta.domain.model.Enums.TypeReport;

public class AccidentReportValidator extends SimpleValidator {

    public LocalDate accidentDateValidator(String date) throws Exception {
        return dateValidator("Fecha del accidente: ", date);
    }

    public TypeReport typeReportValidator(String typeReport) throws Exception {
        String normalizedType = stringValidator("Tipo de reporte: ", typeReport).trim();
        try {
            return TypeReport.valueOf(normalizedType);
        } catch (IllegalArgumentException e) {
            throw new InputsException("Tipo de reporte no válido: " + normalizedType);
        }
    }

    public String communeValidator(String commune) throws Exception {
        return stringValidator("Comuna: ", commune);
    }

    public String neighborhoodValidator(String neighborhood) throws Exception {
        return stringValidator("Barrio: ", neighborhood);
    }

    public String addressValidator(String address) throws Exception {
        return stringValidator("Dirección: ", address);
    }

    public String imageValidator(String image) throws Exception {
        return stringValidator("Imagen: ", image);
    }

    public String descriptionValidator(String description) throws Exception {
        return stringValidator("Descripción: ", description);
    }

    public Status statusValidator(String status) throws Exception {
        String normalizedStatus = stringValidator("Estado: ", status).trim();
        try {
            return Status.valueOf(normalizedStatus);
        } catch (IllegalArgumentException e) {
            throw new InputsException("Estado no válido: " + normalizedStatus);
        }
    }

}
