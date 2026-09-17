package TerritorioAlerta.adapter.in.builder;

import TerritorioAlerta.adapter.in.validator.UserValidator;
import TerritorioAlerta.domain.model.User;

public class UserBuilder {

    private UserValidator userValidator;
    
    public User buildUser(String name, String lastname, String email, String password, String commune, String neighborhood) throws Exception {
        
        User user = new User();

        // Normalizamos y validamos los datos del usuario antes de asignarlos al objeto User

        user.setName(userValidator.nameValidator(name));
        user.setLastname(userValidator.lastNameValidator(lastname));
        user.setEmail(userValidator.emailValidator(email));
        user.setPassword(userValidator.passwordValidator(password));
        user.setCommune(userValidator.communeValidator(commune));
        user.setNeighborhood(userValidator.neighborhoodValidator(neighborhood));

        return user;
    }
}
