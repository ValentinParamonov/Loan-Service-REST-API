package paramonov.valentine.loan_service.web.handlers;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ErrorHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public final class ExceptionLoggingHandler extends ErrorHandler {
    private final Logger log;

    public ExceptionLoggingHandler() {
        log = LogManager.getLogger(getClass());
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
        throws IOException {

        final Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        if(throwable == null) {
            super.handle(target, baseRequest, request, response);
            return;
        }

        final int errorCode = (int) request.getAttribute("javax.servlet.error.status_code");

        log.catching(Level.ERROR, throwable);
        respondWithErrorCode(request, response, errorCode);
    }

    private void respondWithErrorCode(HttpServletRequest request, HttpServletResponse response, int errorCode)
        throws IOException {
        final PrintWriter writer = response.getWriter();

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(errorCode);
        writeErrorPage(request, writer, errorCode);
    }

    private void writeErrorPage(HttpServletRequest request, PrintWriter writer, int errorCode) {
        final String requestURI = request.getRequestURI();

        writer.printf("<h2>HTTP ERROR: %d</h2>\n", errorCode);
        writer.printf("<p>Problem accessing %s.\n", requestURI);
        writer.println("<hr/><i><small>Powered by Jetty://</small></i>");
    }
}
