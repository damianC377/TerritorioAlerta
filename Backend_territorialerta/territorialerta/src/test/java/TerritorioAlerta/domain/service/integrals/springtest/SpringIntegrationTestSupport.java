package TerritorioAlerta.domain.service.integrals.springtest;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import TerritorioAlerta.TerritorialertaApplication;
import TerritorioAlerta.domain.model.Accident_report;
import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.model.Enums.Role;
import TerritorioAlerta.domain.model.Enums.Status;
import TerritorioAlerta.domain.model.Enums.TypeReport;
import TerritorioAlerta.domain.service.CreateAccidentReportService;
import TerritorioAlerta.domain.service.CreateUserService;

abstract class SpringIntegrationTestSupport {

    protected static void runCheck(String name, IntegrationCheck check) {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(TerritorialertaApplication.class)
                .web(WebApplicationType.NONE)
                .run()) {
            PlatformTransactionManager transactionManager =
                    context.getBean(PlatformTransactionManager.class);
            TransactionTemplate transaction = new TransactionTemplate(transactionManager);
            IntegrationEnvironment environment = new IntegrationEnvironment(context);
            context.getAutowireCapableBeanFactory().autowireBean(environment);

            transaction.executeWithoutResult(status -> {
                try {
                    check.run(environment);
                    status.setRollbackOnly();
                } catch (Exception exception) {
                    status.setRollbackOnly();
                    throw new IllegalStateException("Falló la prueba de integración " + name + ".", exception);
                }
            });

            System.out.println("OK - " + name);
        }
    }

    protected static User newUser() {
        User user = new User();
        user.setName("Spring Test");
        user.setLastname("Integration");
        user.setEmail("spring-test-" + UUID.randomUUID() + "@example.test");
        user.setPassword("spring-test-password");
        user.setCommune("Test commune");
        user.setNeighborhood("Test neighborhood");
        user.setRole(Role.User);
        user.setCreation_date(LocalDateTime.now());
        return user;
    }

    protected static Accident_report newReport(Long userId) {
        Accident_report report = new Accident_report();
        report.setId_user(userId);
        report.setDate(LocalDateTime.now());
        report.setType_report(TypeReport.road_accident);
        report.setCommune("Test commune");
        report.setNeighborhood("Test neighborhood");
        report.setAddress("Test address");
        report.setImage("test-image");
        report.setDescription("Spring integration report");
        report.setStatus(Status.Minor);
        report.setCreation_date(LocalDateTime.now());
        return report;
    }

    protected static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    @FunctionalInterface
    protected interface IntegrationCheck {
        void run(IntegrationEnvironment environment) throws Exception;
    }

    protected static final class IntegrationEnvironment {

        private final ConfigurableApplicationContext context;

        @PersistenceContext
        private EntityManager entityManager;

        private IntegrationEnvironment(ConfigurableApplicationContext context) {
            this.context = context;
        }

        protected <T> T getBean(Class<T> beanType) {
            return context.getBean(beanType);
        }

        protected User persistUser() throws Exception {
            CreateUserService service = getBean(CreateUserService.class);
            return service.createUser(newUser());
        }

        protected Accident_report persistReport(Long userId) throws Exception {
            CreateAccidentReportService service = getBean(CreateAccidentReportService.class);
            return service.createAccidentReport(newReport(userId));
        }

        protected void flushAndClear() {
            entityManager.flush();
            entityManager.clear();
        }
    }
}
