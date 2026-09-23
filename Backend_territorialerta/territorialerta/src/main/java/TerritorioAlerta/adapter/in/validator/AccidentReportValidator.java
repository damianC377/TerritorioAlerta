package TerritorioAlerta.adapter.in.validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import TerritorioAlerta.domain.model.Enums.Status;
import TerritorioAlerta.domain.model.Enums.TypeReport;
import org.springframework.stereotype.Component;

@Component
/** Valida los campos específicos de un reporte de accidente o incidente. */
public class AccidentReportValidator extends SimpleValidator {

    /** Convierte y valida el identificador del usuario reportante. */
    public long idUserValidator(String value) throws Exception {
        return longValidator("el ID del usuario que reporta", value);
    }

    /** Convierte el tipo de reporte al enum permitido por el dominio. */
    public TypeReport typeReportValidator(String value) throws Exception {
        stringValidator("el tipo de reporte", value);
        try {
            return TypeReport.valueOf(value.trim());
        } catch (IllegalArgumentException e) {
            throw new Exception("El tipo de reporte debe ser uno de: road_accident, natural_disaster, solid_waste");
        }
    }

    /** Convierte la fecha ISO-8601 al tipo temporal del dominio. */
    public LocalDateTime dateValidator(String value) throws Exception {
        stringValidator("la fecha del incidente", value);
        try {
            return LocalDateTime.parse(value);
        } catch (DateTimeParseException e) {
            throw new Exception("La fecha del incidente debe tener formato ISO-8601, ej: 2025-03-14T10:15:30");
        }
    }

    /** Valida y devuelve la comuna del reporte. */
    public String communeValidator(String commune) throws Exception {
        return stringValidator("Comuna: ", commune);
    }

    /** Valida y devuelve el barrio del reporte. */
    public String neighborhoodValidator(String neighborhood) throws Exception {
        return stringValidator("Barrio: ", neighborhood);
    }

    /** Valida y devuelve la dirección del reporte. */
    public String addressValidator(String address) throws Exception {
        return stringValidator("Dirección: ", address);
    }

    /** Valida y devuelve la referencia de imagen del reporte. */
    public String imageValidator(String image) throws Exception {
        return stringValidator("Imagen: ", image);
    }

    /** Valida y devuelve la descripción del reporte. */
    public String descriptionValidator(String description) throws Exception {
        return stringValidator("Descripción: ", description);
    }

    /** Convierte el nivel de gravedad al enum permitido por el dominio. */
    public Status statusValidator(String value) throws Exception {
        stringValidator("el nivel de gravedad (status)", value);
        try {
            return Status.valueOf(value.trim());
        } catch (IllegalArgumentException e) {
            throw new Exception("El status debe ser uno de: Minor, Moderate, Severe");
        }
    }

}
