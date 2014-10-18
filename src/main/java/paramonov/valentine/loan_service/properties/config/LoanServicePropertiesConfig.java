package paramonov.valentine.loan_service.properties.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import paramonov.valentine.loan_service.properties.LoanServiceProperties;
import paramonov.valentine.loan_service.util.Time;

import java.math.BigDecimal;

@Configuration
class LoanServicePropertiesConfig {
    @Value("${loan.service.minTermDays:7}")
    private Integer minTermDays;

    @Value("${loan.service.maxTermDays:30}")
    private Integer maxTermDays;

    @Value("${loan.service.maxApplicationsPerDay:3}")
    private Integer maxApplicationsPerDay;

    @Value("${loan.service.minAmount:50}")
    private BigDecimal minAmount;

    @Value("${loan.service.maxAmount:500}")
    private BigDecimal maxAmount;

    @Value("${loan.service.defaultInterest:1.1}")
    private BigDecimal defaultInterest;

    @Value("${loan.service.riskyTimeFrom:0}")
    private Time riskyTimeFrom;

    @Value("${loan.service.riskyTimeTill:0}")
    private Time riskyTimeTill;

    @Value("${loan.service.extension.termDays:7}")
    private Integer extensionTermDays;

    @Value("${loan.service.extension.interestFactor:1.5}")
    private BigDecimal extensionInterestFactor;

    @Value("${loan.service.minUserNameLength:4}")
    private Integer minUsernameLength;

    @Value("${loan.service.minPasswordLength:6}")
    private Integer minPasswordLength;

    @Bean
    public LoanServiceProperties loanServiceProperties() {
        return new LoanServiceProperties() {
            @Override
            public Time riskyTimeFrom() {
                return riskyTimeFrom;
            }

            @Override
            public Time riskyTimeTill() {
                return riskyTimeTill;
            }

            @Override
            public Integer maxApplicationsPerDay() {
                return maxApplicationsPerDay;
            }

            @Override public BigDecimal minAmount() {
                return minAmount;
            }

            @Override
            public BigDecimal maxAmount() {
                return maxAmount;
            }

            @Override
            public BigDecimal defaultInterest() {
                return defaultInterest;
            }

            @Override
            public Integer minTermDays() {
                return minTermDays;
            }

            @Override
            public Integer maxTermDays() {
                return maxTermDays;
            }

            @Override
            public Integer extensionTermDays() {
                return extensionTermDays;
            }

            @Override
            public BigDecimal extensionInterestFactor() {
                return extensionInterestFactor;
            }

            @Override
            public Integer minUserNameLength() {
                return minUsernameLength;
            }

            @Override
            public Integer minPasswordLength() {
                return minPasswordLength;
            }
        };
    }
}
