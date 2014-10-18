package paramonov.valentine.loan_service.server;

import org.eclipse.jetty.server.Handler;

public interface Initializer {
    Handler getHandler();
}
