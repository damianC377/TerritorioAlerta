package TerritorioAlerta.domain.service.integrals.springtest;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import TerritorioAlerta.domain.model.Accident_report;
import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.service.CreateAccidentReportService;
import TerritorioAlerta.domain.service.CreateUserService;
import TerritorioAlerta.domain.service.FindAccidentReportByIdService;

@SpringBootTest
@Transactional
public final class CreateAccidentReportServiceSpringTest extends SpringIntegrationTestSupport {

    // Ejecuta esta clase como una prueba independiente desde Java.
    public static void main(String[] args) {
        // Inicia Spring y revierte los datos al finalizar este caso.
        runCheck("CreateAccidentReportService", environment -> {
            // Obtiene el servicio real que crea usuarios.
            CreateUserService createUserService = environment.getBean(CreateUserService.class);
            // Obtiene el servicio real que crea reportes.
            CreateAccidentReportService createReportService =
                    environment.getBean(CreateAccidentReportService.class);
            // Obtiene el servicio que permite verificar el reporte persistido.
            FindAccidentReportByIdService findReportService =
                    environment.getBean(FindAccidentReportByIdService.class);

            // Crea un usuario real para asociarlo al reporte.
            User user = createUserService.createUser(newUser());
            // Crea el reporte mediante el servicio que se está probando.
            Accident_report savedReport = createReportService.createAccidentReport(newReport(user.getId_user()));
            // Comprueba que la base de datos haya asignado un ID al reporte.
            check(savedReport.getId_accident_report() != null, "No se generó el ID del reporte.");

            // Fuerza las escrituras y limpia las entidades en memoria.
            environment.flushAndClear();
            // Vuelve a buscar el reporte desde la persistencia real.
            Accident_report foundReport =
                    findReportService.findAccidentReportById(savedReport.getId_accident_report());
            // Comprueba que el reporte mantuvo la relación con el usuario.
            check(user.getId_user().equals(foundReport.getId_user()),
                    "El reporte no conservó la relación con el usuario.");
        });
    }
}
