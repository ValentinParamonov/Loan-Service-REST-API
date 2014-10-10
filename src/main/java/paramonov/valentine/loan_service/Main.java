package paramonov.valentine.loan_service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

public final class Main {
    private static final int DEFAULT_PORT = 8080;
    private static final int PORT_NUMBER_INDEX = 0;
    private static final String CLASS_NAME = Main.class.getName();
    private static final Logger log = LogManager.getLogger(CLASS_NAME);

    public static void main(String[] args) {
        if(args.length > 1) {
            printUsageAndExit();
        }

        final int portNumber;
        try {
            portNumber = getPortNumber(args);
        } catch(NumberFormatException nfe) {
            final String portNumberString = args[PORT_NUMBER_INDEX];
            printNotANumberAndExit(portNumberString);
            return;
        }

        runServer(portNumber);
    }

    private static void runServer(int portNumber) {
        final Server server = new Server(portNumber);
        try {
            final URL sourceLocation = getSourceLocation();

            final WebAppContext context = new WebAppContext();

            server.start();
            log.info("Server started at port {}", portNumber);
            server.join();
            log.info("Server finished");
        } catch(Exception e) {
            log.error(e);
        }
    }

    private static URL getSourceLocation() {
        final ProtectionDomain domain = Main.class.getProtectionDomain();
        final CodeSource codeSource = domain.getCodeSource();

        return codeSource.getLocation();
    }

    private static void printUsageAndExit() {
        System.out.println("Usage: LoanService.jar [port_number]");
        System.exit(1);
    }

    private static int getPortNumber(String[] args) throws NumberFormatException {
        if(args.length == 0) {
            return DEFAULT_PORT;
        }

        final String portNumberString = args[PORT_NUMBER_INDEX];

        return Integer.parseInt(portNumberString);
    }

    private static void printNotANumberAndExit(String portNumberString) {
        System.out.println("Not a number: " + portNumberString);
        System.exit(1);
    }
}
