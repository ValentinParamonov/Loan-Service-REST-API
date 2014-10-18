package paramonov.valentine.loan_service.server;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

public final class ServerRunner implements Runnable {
    private Logger log;
    private final Server server;

    ServerRunner(int port) {
        server = new Server(port);
    }

    void init() {
        log = LogManager.getLogger(getClass());
    }

    @Override
    public void run() {
        try {
            server.start();
            log.info("Server started at port {}", getPort());
            server.join();
            log.info("Server finished");
        } catch(Exception e) {
            log.catching(Level.ERROR, e);
        }
    }

    public void stop() throws Exception {
        server.stop();
    }

    public boolean isStarted() {
        return server.isStarted();
    }

    public int getPort() {
        final Connector[] connectors = server.getConnectors();
        final ServerConnector activeConnector = (ServerConnector) connectors[0];

        return activeConnector.getLocalPort();
    }

    ServerRunner setHandler(Handler handler) {
        server.setHandler(handler);
        return this;
    }
}
