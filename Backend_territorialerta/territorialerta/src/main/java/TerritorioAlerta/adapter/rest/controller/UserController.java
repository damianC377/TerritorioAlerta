package TerritorioAlerta.adapter.rest.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import TerritorioAlerta.adapter.rest.mapper.AccidentReportRestMapper;
import TerritorioAlerta.adapter.rest.mapper.UserRestMapper;
import TerritorioAlerta.adapter.rest.request.Accident_reportRequest;
import TerritorioAlerta.adapter.rest.request.UserRequest;
import TerritorioAlerta.adapter.rest.response.AccidentReportResponse;
import TerritorioAlerta.adapter.rest.response.UserResponse;
import TerritorioAlerta.application.UseCase.UserUseCase;
import TerritorioAlerta.domain.model.Accident_report;
import TerritorioAlerta.domain.model.User;

@RestController
@RequestMapping("/api")
/** Expone los endpoints REST de usuarios y reportes de alerta. */
public class UserController {

    private final UserUseCase userUseCase;
    private final UserRestMapper userRestMapper;
    private final AccidentReportRestMapper accidentReportRestMapper;

    /** Construye el controller con el caso de uso y los mappers REST. */
    public UserController(UserUseCase userUseCase, UserRestMapper userRestMapper,
            AccidentReportRestMapper accidentReportRestMapper) {
        this.userUseCase = userUseCase;
        this.userRestMapper = userRestMapper;
        this.accidentReportRestMapper = accidentReportRestMapper;
    }

    // Registro de usuario (ciudadano)
    @PostMapping("/users")
    /** Registra un ciudadano a partir de los datos recibidos en el cuerpo HTTP. */
    public UserResponse register(@RequestBody UserRequest request) throws Exception {
        User user = userRestMapper.toDomain(request);
        User created = userUseCase.CreateUserRolDefault(user);
        return userRestMapper.toResponse(created);
    }

    // Crear reporte de accidente/incidente
    @PostMapping("/accident-reports")
    /** Crea un reporte de accidente o incidente y devuelve su representación REST. */
    public AccidentReportResponse createAccidentReport(@RequestBody Accident_reportRequest request) throws Exception {
        Accident_report accidentReport = accidentReportRestMapper.toDomain(request);
        Accident_report created = userUseCase.CreateAccidentReport(accidentReport);
        return accidentReportRestMapper.toResponse(created);
    }

    @GetMapping("/users/{id_user}")
    /** Devuelve el perfil asociado al usuario indicado en la URL. */
    public UserResponse getOwnProfile(@PathVariable Long id_user) throws Exception {
        return userRestMapper.toResponse(userUseCase.getOwnProfile(id_user));
    }

    @GetMapping("/accident-reports/{id_accident_report}")
    /** Devuelve un reporte de alerta identificado en la URL. */
    public AccidentReportResponse getAccidentReportById(@PathVariable Long id_accident_report) throws Exception {
        return accidentReportRestMapper.toResponse(userUseCase.getAccidentReportById(id_accident_report));
    }

    @GetMapping("/users/{id_user}/accident-reports")
    /** Devuelve los reportes creados por el usuario indicado. */
    public List<AccidentReportResponse> getOwnAccidentReports(@PathVariable Long id_user) {
        return userUseCase.getOwnAccidentReports(id_user).stream()
                .map(accidentReportRestMapper::toResponse)
                .toList();
    }

    @GetMapping("/accident-reports")
    /** Devuelve todos los reportes disponibles para la comunidad. */
    public List<AccidentReportResponse> getAllAccidentReports() {
        return userUseCase.getAllAccidentReports().stream()
                .map(accidentReportRestMapper::toResponse)
                .toList();
    }
}
