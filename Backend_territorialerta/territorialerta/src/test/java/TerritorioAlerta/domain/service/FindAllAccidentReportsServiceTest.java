package TerritorioAlerta.domain.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import TerritorioAlerta.domain.model.Accident_report;
import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.model.Enums.Role;
import TerritorioAlerta.domain.model.Enums.Status;
import TerritorioAlerta.domain.model.Enums.TypeReport;

/** Pruebas JUnit del listado general usando PostgreSQL real. */
// Carga la aplicación para ejecutar el servicio conectado al repositorio real.
@SpringBootTest
// Revierte los registros creados por cada prueba al terminar.
@Transactional
class FindAllAccidentReportsServiceTest {

    // Inyecta el servicio real de listado de reportes.
    @Autowired
    private FindAllAccidentReportsService findAllAccidentReportsService;

    // Inyecta el servicio real que guarda reportes durante la preparación.
    @Autowired
    private CreateAccidentReportService createAccidentReportService;

    // Inyecta el servicio real que prepara un usuario propietario.
    @Autowired
    private CreateUserService createUserService;

    // Comprueba que el listado contiene el reporte guardado en esta prueba.
    @Test
    void listarReportes_reportePersistido_incluyeReporte() throws Exception {
        // Crea un usuario real para el reporte de prueba.
        User user = createUserService.createUser(newUser());
        // Inserta el reporte y conserva su ID de base de datos.
        Accident_report saved = createAccidentReportService.createAccidentReport(newReport(user.getId_user()));
        // Pide al servicio todos los reportes persistidos.
        List<Accident_report> reports = findAllAccidentReportsService.findAllAccidentReports();
        // Confirma que el servicio devolvió una lista.
        assertNotNull(reports);
        // Confirma que la lista incluye el reporte insertado por este test.
        assertTrue(reports.stream().anyMatch(report ->
                saved.getId_accident_report().equals(report.getId_accident_report())));
        System.out.println("JUnit: reporte incluido en el listado satisfactoriamente.");
    }

    // Comprueba que la lista general puede devolver varios reportes guardados.
    @Test
    void listarReportes_variosReportesPersistidos_devuelveColeccion() throws Exception {
        // Crea un usuario real para los reportes.
        User user = createUserService.createUser(newUser());
        // Inserta el primer reporte.
        createAccidentReportService.createAccidentReport(newReport(user.getId_user()));
        // Inserta el segundo reporte.
        createAccidentReportService.createAccidentReport(newReport(user.getId_user()));
        // Consulta todos los reportes mediante el servicio bajo prueba.
        List<Accident_report> reports = findAllAccidentReportsService.findAllAccidentReports();
        // Confirma que el resultado no es nulo.
        assertNotNull(reports);
        // Confirma que están incluidos al menos los dos reportes insertados.
        assertTrue(reports.size() >= 2);
        System.out.println("JUnit: listado de reportes obtenido satisfactoriamente.");
    }

    // El listado no define errores para entradas inválidas; estos helpers crean datos reales.
    private User newUser() {
        // Inicializa el modelo de usuario para la base de datos.
        User user = new User();
        // Completa el nombre del usuario de prueba.
        user.setName("JUnit");
        // Completa el apellido del usuario de prueba.
        user.setLastname("Integration");
        // Genera un email irrepetible para evitar duplicados.
        user.setEmail("junit-" + UUID.randomUUID() + "@example.test");
        // Completa la contraseña de prueba.
        user.setPassword("test-password");
        // Completa la comuna.
        user.setCommune("Test commune");
        // Completa el barrio.
        user.setNeighborhood("Test neighborhood");
        // Asigna un rol válido.
        user.setRole(Role.User);
        // Define la fecha de creación.
        user.setCreation_date(LocalDateTime.now());
        // Devuelve el usuario listo para guardar.
        return user;
    }

    // Prepara un reporte válido asociado al usuario indicado.
    private Accident_report newReport(Long userId) {
        // Inicializa el modelo de reporte.
        Accident_report report = new Accident_report();
        // Asocia el reporte con el usuario de la prueba.
        report.setId_user(userId);
        // Establece la fecha del accidente.
        report.setDate(LocalDateTime.now());
        // Define un tipo permitido por el dominio.
        report.setType_report(TypeReport.road_accident);
        // Completa la comuna del incidente.
        report.setCommune("Test commune");
        // Completa el barrio del incidente.
        report.setNeighborhood("Test neighborhood");
        // Completa la dirección del incidente.
        report.setAddress("Test address");
        // Completa la imagen de prueba.
        report.setImage("test-image");
        // Completa la descripción de prueba.
        report.setDescription("JUnit integration report");
        // Asigna un estado permitido.
        report.setStatus(Status.Minor);
        // Define la fecha de creación.
        report.setCreation_date(LocalDateTime.now());
        // Devuelve el reporte preparado.
        return report;
    }
}