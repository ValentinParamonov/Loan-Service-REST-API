package paramonov.valentine.loan_service.common.vos.impl;

import org.springframework.stereotype.Component;
import paramonov.valentine.loan_service.common.vos.LoanApplicationVo;
import paramonov.valentine.loan_service.common.vos.LoanServiceVoBuilder;
import paramonov.valentine.loan_service.db.entities.User;

import java.math.BigDecimal;

@Component("loanServiceVoBuilder")
public class LoanServiceVoBuilderImpl implements LoanServiceVoBuilder {
    @Override
    public LoanApplicationVo newLoanApplicationVo(final User applicant, final BigDecimal amount, final Integer term,
        final String applicantIp) {

        return new LoanApplicationVo() {
            @Override public User getApplicant() {
                return applicant;
            }

            @Override public BigDecimal getAmount() {
                return amount;
            }

            @Override public Integer getTerm() {
                return term;
            }

            @Override public String getApplicantIp() {
                return applicantIp;
            }
        };
    }
}
