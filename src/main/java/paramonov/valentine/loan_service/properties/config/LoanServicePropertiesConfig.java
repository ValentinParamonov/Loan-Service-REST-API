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

    @Bean
    public LoanServiceProperties loanServiceProperties() {
        return new LoanServiceProperties() {
            @Override
            public Time getRiskyTimeFrom() {
                return riskyTimeFrom;
            }

            @Override
            public Time getRiskyTimeTill() {
                return riskyTimeTill;
            }

            @Override
            public Integer getMaxApplicationsPerDay() {
                return maxApplicationsPerDay;
            }

            @Override public BigDecimal getMinAmount() {
                return minAmount;
            }

            @Override
            public BigDecimal getMaxAmount() {
                return maxAmount;
            }

            @Override public BigDecimal getDefaultInterest() {
                return defaultInterest;
            }

            @Override public Integer getMinTermDays() {
                return minTermDays;
            }

            @Override
            public Integer getMaxTermDays() {
                return maxTermDays;
            }

            @Override public Integer getExtensionTermDays() {
                return extensionTermDays;
            }

            @Override public BigDecimal getExtensionInterestFactor() {
                return extensionInterestFactor;
            }
        };
    }
}
