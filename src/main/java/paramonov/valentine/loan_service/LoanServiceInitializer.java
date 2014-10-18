package paramonov.valentine.loan_service;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import paramonov.valentine.loan_service.server.Initializer;
import paramonov.valentine.loan_service.web.handlers.ExceptionLoggingHandler;

import javax.servlet.DispatcherType;
import java.io.IOException;
import java.net.URI;
import java.util.EnumSet;
import java.util.EventListener;

public final class LoanServiceInitializer implements Initializer {
    private static final String MAPPING_URL = "/*";
    private static final String APP_ROOT = "/";
    private static final String RESOURCE_BASE = "res";

    private Logger log;
    private WebApplicationContext context;

    private LoanServiceInitializer() {
    }

    private void init() {
        log = LogManager.getLogger(getClass());
    }

    public static LoanServiceInitializer fromContext(WebApplicationContext context) {
        final LoanServiceInitializer initializer = new LoanServiceInitializer();

        initializer.init();
        initializer.context = context;

        return initializer;
    }

    @Override
    public Handler getHandler() {
        final ServletContextHandler handler = new ServletContextHandler();
        final String resourceBase = getResourceBase();
        final ExceptionLoggingHandler errorHandler = new ExceptionLoggingHandler();

        handler.setErrorHandler(errorHandler);
        handler.setContextPath(APP_ROOT);
        handler.setResourceBase(resourceBase);
        addServlets(handler, context);
        addEventListeners(handler, context);
        addFilters(handler);

        return handler;
    }

    private void addServlets(ServletContextHandler handler, WebApplicationContext context) {
        addDispatcherServlet(handler, context);
    }

    private void addEventListeners(ServletContextHandler handler, WebApplicationContext context) {
        addContextLoaderListener(handler, context);
    }

    private void addFilters(ServletContextHandler handler) {
        addSpringSecurityFilterChain(handler);
    }

    private void addSpringSecurityFilterChain(ServletContextHandler handler) {
        final DelegatingFilterProxy proxy = new DelegatingFilterProxy("springSecurityFilterChain");
        final FilterHolder springSecurityFilterChain = new FilterHolder(proxy);
        final EnumSet<DispatcherType> dispatcherTypes = EnumSet.allOf(DispatcherType.class);

        handler.addFilter(springSecurityFilterChain, MAPPING_URL, dispatcherTypes);
    }

    private void addContextLoaderListener(ServletContextHandler handler, WebApplicationContext context) {
        final EventListener contextLoaderListener = new ContextLoaderListener(context);

        handler.addEventListener(contextLoaderListener);
    }

    private void addDispatcherServlet(ServletContextHandler handler, WebApplicationContext context) {
        final DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
        final ServletHolder dispatcherServletHolder = new ServletHolder(dispatcherServlet);

        handler.addServlet(dispatcherServletHolder, MAPPING_URL);
    }

    private String getResourceBase() {
        final ClassPathResource classPathResource = new ClassPathResource(RESOURCE_BASE);
        final URI resourceBaseUri;
        try {
            resourceBaseUri = classPathResource.getURI();
        } catch(IOException ioe) {
            log.catching(Level.ERROR, ioe);
            return "";
        }

        return resourceBaseUri.toString();
    }
}
