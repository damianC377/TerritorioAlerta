package TerritorioAlerta.domain.service.integrals.springtest;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import TerritorioAlerta.domain.model.Accident_report;
import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.service.FindAccidentReportByIdService;

@SpringBootTest
@Transactional
public final class FindAccidentReportByIdServiceSpringTest extends SpringIntegrationTestSupport {

    // Ejecuta esta clase como una prueba independiente desde Java.
    public static void main(String[] args) {
        // Inicia Spring y revierte los datos al finalizar este caso.
        runCheck("FindAccidentReportByIdService", environment -> {
            // Crea un usuario real para asociarlo al reporte.
            User user = environment.persistUser();
            // Crea el reporte cuyo identificador será consultado.
            Accident_report savedReport = environment.persistReport(user.getId_user());
            // Fuerza las escrituras y limpia la caché JPA antes de consultar.
            environment.flushAndClear();

            // Obtiene el servicio real de búsqueda de reportes por ID.
            FindAccidentReportByIdService service =
                    environment.getBean(FindAccidentReportByIdService.class);
            // Recupera el reporte usando el ID generado por la base.
            Accident_report foundReport =
                    service.findAccidentReportById(savedReport.getId_accident_report());
            // Comprueba que el servicio devolvió exactamente el reporte solicitado.
            check(savedReport.getId_accident_report().equals(foundReport.getId_accident_report()),
                    "La búsqueda no devolvió el reporte solicitado.");
        });
    }
}
