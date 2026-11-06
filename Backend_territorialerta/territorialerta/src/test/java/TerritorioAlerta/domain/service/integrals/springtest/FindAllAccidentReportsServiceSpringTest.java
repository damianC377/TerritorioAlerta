package TerritorioAlerta.domain.service.integrals.springtest;

import java.util.List;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import TerritorioAlerta.domain.model.Accident_report;
import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.service.FindAllAccidentReportsService;

@SpringBootTest
@Transactional
public final class FindAllAccidentReportsServiceSpringTest extends SpringIntegrationTestSupport {

    // Ejecuta esta clase como una prueba independiente desde Java.
    public static void main(String[] args) {
        // Inicia Spring y revierte los datos al finalizar este caso.
        runCheck("FindAllAccidentReportsService", environment -> {
            // Crea un usuario real para asociarlo al reporte.
            User user = environment.persistUser();
            // Persiste el reporte que debe aparecer en el listado.
            Accident_report savedReport = environment.persistReport(user.getId_user());
            // Fuerza las escrituras y limpia la caché JPA antes de consultar.
            environment.flushAndClear();

            // Obtiene el servicio real que lista todos los reportes.
            FindAllAccidentReportsService service =
                    environment.getBean(FindAllAccidentReportsService.class);
            // Solicita todos los reportes persistidos.
            List<Accident_report> reports = service.findAllAccidentReports();

            // Comprueba que el listado contiene el reporte creado por este caso.
            check(reports.stream().anyMatch(report ->
                            savedReport.getId_accident_report().equals(report.getId_accident_report())),
                    "El listado general no incluyó el reporte creado.");
        });
    }
}
