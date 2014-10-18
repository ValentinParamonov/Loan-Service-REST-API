package paramonov.valentine.loan_service.common.validators.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import paramonov.valentine.loan_service.common.validators.LoanApplicationValidator;
import paramonov.valentine.loan_service.common.vos.LoanApplicationVo;
import paramonov.valentine.loan_service.properties.LoanServiceProperties;
import paramonov.valentine.loan_service.util.ValidationUtils;
import paramonov.valentine.loan_service.common.validators.exceptions.InvalidAmountException;
import paramonov.valentine.loan_service.common.validators.exceptions.InvalidTermException;

import java.math.BigDecimal;

@Component("loanApplicationValidator")
class LoanApplicationValidatorImpl implements LoanApplicationValidator {
    @Autowired
    private LoanServiceProperties loanServiceProperties;

    @Override
    public void validateLoanApplication(LoanApplicationVo applicationDetails) {
        final BigDecimal applicationAmount = applicationDetails.getAmount();
        final Integer term = applicationDetails.getTerm();

        validateAmount(applicationAmount);
        validateTerm(term);
    }

    private void validateAmount(BigDecimal applicationAmount) {
        final BigDecimal minAmount = loanServiceProperties.getMinAmount();
        final BigDecimal maxAmount = loanServiceProperties.getMaxAmount();

        if(!ValidationUtils.isBetweenIncluding(applicationAmount, minAmount, maxAmount)) {
            throw new InvalidAmountException();
        }
    }

    private void validateTerm(Integer term) {
        final Integer minTerm = loanServiceProperties.getMinTermDays();
        final Integer maxTerm = loanServiceProperties.getMaxTermDays();

        if(!ValidationUtils.isBetweenIncluding(term, minTerm, maxTerm)) {
            throw new InvalidTermException();
        }
    }
}
