package paramonov.valentine.loan_service.acceptance;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    format = {"pretty", "html:target/cucumber"},
    features = {"classpath:paramonov/valentine/loan_service"})
public class CucumberConfig {
    private static Server server;

    @BeforeClass
    public static void setUp() throws Exception {
        final Handler handler = getHandler();
        server = new Server(0);
        server.setHandler(handler);
        server.start();
        final int port = getPort();
        System.out.println(port);
    }

    @AfterClass
    public static void tearDown() throws InterruptedException {
        server.join();
    }

    private static Handler getHandler() {
        final ServletContextHandler handler = new ServletContextHandler();

        return handler;
    }

    private static int getPort() {
        final Connector[] connectors = server.getConnectors();
        final ServerConnector activeConnector = (ServerConnector) connectors[0];
        return activeConnector.getLocalPort();
    }
}
