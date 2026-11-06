package TerritorioAlerta.domain.service.integrals.springtest;

import java.util.List;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import TerritorioAlerta.domain.model.Accident_report;
import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.service.FindAccidentReportsByUserIdService;

@SpringBootTest
@Transactional
public final class FindAccidentReportsByUserIdServiceSpringTest extends SpringIntegrationTestSupport {

    // Ejecuta esta clase como una prueba independiente desde Java.
    public static void main(String[] args) {
        // Inicia Spring y revierte los datos al finalizar este caso.
        runCheck("FindAccidentReportsByUserIdService", environment -> {
            // Crea el usuario al que pertenecerán los reportes.
            User user = environment.persistUser();
            // Persiste el primer reporte del usuario.
            environment.persistReport(user.getId_user());
            // Persiste el segundo reporte del mismo usuario.
            environment.persistReport(user.getId_user());
            // Fuerza las escrituras y limpia la caché JPA antes de buscar.
            environment.flushAndClear();

            // Obtiene el servicio real que filtra reportes por usuario.
            FindAccidentReportsByUserIdService service =
                    environment.getBean(FindAccidentReportsByUserIdService.class);
            // Consulta los reportes usando el ID real del usuario.
            List<Accident_report> reports = service.findAccidentReportsByUserId(user.getId_user());

            // Comprueba que se recuperaron los dos reportes creados.
            check(reports.size() == 2, "La búsqueda no devolvió los dos reportes creados.");
            // Comprueba que todos los resultados pertenecen al usuario consultado.
            check(reports.stream().allMatch(report -> user.getId_user().equals(report.getId_user())),
                    "La búsqueda devolvió un reporte de otro usuario.");
        });
    }
}
