package paramonov.valentine.loan_service.properties;

import paramonov.valentine.loan_service.util.Time;

import java.math.BigDecimal;

public interface LoanManagerProperties {
    Time getRiskyTimeFrom();
    Time getRiskyTimeTill();
    Integer getMaxApplicationsPerDay();
    BigDecimal getMaxAmount();
    Integer getMaxTermDays();
}
