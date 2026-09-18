package TerritorioAlerta.adapter.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
public class UserController {

    @Autowired
    private UserUseCase userUseCase;
    @Autowired
    private UserRestMapper userRestMapper;
    @Autowired
    private AccidentReportRestMapper accidentReportRestMapper;

    // Registro de usuario (ciudadano)
    @PostMapping("/users")
    public UserResponse register(@RequestBody UserRequest request) throws Exception {
        User user = userRestMapper.toDomain(request);
        User created = userUseCase.CreateUserRolDefault(user);
        return userRestMapper.toResponse(created);
    }

    // Crear reporte de accidente/incidente
    @PostMapping("/accident-reports")
    public AccidentReportResponse createAccidentReport(@RequestBody Accident_reportRequest request) throws Exception {
        Accident_report accidentReport = accidentReportRestMapper.toDomain(request);
        Accident_report created = userUseCase.CreateAccidentReport(accidentReport);
        return accidentReportRestMapper.toResponse(created);
    }
}
