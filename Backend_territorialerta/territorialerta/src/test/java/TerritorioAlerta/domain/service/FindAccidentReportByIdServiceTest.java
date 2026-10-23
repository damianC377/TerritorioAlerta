package TerritorioAlerta.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
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

/** Pruebas JUnit del servicio de búsqueda de reportes con PostgreSQL real. */
// Carga los servicios, adaptadores y repositorios reales de la aplicación.
@SpringBootTest
// Revierte los registros insertados en cada método de prueba.
@Transactional
class FindAccidentReportByIdServiceTest {

    // Inyecta el servicio real que busca reportes por su identificador.
    @Autowired
    private FindAccidentReportByIdService findAccidentReportByIdService;

    // Inyecta el servicio real que persiste reportes para preparar los casos.
    @Autowired
    private CreateAccidentReportService createAccidentReportService;

    // Inyecta el servicio real que crea el usuario asociado al reporte.
    @Autowired
    private CreateUserService createUserService;

    // Comprueba que un reporte insertado puede encontrarse por el ID generado.
    @Test
    void buscarReporte_idExistente_devuelveReporte() throws Exception {
        // Crea un usuario real para asociarlo al reporte.
        User user = createUserService.createUser(newUser());
        // Inserta un reporte y conserva el ID asignado por PostgreSQL.
        Accident_report saved = createAccidentReportService.createAccidentReport(newReport(user.getId_user()));
        // Busca el reporte guardado usando su ID autoincremental.
        Accident_report found = findAccidentReportByIdService.findAccidentReportById(saved.getId_accident_report());
        // Comprueba que la clave devuelta coincide con la persistida.
        assertEquals(saved.getId_accident_report(), found.getId_accident_report());
        // Comprueba que se mantiene el usuario asociado.
        assertEquals(saved.getId_user(), found.getId_user());
        System.out.println("JUnit: reporte encontrado satisfactoriamente con ID " + found.getId_accident_report() + ".");
    }

    // Comprueba que la búsqueda también identifica correctamente un segundo reporte.
    @Test
    void buscarReporte_otroIdExistente_devuelveReporteCorrecto() throws Exception {
        // Crea un usuario real para los reportes.
        User user = createUserService.createUser(newUser());
        // Inserta un primer reporte que no será el objetivo de la búsqueda.
        createAccidentReportService.createAccidentReport(newReport(user.getId_user()));
        // Inserta el reporte objetivo y guarda el ID generado.
        Accident_report expected = createAccidentReportService.createAccidentReport(newReport(user.getId_user()));
        // Recupera el reporte objetivo desde PostgreSQL.
        Accident_report found = findAccidentReportByIdService
                .findAccidentReportById(expected.getId_accident_report());
        // Confirma que el resultado corresponde al ID buscado.
        assertEquals(expected.getId_accident_report(), found.getId_accident_report());
        System.out.println("JUnit: se encontró correctamente el segundo reporte.");
    }

    // Comprueba que el servicio informa cuando se solicita un reporte inexistente.
    @Test
    void buscarReporte_idInexistente_lanzaExcepcion() {
        // Confirma que Long.MAX_VALUE no corresponde a un ID generado existente.
        assertThrows(Exception.class,
                () -> findAccidentReportByIdService.findAccidentReportById(Long.MAX_VALUE));
        System.out.println("JUnit: no se pudo encontrar el reporte solicitado.");
    }

    // Crea un usuario de prueba con datos completos y un email irrepetible.
    private User newUser() {
        // Inicializa el modelo de usuario.
        User user = new User();
        // Establece el nombre de prueba.
        user.setName("JUnit");
        // Establece el apellido de prueba.
        user.setLastname("Integration");
        // Genera un email distinto para cada ejecución.
        user.setEmail("junit-" + UUID.randomUUID() + "@example.test");
        // Establece una contraseña para la fila de prueba.
        user.setPassword("test-password");
        // Establece la comuna del usuario.
        user.setCommune("Test commune");
        // Establece el barrio del usuario.
        user.setNeighborhood("Test neighborhood");
        // Asigna un rol admitido por el dominio.
        user.setRole(Role.User);
        // Establece la fecha de creación.
        user.setCreation_date(LocalDateTime.now());
        // Devuelve el usuario listo para persistir.
        return user;
    }

    // Crea un reporte válido y lo relaciona con el ID de usuario indicado.
    private Accident_report newReport(Long userId) {
        // Inicializa el modelo de reporte.
        Accident_report report = new Accident_report();
        // Asigna el propietario del reporte.
        report.setId_user(userId);
        // Define la fecha del incidente.
        report.setDate(LocalDateTime.now());
        // Selecciona un tipo de reporte permitido.
        report.setType_report(TypeReport.road_accident);
        // Establece la comuna del incidente.
        report.setCommune("Test commune");
        // Establece el barrio del incidente.
        report.setNeighborhood("Test neighborhood");
        // Establece la dirección de prueba.
        report.setAddress("Test address");
        // Establece el nombre de imagen de prueba.
        report.setImage("test-image");
        // Agrega una descripción de prueba.
        report.setDescription("JUnit integration report");
        // Selecciona un estado permitido.
        report.setStatus(Status.Minor);
        // Establece la fecha de creación.
        report.setCreation_date(LocalDateTime.now());
        // Devuelve el reporte listo para persistir.
        return report;
    }
}