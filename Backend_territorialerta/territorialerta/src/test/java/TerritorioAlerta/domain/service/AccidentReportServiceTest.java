package TerritorioAlerta.domain.service;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

/** Pruebas JUnit que guardan reportes usando el servicio y PostgreSQL reales. */
// Inicia la aplicación para usar los adaptadores y repositorios reales.
@SpringBootTest
// Revierte los inserts de cada prueba al finalizar.
@Transactional
class AccidentReportServiceTest {

    // Inyecta el servicio real que crea reportes.
    @Autowired
    private CreateAccidentReportService accidentReportService;

    // Inyecta el servicio real necesario para crear el usuario dueño del reporte.
    @Autowired
    private CreateUserService userService;

    // Comprueba que un reporte válido se guarda y recibe ID autoincremental.
    @Test
    void crearReporte_datosValidos_seGuardaCorrectamente() throws Exception {
        // Crea primero un usuario real en PostgreSQL.
        User user = userService.createUser(newUser());
        // Construye un reporte asociado al ID que generó la base de datos.
        Accident_report report = newReport(user.getId_user());
        // Guarda el reporte pasando por el servicio y el adaptador reales.
        Accident_report saved = accidentReportService.createAccidentReport(report);
        // Confirma que PostgreSQL generó la clave del reporte.
        assertNotNull(saved.getId_accident_report());
        // Confirma que el reporte conserva la referencia al usuario.
        assertEquals(user.getId_user(), saved.getId_user());
    }

    // Comprueba que dos reportes válidos reciben IDs diferentes.
    @Test
    void crearReporte_dosReportesValidos_asignaIdsDistintos() throws Exception {
        // Crea un usuario real para asociarlo a ambos reportes.
        User user = userService.createUser(newUser());
        // Guarda el primer reporte a nombre del usuario.
        Accident_report first = accidentReportService.createAccidentReport(newReport(user.getId_user()));
        // Guarda un segundo reporte para el mismo usuario.
        Accident_report second = accidentReportService.createAccidentReport(newReport(user.getId_user()));
        // Comprueba que el primer reporte obtuvo ID.
        assertNotNull(first.getId_accident_report());
        // Comprueba que el segundo reporte obtuvo ID.
        assertNotNull(second.getId_accident_report());
        // Comprueba que cada insert recibió una clave distinta.
        assertNotEquals(first.getId_accident_report(), second.getId_accident_report());
    }

    // Comprueba la regla negativa cuando falta el ID del usuario.
    @Test
    void crearReporte_sinIdUsuario_lanzaExcepcion() {
        // Construye un reporte sin propietario.
        Accident_report report = newReport(null);
        // Confirma que el servicio rechaza el reporte antes de guardarlo.
        assertThrows(Exception.class, () -> accidentReportService.createAccidentReport(report));
    }

    // Construye un usuario válido con un email único para esta ejecución.
    private User newUser() {
        // Crea un objeto de usuario nuevo.
        User user = new User();
        // Define un nombre de prueba.
        user.setName("JUnit");
        // Define un apellido de prueba.
        user.setLastname("Integration");
        // Genera un email único para que no choque con otros registros.
        user.setEmail("junit-" + UUID.randomUUID() + "@example.test");
        // Completa la contraseña de prueba.
        user.setPassword("test-password");
        // Completa la comuna del usuario.
        user.setCommune("Test commune");
        // Completa el barrio del usuario.
        user.setNeighborhood("Test neighborhood");
        // Asigna un rol válido.
        user.setRole(Role.User);
        // Define la fecha de creación.
        user.setCreation_date(LocalDateTime.now());
        // Devuelve el usuario preparado.
        return user;
    }

    // Construye un reporte válido, asociado al ID de usuario recibido.
    private Accident_report newReport(Long userId) {
        // Crea una instancia nueva del modelo de reporte.
        Accident_report report = new Accident_report();
        // Relaciona el reporte con el usuario de la prueba.
        report.setId_user(userId);
        // Define la fecha del incidente.
        report.setDate(LocalDateTime.now());
        // Selecciona un tipo de reporte válido.
        report.setType_report(TypeReport.road_accident);
        // Completa la comuna del incidente.
        report.setCommune("Test commune");
        // Completa el barrio del incidente.
        report.setNeighborhood("Test neighborhood");
        // Completa la dirección del incidente.
        report.setAddress("Test address");
        // Completa el valor de imagen esperado por el modelo.
        report.setImage("test-image");
        // Agrega una descripción de prueba.
        report.setDescription("JUnit integration report");
        // Selecciona un estado válido.
        report.setStatus(Status.Minor);
        // Define la fecha de creación del reporte.
        report.setCreation_date(LocalDateTime.now());
        // Devuelve el reporte listo para guardar.
        return report;
    }
    // ==================== AQUÍ EMPIEZAN LAS PRUEBAS MOCKITO ====================

