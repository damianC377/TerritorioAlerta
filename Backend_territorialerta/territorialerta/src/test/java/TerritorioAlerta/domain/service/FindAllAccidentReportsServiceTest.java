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
        // ==================== AQUÍ EMPIEZAN LAS PRUEBAS MOCKITO ====================

        // Caso Mockito exitoso: devuelve una lista con reportes.
        @Test
        void mockito_listarReportes_listaConDatos_devuelveLista() {
        // Crea un puerto falso para controlar el resultado del listado.
        TerritorioAlerta.domain.port.Accident_reportPort mockPort = org.mockito.Mockito.mock(
            TerritorioAlerta.domain.port.Accident_reportPort.class);
        // Construye el servicio conectado al puerto simulado.
        FindAllAccidentReportsService mockService = new FindAllAccidentReportsService(mockPort);
        // Prepara una lista no vacía para devolver desde el mock.
        java.util.List<Accident_report> expected = java.util.List.of(
            new Accident_report(), new Accident_report());
        // Configura la respuesta del listado general.
        org.mockito.Mockito.when(mockPort.findAll()).thenReturn(expected);
        // Ejecuta el listado por medio del servicio.
        java.util.List<Accident_report> result = mockService.findAllAccidentReports();
        // Comprueba que se devolvió exactamente la lista esperada.
        org.junit.jupiter.api.Assertions.assertSame(expected, result);
        // Comprueba que el servicio consultó el puerto.
        org.mockito.Mockito.verify(mockPort).findAll();
        }

        // Caso Mockito exitoso: devuelve una lista vacía si no hay reportes.
        @Test
        void mockito_listarReportes_listaVacia_devuelveListaVacia() {
        // Crea el puerto falso para simular que no hay registros.
        TerritorioAlerta.domain.port.Accident_reportPort mockPort = org.mockito.Mockito.mock(
            TerritorioAlerta.domain.port.Accident_reportPort.class);
        // Crea una instancia de servicio que utilizará este mock.
        FindAllAccidentReportsService mockService = new FindAllAccidentReportsService(mockPort);
        // Prepara una lista sin elementos como respuesta.
        java.util.List<Accident_report> expected = java.util.List.of();
        // Configura el puerto para devolver esa lista vacía.
        org.mockito.Mockito.when(mockPort.findAll()).thenReturn(expected);
        // Ejecuta la búsqueda general.
        java.util.List<Accident_report> result = mockService.findAllAccidentReports();
        // Comprueba que el resultado es la lista vacía configurada.
        org.junit.jupiter.api.Assertions.assertSame(expected, result);
        // Comprueba que se ejecutó la consulta del puerto.
        org.mockito.Mockito.verify(mockPort).findAll();
        }

        // Caso Mockito negativo: propaga el error lanzado por el puerto.
        @Test
        void mockito_listarReportes_errorDelPuerto_propagaExcepcion() {
        // Crea un mock que permitirá simular una falla de consulta.
        TerritorioAlerta.domain.port.Accident_reportPort mockPort = org.mockito.Mockito.mock(
            TerritorioAlerta.domain.port.Accident_reportPort.class);
        // Construye el servicio para probar la propagación del error.
        FindAllAccidentReportsService mockService = new FindAllAccidentReportsService(mockPort);
        // Configura el mock para fallar al listar todos los reportes.
        org.mockito.Mockito.when(mockPort.findAll())
            .thenThrow(new IllegalStateException("Error de consulta"));
        // Comprueba que el servicio propaga la excepción recibida.
        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class,
            () -> mockService.findAllAccidentReports());
        // Comprueba que el servicio intentó invocar el listado.
        org.mockito.Mockito.verify(mockPort).findAll();
        }
}