package paramonov.valentine.loan_service.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"paramonov.valentine.loan_service"})
class WebConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private HandlerMethodArgumentResolver activeUserArgumentResolver;

    @Autowired
    public void configureHandlerAdapter(RequestMappingHandlerAdapter handlerAdapter) {
        final List<HandlerMethodArgumentResolver> oldArgumentResolvers = handlerAdapter.getArgumentResolvers();
        final ArrayList<HandlerMethodArgumentResolver> newArgumentResolvers = new ArrayList<>(oldArgumentResolvers);
        final List<HandlerMethodArgumentResolver> argumentResolvers =
            Collections.unmodifiableList(newArgumentResolvers);

        newArgumentResolvers.add(0, activeUserArgumentResolver);
        handlerAdapter.setArgumentResolvers(argumentResolvers);
    }
}
