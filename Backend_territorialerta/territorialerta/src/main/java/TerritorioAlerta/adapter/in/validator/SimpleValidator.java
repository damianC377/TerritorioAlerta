package TerritorioAlerta.adapter.in.validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import TerritorioAlerta.application.exception.InputsException;
import org.springframework.stereotype.Component;

@Component
/** Proporciona validaciones primitivas reutilizables para los datos de entrada. */
public class SimpleValidator {
 /** Rechaza valores nulos o vacíos y devuelve el texto válido. */
 public String stringValidator(String element, String value) throws Exception {
        if (value == null || value.equals("")) {
            throw new InputsException(element + " no puede tener un valor vacio o nulo");
        }
        return value;
    }

    /** Convierte un texto a entero después de validar que no esté vacío. */
    public int integerValidator(String element, String value) throws Exception {
        stringValidator(element, value);
        try {
            int intValue = Integer.parseInt(value);
            return intValue;
        } catch (Exception e) {
            throw new InputsException(element + " debe ser un valor numerico");
        }
    }

    /** Convierte un texto a long después de validar que no esté vacío. */
    public long longValidator(String element, String value) throws Exception {
        stringValidator(element, value);
        try {
            long longValue = Long.parseLong(value);
            return longValue;
        } catch (Exception e) {
            throw new InputsException(element + " debe ser un valor numerico");
        }
    }
    /** Convierte un texto a double después de validar que no esté vacío. */
    public double doubleValidator(String element, String value) throws Exception {
        stringValidator(element, value);
        try {
            double doubleValue = Double.parseDouble(value);
            return doubleValue;
        } catch (Exception e) {
            throw new InputsException(element + " debe ser un valor numerico");
        }
    }

    /** Convierte una fecha con formato dd/MM/yyyy después de validarla. */
    public LocalDate dateValidator(String element, String value) throws Exception {
        if (value == null || value.isBlank()) {
        throw new InputsException(element + " no puede ser nulo ni vacío");
        }

        try {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(value, formatter);
        } catch (DateTimeParseException e) {
        throw new InputsException(element + " no tiene un formato de fecha válido (dd/MM/yyyy)");
        }
    }
}
