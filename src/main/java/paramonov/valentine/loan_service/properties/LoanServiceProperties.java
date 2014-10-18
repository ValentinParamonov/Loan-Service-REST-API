package paramonov.valentine.loan_service.properties;

import paramonov.valentine.loan_service.util.Time;

import java.math.BigDecimal;

public interface LoanServiceProperties {
    Time riskyTimeFrom();

    Time riskyTimeTill();

    Integer maxApplicationsPerDay();

    BigDecimal minAmount();

    BigDecimal maxAmount();

    BigDecimal defaultInterest();

    Integer minTermDays();

    Integer maxTermDays();

    Integer extensionTermDays();

    BigDecimal extensionInterestFactor();

    Integer minUserNameLength();

    Integer minPasswordLength();
}
