package paramonov.valentine.loan_service.server;

import org.eclipse.jetty.server.Handler;

public final class ServerRunnerBuilder {
    private int port;
    private Initializer initializer;

    private ServerRunnerBuilder() {
    }

    public static ServerRunnerBuilder config() {
        final ServerRunnerBuilder builder = new ServerRunnerBuilder();
        return builder;
    }

    public ServerRunnerBuilder port(int port) {
        this.port = port;
        return this;
    }

    public ServerRunnerBuilder initializer(Initializer initializer) {
        this.initializer = initializer;
        return this;
    }

    public ServerRunner runner() {
        final ServerRunner runner = new ServerRunner(port);
        runner.init();
        if(initializer == null) {
            return runner;
        }

        final Handler handler = initializer.getHandler();
        runner.setHandler(handler);

        return runner;
    }
}
