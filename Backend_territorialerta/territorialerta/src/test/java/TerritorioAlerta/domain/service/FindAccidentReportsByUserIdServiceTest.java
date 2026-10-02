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
    }

    // Comprueba que un ID sin reportes produce una lista vacía, no un error.
    @Test
    void buscarReportesPorUsuario_usuarioSinReportes_devuelveListaVacia() {
        // Busca por un ID que no corresponde a un usuario creado en esta prueba.
        List<Accident_report> reports = findAccidentReportsByUserIdService
                .findAccidentReportsByUserId(Long.MAX_VALUE);
        // Confirma el comportamiento definido por el servicio para cero resultados.
        assertTrue(reports.isEmpty());
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
        // ==================== AQUÍ EMPIEZAN LAS PRUEBAS MOCKITO ====================

        // Caso Mockito exitoso: devuelve reportes asociados al ID solicitado.
        @Test
        void mockito_listarReportesPorUsuario_listaConDatos_devuelveLista() {
        // Crea un puerto de reportes falso para simular la consulta.
        TerritorioAlerta.domain.port.Accident_reportPort mockPort = org.mockito.Mockito.mock(
            TerritorioAlerta.domain.port.Accident_reportPort.class);
        // Construye el servicio usando ese puerto simulado.
        FindAccidentReportsByUserIdService mockService = new FindAccidentReportsByUserIdService(mockPort);
        // Prepara una lista de reportes como respuesta esperada.
        java.util.List<Accident_report> expected = java.util.List.of(
            new Accident_report(), new Accident_report());
        // Configura la respuesta para el usuario 12.
        org.mockito.Mockito.when(mockPort.findByIdUserList(12L)).thenReturn(expected);
        // Ejecuta la búsqueda de reportes para ese usuario.
        java.util.List<Accident_report> result = mockService.findAccidentReportsByUserId(12L);
        // Comprueba que el servicio devuelve la lista preparada.
        org.junit.jupiter.api.Assertions.assertSame(expected, result);
        // Comprueba que consultó el puerto usando el ID correcto.
        org.mockito.Mockito.verify(mockPort).findByIdUserList(12L);
        }

        // Caso Mockito exitoso: devuelve lista vacía para un usuario sin reportes.
        @Test
        void mockito_listarReportesPorUsuario_listaVacia_devuelveListaVacia() {
        // Crea un puerto falso para simular cero resultados.
        TerritorioAlerta.domain.port.Accident_reportPort mockPort = org.mockito.Mockito.mock(
            TerritorioAlerta.domain.port.Accident_reportPort.class);
        // Inyecta el puerto falso en una instancia del servicio.
        FindAccidentReportsByUserIdService mockService = new FindAccidentReportsByUserIdService(mockPort);
        // Prepara la lista vacía que se devolverá.
        java.util.List<Accident_report> expected = java.util.List.of();
        // Configura cero resultados para el usuario 19.
        org.mockito.Mockito.when(mockPort.findByIdUserList(19L)).thenReturn(expected);
        // Ejecuta la búsqueda por el ID 19.
        java.util.List<Accident_report> result = mockService.findAccidentReportsByUserId(19L);
        // Comprueba que el resultado coincide con la lista vacía preparada.
        org.junit.jupiter.api.Assertions.assertSame(expected, result);
        // Comprueba que la consulta fue delegada usando el ID 19.
        org.mockito.Mockito.verify(mockPort).findByIdUserList(19L);
        }

        // Caso Mockito negativo: propaga el error que ocurre dentro del puerto.
        @Test
        void mockito_listarReportesPorUsuario_errorDelPuerto_propagaExcepcion() {
        // Crea un puerto falso que simulará una falla de consulta.
        TerritorioAlerta.domain.port.Accident_reportPort mockPort = org.mockito.Mockito.mock(
            TerritorioAlerta.domain.port.Accident_reportPort.class);
        // Construye el servicio que recibirá la excepción del mock.
        FindAccidentReportsByUserIdService mockService = new FindAccidentReportsByUserIdService(mockPort);
        // Configura una excepción cuando se consulten reportes para el ID 99.
        org.mockito.Mockito.when(mockPort.findByIdUserList(99L))
            .thenThrow(new IllegalStateException("Error de consulta"));
        // Comprueba que el servicio propaga el error de la dependencia.
        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class,
            () -> mockService.findAccidentReportsByUserId(99L));
        // Comprueba que la operación fallida llegó al puerto.
        org.mockito.Mockito.verify(mockPort).findByIdUserList(99L);
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