package TerritorioAlerta.domain.service;

import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.port.Accident_reportPort;
import TerritorioAlerta.domain.port.UserPort;

import java.util.List;

import TerritorioAlerta.domain.model.Accident_report;

public class UserService {

    // Creacion de puerto user
    private UserPort userPort;

    // Crear usuario
    public User createUser(User user) throws Exception{
        
        if (user.getId_user() != null && userPort.findById(user.getId_user()) != null) {
            throw new Exception("El usuario con ID " + user.getId_user() + " ya existe.");
        }

        if (userPort.findByEmail(user.getEmail()) != null) {
            throw new Exception("Usuario con email " + user.getEmail() + " ya existe.");
        }

        
        return userPort.save(user);
    }

    // Encontrar usuario
    public User findByIdUser(Long userId) throws Exception{
        User user = userPort.findById(userId);

        if (user == null) {
            throw new Exception("Usuario con ID " + userId + " no encontrado.");
        }

        return user;
    }

    // Puerto de accident_report
    // Creación de acción guardar dato en tabla
    private Accident_reportPort accidentReportPort;

    public Accident_report createAccidentReport(Accident_report accidentReport) throws Exception {
        if (accidentReport.getId_accident_report() != null && accidentReportPort.findById(accidentReport.getId_accident_report()) != null) {
            throw new Exception("El reporte de accidente con ID " + accidentReport.getId_accident_report() + " ya existe.");
        }

        if (accidentReport.getId_user() == null) {
            throw new Exception("El ID del usuario es requerido.");
        }

        return accidentReportPort.save(accidentReport);
    }

    // Encontrar por ID de reporte de accidente (Solo un 1, no una lista)
    public Accident_report findAccidentReportById(Long accidentReportId) throws Exception {
        Accident_report accidentReport = accidentReportPort.findById(accidentReportId);

        if (accidentReport == null) {
            throw new Exception("Reporte de accidente con ID " + accidentReportId + " no encontrado.");
        }

        return accidentReport;
    }

    // Listar todos los reportes de accidente
    public List<Accident_report> findAllAccidentReports() {
        return accidentReportPort.findAll();
    }

    // Listar reportes de accidente por ID de usuario
    public List<Accident_report> findAccidentReportsByUserId(int userId) {
        return accidentReportPort.findByIdUserList(userId);
    }
    
}
