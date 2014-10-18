package paramonov.valentine.loan_service;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import paramonov.valentine.loan_service.properties.LoanServiceProperties;
import paramonov.valentine.loan_service.util.Time;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {
    "paramonov.valentine.loan_service.web.handlers",
    "paramonov.valentine.loan_service.web.resolvers",
    "paramonov.valentine.loan_service.db.repositories",
    "paramonov.valentine.loan_service.web.controllers",
    "paramonov.valentine.loan_service.web.managers",
    "paramonov.valentine.loan_service.common"})
public class TestConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private HandlerMethodArgumentResolver activeUserArgumentResolver;

    @Autowired
    public void configureHandlerAdapter(RequestMappingHandlerAdapter handlerAdapter) {
        final List<HandlerMethodArgumentResolver> oldArgumentResolvers = handlerAdapter.getArgumentResolvers();
        final ArrayList<HandlerMethodArgumentResolver> newArgumentResolvers = new ArrayList<>(oldArgumentResolvers);

        newArgumentResolvers.add(0, activeUserArgumentResolver);
        handlerAdapter.setArgumentResolvers(newArgumentResolvers);
    }

    @Bean
    public StringEncryptor encryptor() {
        final StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword("test");

        return encryptor;
    }

    @Bean
    public LoanServiceProperties loanServiceProperties() {
        return new LoanServiceProperties() {
            @Override
            public Time riskyTimeFrom() {
                return Time.valueOf("0");
            }

            @Override
            public Time riskyTimeTill() {
                return Time.valueOf("06:00:00");
            }

            @Override
            public Integer maxApplicationsPerDay() {
                return 3;
            }

            @Override
            public BigDecimal minAmount() {
                return new BigDecimal(50);
            }

            @Override
            public BigDecimal maxAmount() {
                return new BigDecimal(500);
            }

            @Override
            public BigDecimal defaultInterest() {
                return new BigDecimal(1.1);
            }

            @Override
            public Integer minTermDays() {
                return 5;
            }

            @Override
            public Integer maxTermDays() {
                return 30;
            }

            @Override
            public Integer extensionTermDays() {
                return 7;
            }

            @Override
            public BigDecimal extensionInterestFactor() {
                return new BigDecimal(1.5);
            }

            @Override
            public Integer minUserNameLength() {
                return 4;
            }

            @Override
            public Integer minPasswordLength() {
                return 6;
            }
        };
    }
}
