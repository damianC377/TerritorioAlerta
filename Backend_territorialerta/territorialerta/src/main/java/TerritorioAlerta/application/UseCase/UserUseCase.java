package TerritorioAlerta.application.UseCase;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import TerritorioAlerta.domain.model.Accident_report;
import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.model.Enums.Role;
import TerritorioAlerta.domain.service.CreateAccidentReportService;
import TerritorioAlerta.domain.service.CreateUserService;
import TerritorioAlerta.domain.service.FindAccidentReportByIdService;
import TerritorioAlerta.domain.service.FindAccidentReportsByUserIdService;
import TerritorioAlerta.domain.service.FindAllAccidentReportsService;
import TerritorioAlerta.domain.service.FindByIdUserService;

@Service
/** Orquesta los casos de uso de usuarios y reportes de alerta. */
public class UserUseCase {
    
    //Servicios y lógica de negocio relacionados con los usuarios se implementarán aquí.
    private final CreateUserService createUserService;
    private final CreateAccidentReportService createAccidentReportService;
    private final FindByIdUserService findByIdUserService;
    private final FindAccidentReportByIdService findAccidentReportByIdService;
    private final FindAccidentReportsByUserIdService findAccidentReportsByUserIdService;
    private final FindAllAccidentReportsService findAllAccidentReportsService;

    /** Construye el orquestador con sus servicios de creación y consulta. */
    public UserUseCase(
            CreateUserService createUserService,
            CreateAccidentReportService createAccidentReportService,
            FindByIdUserService findByIdUserService,
            FindAccidentReportByIdService findAccidentReportByIdService,
            FindAccidentReportsByUserIdService findAccidentReportsByUserIdService,
            FindAllAccidentReportsService findAllAccidentReportsService) {
        this.createUserService = createUserService;
        this.createAccidentReportService = createAccidentReportService;
        this.findByIdUserService = findByIdUserService;
        this.findAccidentReportByIdService = findAccidentReportByIdService;
        this.findAccidentReportsByUserIdService = findAccidentReportsByUserIdService;
        this.findAllAccidentReportsService = findAllAccidentReportsService;
    }

    // Asignador de rol, predeterminado para el caso de uso de creación de usuario.
    /** Asigna el rol ciudadano y la fecha de creación antes de registrar al usuario. */
    public User CreateUserRolDefault(User user) throws Exception {
        
        
        user.setRole(Role.User);
        user.setCreation_date(LocalDateTime.now());


        return createUserService.createUser(user);
    }

    // Creacion de reporte de accidente
    /** Asigna la fecha de creación y registra un reporte de accidente. */
    public Accident_report CreateAccidentReport(Accident_report accidentReport) throws Exception {
        accidentReport.setCreation_date(LocalDateTime.now());
        return createAccidentReportService.createAccidentReport(accidentReport);
    }

    /** Obtiene el perfil del usuario identificado por el ID recibido. */
    public User getOwnProfile(Long id_user) throws Exception {
        return findByIdUserService.findByIdUser(id_user);
    }

    /** Obtiene un reporte de accidente por su ID. */
    public Accident_report getAccidentReportById(Long id_accident_report) throws Exception {
        return findAccidentReportByIdService.findAccidentReportById(id_accident_report);
    }

    /** Obtiene los reportes pertenecientes al usuario indicado. */
    public List<Accident_report> getOwnAccidentReports(Long id_user) {
        return findAccidentReportsByUserIdService.findAccidentReportsByUserId(id_user);
    }

    /** Obtiene todos los reportes para la consulta comunitaria. */
    public List<Accident_report> getAllAccidentReports() {
        return findAllAccidentReportsService.findAllAccidentReports();
    }
}