    // Caso Mockito exitoso: guarda un reporte nuevo que tiene usuario.
    @Test
    void mockito_crearReporte_usuarioIndicado_guardaReporte() throws Exception {
        // Crea un puerto falso para no llegar al repositorio PostgreSQL.
        TerritorioAlerta.domain.port.Accident_reportPort mockPort = org.mockito.Mockito.mock(
                TerritorioAlerta.domain.port.Accident_reportPort.class);
        // Construye el servicio con el puerto simulado.
        CreateAccidentReportService mockService = new CreateAccidentReportService(mockPort);
        // Crea el reporte que se enviará al servicio.
        Accident_report report = new Accident_report();
        // Asigna el ID de usuario requerido por la regla de negocio.
        report.setId_user(8L);
        // Simula que el puerto devuelve el reporte después de guardarlo.
        org.mockito.Mockito.when(mockPort.save(report)).thenReturn(report);
        // Ejecuta la creación del reporte con el servicio real.
        Accident_report saved = mockService.createAccidentReport(report);
        // Comprueba que se devolvió el reporte preparado.
        org.junit.jupiter.api.Assertions.assertSame(report, saved);
        // Comprueba que el puerto recibió la operación de guardado.
        org.mockito.Mockito.verify(mockPort).save(report);
    }

    // Caso Mockito exitoso: un ID de reporte disponible se puede guardar.
    @Test
    void mockito_crearReporte_idNoExistente_guardaReporte() throws Exception {
        // Crea un puerto simulado para controlar la consulta y el guardado.
        TerritorioAlerta.domain.port.Accident_reportPort mockPort = org.mockito.Mockito.mock(
                TerritorioAlerta.domain.port.Accident_reportPort.class);
        // Construye una instancia de servicio que depende del mock.
        CreateAccidentReportService mockService = new CreateAccidentReportService(mockPort);
        // Prepara el reporte con un ID explícito.
        Accident_report report = new Accident_report();
        // Asigna el ID que se comprobará como disponible.
        report.setId_accident_report(63L);
        // Asigna un usuario para que el reporte cumpla la validación.
        report.setId_user(8L);
        // Simula que no existe reporte con ese ID.
        org.mockito.Mockito.when(mockPort.findById(63L)).thenReturn(null);
        // Simula la respuesta exitosa del guardado.
        org.mockito.Mockito.when(mockPort.save(report)).thenReturn(report);
        // Ejecuta la creación del reporte.
        Accident_report saved = mockService.createAccidentReport(report);
        // Comprueba que el ID explícito se conserva en el resultado.
        org.junit.jupiter.api.Assertions.assertEquals(63L, saved.getId_accident_report());
        // Comprueba que el servicio preguntó si el ID existía.
        org.mockito.Mockito.verify(mockPort).findById(63L);
        // Comprueba que después guardó el reporte válido.
        org.mockito.Mockito.verify(mockPort).save(report);
    }

    // Caso Mockito negativo: no crea un reporte sin el ID del usuario.
    @Test
    void mockito_crearReporte_sinIdUsuario_lanzaExcepcion() {
        // Crea un puerto falso para observar si se intenta guardar.
        TerritorioAlerta.domain.port.Accident_reportPort mockPort = org.mockito.Mockito.mock(
                TerritorioAlerta.domain.port.Accident_reportPort.class);
        // Construye el servicio con la dependencia simulada.
        CreateAccidentReportService mockService = new CreateAccidentReportService(mockPort);
        // Prepara un reporte sin asignar su propietario.
        Accident_report report = new Accident_report();
        // Comprueba que el servicio lanza una excepción de validación.
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class,
                () -> mockService.createAccidentReport(report));
        // Comprueba que el reporte inválido nunca se envió a guardar.
        org.mockito.Mockito.verify(mockPort, org.mockito.Mockito.never())
                .save(org.mockito.ArgumentMatchers.any(Accident_report.class));
    }
}