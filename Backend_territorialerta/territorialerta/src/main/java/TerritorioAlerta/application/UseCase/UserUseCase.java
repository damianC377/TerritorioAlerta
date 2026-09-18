package TerritorioAlerta.application.UseCase;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import TerritorioAlerta.domain.model.Accident_report;
import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.model.Enums.Role;
import TerritorioAlerta.domain.service.CreateAccidentReportService;
import TerritorioAlerta.domain.service.CreateUserService;

@Service
public class UserUseCase {
    
    //Servicios y lógica de negocio relacionados con los usuarios se implementarán aquí.
    @Autowired
    private CreateUserService createUserService;
    
    @Autowired
    private CreateAccidentReportService createAccidentReportService;

    // Asignador de rol, predeterminado para el caso de uso de creación de usuario.
    public User CreateUserRolDefault(User user) throws Exception {
        
        user.setRole(Role.User);
        user.setCreation_date(LocalDateTime.now());


        return createUserService.createUser(user);
    }

    // Creacion de reporte de accidente
    public Accident_report CreateAccidentReport(Accident_report accidentReport) throws Exception {
        accidentReport.setCreation_date(LocalDateTime.now());
        return createAccidentReportService.createAccidentReport(accidentReport);
    }
}
