package paramonov.valentine.loan_service.web;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.IOException;
import java.net.URI;
import java.util.EventListener;

public final class WebInitializer {
    private static final String CONFIG_LOCATION = "paramonov.valentine.loan_service.web";
    private static final String MAPPING_URL = "/*";
    private static final String APP_ROOT = "/";
    private static final String RESOURCE_BASE = "";

    public Handler newServletContextHandler() throws IOException {
        final ServletContextHandler handler = new ServletContextHandler();
        final WebApplicationContext context = getWebAppContext();
        final ServletHolder dispatcherServletHolder = getDispatcherServletHolder(context);
        final EventListener contextLoaderListener = new ContextLoaderListener(context);
        final String resourceBase = getResourceBase();

        handler.setErrorHandler(null);
        handler.setContextPath(APP_ROOT);
        handler.addServlet(dispatcherServletHolder, MAPPING_URL);
        handler.addEventListener(contextLoaderListener);
        handler.setResourceBase(resourceBase);

        return handler;
    }

    private ServletHolder getDispatcherServletHolder(WebApplicationContext context) {
        final DispatcherServlet dispatcherServlet = new DispatcherServlet(context);

        return new ServletHolder(dispatcherServlet);
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
