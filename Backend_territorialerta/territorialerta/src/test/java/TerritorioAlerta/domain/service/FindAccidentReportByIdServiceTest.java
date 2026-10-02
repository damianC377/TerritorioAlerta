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
    }

    // Comprueba que el servicio informa cuando se solicita un reporte inexistente.
    @Test
    void buscarReporte_idInexistente_lanzaExcepcion() {
        // Confirma que Long.MAX_VALUE no corresponde a un ID generado existente.
        assertThrows(Exception.class,
                () -> findAccidentReportByIdService.findAccidentReportById(Long.MAX_VALUE));
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
    // ==================== AQUÍ EMPIEZAN LAS PRUEBAS MOCKITO ====================

    // Caso Mockito exitoso: devuelve el reporte encontrado por su ID.
    @Test
    void mockito_buscarReporte_existente_devuelveReporte() throws Exception {
        // Crea un puerto falso para definir la respuesta de la búsqueda.
        TerritorioAlerta.domain.port.Accident_reportPort mockPort = org.mockito.Mockito.mock(
                TerritorioAlerta.domain.port.Accident_reportPort.class);
        // Construye el servicio de consulta con la dependencia simulada.
        FindAccidentReportByIdService mockService = new FindAccidentReportByIdService(mockPort);
        // Prepara el reporte que debería encontrarse.
        Accident_report expected = new Accident_report();
        // Asigna el ID consultado al reporte esperado.
        expected.setId_accident_report(25L);
        // Configura el mock para devolver el reporte con ID 25.
        org.mockito.Mockito.when(mockPort.findById(25L)).thenReturn(expected);
        // Ejecuta la búsqueda por medio del servicio.
        Accident_report result = mockService.findAccidentReportById(25L);
        // Comprueba que se recibió el mismo reporte esperado.
        org.junit.jupiter.api.Assertions.assertSame(expected, result);
        // Comprueba que el servicio consultó el ID correcto.
        org.mockito.Mockito.verify(mockPort).findById(25L);
    }

    // Caso Mockito exitoso adicional: recupera otro reporte por su identificador.
    @Test
    void mockito_buscarReporte_otroIdExistente_devuelveReporte() throws Exception {
        // Crea un mock independiente para el segundo caso.
        TerritorioAlerta.domain.port.Accident_reportPort mockPort = org.mockito.Mockito.mock(
                TerritorioAlerta.domain.port.Accident_reportPort.class);
        // Construye el servicio conectado a ese mock.
        FindAccidentReportByIdService mockService = new FindAccidentReportByIdService(mockPort);
        // Prepara el reporte que debe devolver la consulta.
        Accident_report expected = new Accident_report();
        // Asigna el identificador del segundo escenario.
        expected.setId_accident_report(36L);
        // Configura la respuesta simulada para el ID 36.
        org.mockito.Mockito.when(mockPort.findById(36L)).thenReturn(expected);
        // Ejecuta la consulta del segundo reporte.
        Accident_report result = mockService.findAccidentReportById(36L);
        // Comprueba que el ID del resultado corresponde a la búsqueda.
        org.junit.jupiter.api.Assertions.assertEquals(36L, result.getId_accident_report());
        // Comprueba que el puerto recibió el identificador esperado.
        org.mockito.Mockito.verify(mockPort).findById(36L);
    }

    // Caso Mockito negativo: informa si el puerto no encuentra el reporte.
    @Test
    void mockito_buscarReporte_inexistente_lanzaExcepcion() {
        // Crea un puerto simulado para representar una búsqueda sin resultados.
        TerritorioAlerta.domain.port.Accident_reportPort mockPort = org.mockito.Mockito.mock(
                TerritorioAlerta.domain.port.Accident_reportPort.class);
        // Construye el servicio con el puerto falso.
        FindAccidentReportByIdService mockService = new FindAccidentReportByIdService(mockPort);
        // Configura la respuesta nula para el reporte inexistente.
        org.mockito.Mockito.when(mockPort.findById(404L)).thenReturn(null);
        // Comprueba que el servicio lanza una excepción ante la ausencia.
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class,
                () -> mockService.findAccidentReportById(404L));
        // Comprueba que la consulta se envió al puerto.
        org.mockito.Mockito.verify(mockPort).findById(404L);
    }
}