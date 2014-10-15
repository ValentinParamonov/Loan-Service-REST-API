package paramonov.valentine.loan_service.common.dtos;

import paramonov.valentine.loan_service.db.entities.User;

import java.math.BigDecimal;

public class LoanApplicationDto {
    private User applicant;
    private BigDecimal amount;
    private Integer term;
    private String applicantIp;

    public User getApplicant() {
        return applicant;
    }

    public LoanApplicationDto withApplicant(User applicant) {
        this.applicant = applicant;
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LoanApplicationDto withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public Integer getTerm() {
        return term;
    }

    public LoanApplicationDto withTerm(Integer term) {
        this.term = term;
        return this;
    }

    public String getApplicantIp() {
        return applicantIp;
    }

    public LoanApplicationDto withApplicantIp(String applicantIp) {
        this.applicantIp = applicantIp;
        return this;
    }
}

