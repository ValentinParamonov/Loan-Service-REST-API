package paramonov.valentine.loan_service.properties.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import paramonov.valentine.loan_service.properties.LoanManagerProperties;
import paramonov.valentine.loan_service.util.Time;

import java.math.BigDecimal;

@Configuration
class LoanManagerPropertiesConfig {
    @Value("${loan.manager.minTermDays:7}")
    private Integer minTermDays;

    @Value("${loan.manager.maxTermDays:30}")
    private Integer maxTermDays;

    @Value("${loan.manager.maxApplicationsPerDay:3}")
    private Integer maxApplicationsPerDay;

    @Value("${loan.manager.minAmount:50}")
    private BigDecimal minAmount;

    @Value("${loan.manager.maxAmount:500}")
    private BigDecimal maxAmount;

    @Value("${loan.manager.defaultInterest:1.1}")
    private BigDecimal defaultInterest;

    @Value("${loan.manager.riskyTimeFrom:0}")
    private Time riskyTimeFrom;

    @Value("${loan.manager.riskyTimeTill:0}")
    private Time riskyTimeTill;

    @Value("${loan.manager.extension.termDays:7}")
    private Integer extensionTermDays;

    @Value("${loan.manager.extension.interestFactor:1.5}")
    private BigDecimal extensionInterestFactor;

    @Bean
    public LoanManagerProperties loanManagerProperties() {
        return new LoanManagerProperties() {
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
