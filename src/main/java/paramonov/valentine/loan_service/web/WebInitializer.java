package paramonov.valentine.loan_service.web;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import paramonov.valentine.loan_service.web.handlers.ExceptionLoggingHandler;

import javax.servlet.DispatcherType;
import java.io.IOException;
import java.net.URI;
import java.util.EnumSet;
import java.util.EventListener;

public final class WebInitializer {
    private static final String CONFIG_LOCATION = "paramonov.valentine.loan_service.web";
    private static final String MAPPING_URL = "/*";
    private static final String APP_ROOT = "/";
    private static final String RESOURCE_BASE = "res";

    public Handler newServletContextHandler() throws IOException {
        final ServletContextHandler handler = new ServletContextHandler();
        final WebApplicationContext context = getWebAppContext();
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

    private String getResourceBase() throws IOException {
        final ClassPathResource classPathResource = new ClassPathResource(RESOURCE_BASE);
        final URI resourceBaseUri = classPathResource.getURI();

        return resourceBaseUri.toString();
    }

    private WebApplicationContext getWebAppContext() {
        final AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation(CONFIG_LOCATION);

        return context;
    }
}
