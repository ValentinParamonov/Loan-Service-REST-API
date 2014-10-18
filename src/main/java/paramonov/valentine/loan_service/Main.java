package paramonov.valentine.loan_service;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import paramonov.valentine.loan_service.server.ServerRunner;
import paramonov.valentine.loan_service.server.ServerRunnerBuilder;

public final class Main {
    private static final int DEFAULT_PORT = 8080;
    private static final int PORT_NUMBER_INDEX = 0;
    private static final String CONFIG_LOCATION = "paramonov.valentine.loan_service.web";

    private Main() {
    }

    public static void main(String[] args) {
        if(args.length > 1) {
            printUsageAndExit();
        }

        final int portNumber = parsePortNumber(args);
        final WebApplicationContext context = getContext();
        final LoanServiceInitializer initializer = LoanServiceInitializer.fromContext(context);

        final ServerRunner runner = ServerRunnerBuilder.config()
            .port(portNumber)
            .initializer(initializer)
            .runner();

        runInCurrentThread(runner);
    }

    private static void runInCurrentThread(ServerRunner runner) {
        runner.run();
    }

    private static WebApplicationContext getContext() {
        final AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation(CONFIG_LOCATION);

        return context;
    }

    private static void printUsageAndExit() {
        System.out.println("Expected arguments: [port_number]");
        System.exit(1);
    }

    private static int parsePortNumber(String[] args) {
        if(args.length == 0) {
            return DEFAULT_PORT;
        }

        final String portNumberString = args[PORT_NUMBER_INDEX];

        try {
            return Integer.parseInt(portNumberString);
        } catch(NumberFormatException nfe) {
            printUsageAndExit();
        }

        assert false : "Should be never reached";
        return -1;
    }
}
