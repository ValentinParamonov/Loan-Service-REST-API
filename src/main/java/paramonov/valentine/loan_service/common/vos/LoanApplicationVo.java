package paramonov.valentine.loan_service.common.vos;

import paramonov.valentine.loan_service.db.entities.User;

import java.math.BigDecimal;

public interface LoanApplicationVo {
    public User getApplicant();

    public BigDecimal getAmount();

    public Integer getTerm();

    public String getApplicantIp();
}

