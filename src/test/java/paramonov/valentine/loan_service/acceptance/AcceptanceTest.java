package paramonov.valentine.loan_service.acceptance;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import paramonov.valentine.loan_service.LoanServiceInitializer;
import paramonov.valentine.loan_service.server.ServerRunner;
import paramonov.valentine.loan_service.server.ServerRunnerBuilder;

@RunWith(Cucumber.class)
@CucumberOptions(
    format = {"pretty", "html:target/cucumber"},
    features = {"classpath:paramonov/valentine/loan_service"})
public class AcceptanceTest {
    public static final int TEST_PORT = 48048;
    private static final String[] configLocations = {
        "paramonov.valentine.loan_service.TestConfig",
        "paramonov.valentine.loan_service.TestDatabaseConfig",
        "paramonov.valentine.loan_service.security"
    };
    private static ServerRunner runner;

    @BeforeClass
    public static void setUp() throws Exception {
        final AnnotationConfigWebApplicationContext testContext = new AnnotationConfigWebApplicationContext();
        testContext.setConfigLocations(configLocations);
        final LoanServiceInitializer initializer = LoanServiceInitializer.fromContext(testContext);

        runner = ServerRunnerBuilder.config()
            .port(TEST_PORT)
            .initializer(initializer)
            .runner();

        new Thread(runner).start();
        while(!runner.isStarted()) {
            Thread.sleep(200);
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
        runner.stop();
    }
}
