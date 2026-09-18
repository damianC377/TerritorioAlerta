package TerritorioAlerta.adapter.in.validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import TerritorioAlerta.domain.model.Enums.Status;
import TerritorioAlerta.domain.model.Enums.TypeReport;
import org.springframework.stereotype.Component;

@Component
public class AccidentReportValidator extends SimpleValidator {

    public long idUserValidator(String value) throws Exception {
        return longValidator("el ID del usuario que reporta", value);
    }

    public TypeReport typeReportValidator(String value) throws Exception {
        stringValidator("el tipo de reporte", value);
        try {
            return TypeReport.valueOf(value.trim());
        } catch (IllegalArgumentException e) {
            throw new Exception("El tipo de reporte debe ser uno de: road_accident, natural_disaster, solid_waste");
        }
    }

    public LocalDateTime dateValidator(String value) throws Exception {
        stringValidator("la fecha del incidente", value);
        try {
            return LocalDateTime.parse(value);
        } catch (DateTimeParseException e) {
            throw new Exception("La fecha del incidente debe tener formato ISO-8601, ej: 2025-03-14T10:15:30");
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

    public Status statusValidator(String value) throws Exception {
        stringValidator("el nivel de gravedad (status)", value);
        try {
            return Status.valueOf(value.trim());
        } catch (IllegalArgumentException e) {
            throw new Exception("El status debe ser uno de: Minor, Moderate, Severe");
        }
    }

}
