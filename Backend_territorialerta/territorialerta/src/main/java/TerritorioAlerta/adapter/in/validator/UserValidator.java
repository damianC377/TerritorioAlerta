package TerritorioAlerta.adapter.in.validator;

import org.springframework.stereotype.Component;

@Component
/** Valida los campos específicos requeridos para registrar usuarios. */
public class UserValidator extends SimpleValidator{
    
    /** Valida la longitud y presencia del nombre. */
    public String nameValidator(String name) throws Exception {
    
        if (name.length() < 3 || name.length() > 50) {
            return "El nombre debe tener entre 3 y 50 caracteres.";
        }
        return stringValidator("Nombre de la persona: ", name);

    }

    /** Valida la longitud y presencia del apellido. */
    public String lastNameValidator(String lastName) throws Exception {
            if (lastName.length() < 3 || lastName.length() > 50) {
                return "El apellido debe tener entre 3 y 50 caracteres.";
            }
            return stringValidator("Apellido de la persona: ", lastName);
    }

    /** Valida el formato y normaliza el correo electrónico. */
    public String emailValidator(String email) throws Exception {
       stringValidator("Correo electrónico: ", email);
       if (!email.matches("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
        throw new Exception("El correo electrónico no tiene un formato válido");
       }
       return email.trim().toLowerCase();
    }

    /** Valida que la contraseña tenga una longitud permitida. */
    public String passwordValidator(String password) throws Exception {

        stringValidator("Contraseña: ", password);

        if (password.length() < 8 || password.length() > 20) {
            throw new Exception("La contraseña debe tener entre 8 y 20 caracteres.");
        }
        return password;
    }

    /** Valida y devuelve la comuna del usuario. */
    public String communeValidator(String commune) throws Exception {
        return stringValidator("Comuna: ", commune);
    }

    /** Valida y devuelve el barrio del usuario. */
    public String neighborhoodValidator(String neighborhood) throws Exception {
        return stringValidator("Barrio: ", neighborhood);
    }
}
