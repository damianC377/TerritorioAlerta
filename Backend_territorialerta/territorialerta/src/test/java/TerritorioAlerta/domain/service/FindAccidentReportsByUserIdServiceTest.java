package TerritorioAlerta.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

/** Pruebas JUnit de reportes por usuario, conectadas a PostgreSQL. */
// Carga los componentes reales de Spring para probar también la consulta JPA.
@SpringBootTest
// Revierte los datos insertados por cada método de prueba.
@Transactional
class FindAccidentReportsByUserIdServiceTest {

    // Inyecta el servicio real que filtra reportes por ID de usuario.
    @Autowired
    private FindAccidentReportsByUserIdService findAccidentReportsByUserIdService;

    // Inyecta el servicio real que inserta reportes para los casos positivos.
    @Autowired
    private CreateAccidentReportService createAccidentReportService;

    // Inyecta el servicio real que crea usuarios dueños de los reportes.
    @Autowired
    private CreateUserService createUserService;

    // Comprueba que el servicio devuelve los dos reportes del usuario elegido.
    @Test
    void buscarReportesPorUsuario_dosReportes_devuelveAmbos() throws Exception {
        // Crea un usuario real en PostgreSQL.
        User user = createUserService.createUser(newUser());
        // Inserta el primer reporte asociado al usuario.
        createAccidentReportService.createAccidentReport(newReport(user.getId_user()));
        // Inserta el segundo reporte asociado al mismo usuario.
        createAccidentReportService.createAccidentReport(newReport(user.getId_user()));
        // Consulta los reportes usando el ID del usuario guardado.
        List<Accident_report> reports = findAccidentReportsByUserIdService
                .findAccidentReportsByUserId(user.getId_user());
        // Comprueba que los dos reportes se recuperaron.
        assertEquals(2, reports.size());
        // Comprueba que cada resultado pertenece al usuario solicitado.
        assertTrue(reports.stream().allMatch(report -> user.getId_user().equals(report.getId_user())));
        System.out.println("JUnit: reportes del usuario " + user.getId_user() + " consultados satisfactoriamente.");
    }

    // Comprueba que un único reporte también se filtra correctamente.
    @Test
    void buscarReportesPorUsuario_unReporte_devuelveUno() throws Exception {
        // Crea el usuario que será dueño del reporte.
        User user = createUserService.createUser(newUser());
        // Inserta un reporte asociado a ese usuario.
        createAccidentReportService.createAccidentReport(newReport(user.getId_user()));
        // Ejecuta la búsqueda por el ID real del usuario.
        List<Accident_report> reports = findAccidentReportsByUserIdService
                .findAccidentReportsByUserId(user.getId_user());
        // Comprueba que la lista contiene exactamente el reporte insertado.
        assertEquals(1, reports.size());
        System.out.println("JUnit: se encontró el reporte del usuario satisfactoriamente.");
    }

    // Comprueba que un ID sin reportes produce una lista vacía, no un error.
    @Test
    void buscarReportesPorUsuario_usuarioSinReportes_devuelveListaVacia() {
        // Busca por un ID que no corresponde a un usuario creado en esta prueba.
        List<Accident_report> reports = findAccidentReportsByUserIdService
                .findAccidentReportsByUserId(Long.MAX_VALUE);
        // Confirma el comportamiento definido por el servicio para cero resultados.
        assertTrue(reports.isEmpty());
        System.out.println("JUnit: no se encontraron reportes para el usuario consultado.");
    }

    // Prepara los datos válidos del usuario asociado a los reportes.
    private User newUser() {
        // Inicializa el usuario nuevo.
        User user = new User();
        // Establece el nombre de prueba.
        user.setName("JUnit");
        // Establece el apellido de prueba.
        user.setLastname("Integration");
        // Genera email único para evitar colisiones.
        user.setEmail("junit-" + UUID.randomUUID() + "@example.test");
        // Completa la contraseña del modelo.
        user.setPassword("test-password");
        // Define la comuna del usuario.
        user.setCommune("Test commune");
        // Define el barrio del usuario.
        user.setNeighborhood("Test neighborhood");
        // Asigna un rol válido.
        user.setRole(Role.User);
        // Define la fecha de creación.
        user.setCreation_date(LocalDateTime.now());
        // Devuelve el usuario listo para persistir.
        return user;
    }
    // Construye el reporte que se guardará vinculado al usuario recibido.
    private Accident_report newReport(Long userId) {
        // Inicializa el reporte.
        Accident_report report = new Accident_report();
        // Asigna el ID del usuario como propietario.
        report.setId_user(userId);
        // Define la fecha del incidente.
        report.setDate(LocalDateTime.now());
        // Selecciona una categoría válida.
        report.setType_report(TypeReport.road_accident);
        // Completa la comuna del incidente.
        report.setCommune("Test commune");
        // Completa el barrio del incidente.
        report.setNeighborhood("Test neighborhood");
        // Completa la dirección del incidente.
        report.setAddress("Test address");
        // Completa el dato de imagen de prueba.
        report.setImage("test-image");
        // Completa una descripción de prueba.
        report.setDescription("JUnit integration report");
        // Selecciona un estado válido.
        report.setStatus(Status.Minor);
        // Define la fecha de creación del reporte.
        report.setCreation_date(LocalDateTime.now());
        // Devuelve el reporte listo para guardar.
        return report;
    }
}