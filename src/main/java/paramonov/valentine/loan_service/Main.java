package paramonov.valentine.loan_service;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import paramonov.valentine.loan_service.web.WebInitializer;

import java.io.IOException;

public final class Main {
    private static final int DEFAULT_PORT = 8080;
    private static final int PORT_NUMBER_INDEX = 0;
    private static final Logger log;

    static {
        final String className = Main.class.getName();
        log = LogManager.getLogger(className);
    }

    private Main() {
    }

    public static void main(String[] args) {
        if(args.length > 1) {
            printUsageAndExit();
        }

        final int portNumber = parsePortNumber(args);

        runServer(portNumber);
    }

    private static void runServer(int portNumber) {
        final Server server = new Server(portNumber);
        try {
            attachServletContextHandler(server)
                .start();
            log.info("Server started at port {}", portNumber);
            server.join();
            log.info("Server finished");
        } catch(Exception e) {
            log.catching(Level.ERROR, e);
        }
    }

    private static Server attachServletContextHandler(Server server) throws IOException {
        final WebInitializer initializer = new WebInitializer();
        final Handler handler = initializer.newServletContextHandler();

        server.setHandler(handler);

        return server;
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
