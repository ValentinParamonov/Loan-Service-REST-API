package paramonov.valentine.loan_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource({"classpath:res/loan_service.properties"})
public class PropertyPlaceholderConfig {
    @Bean
    @Autowired
    public static PropertySourcesPlaceholderConfigurer loanServiceProperties(Environment environment) {
        final PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setEnvironment(environment);

        return configurer;
    }
}
