package paramonov.valentine.loan_service.common.validators.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import paramonov.valentine.loan_service.common.validators.LoanApplicationValidator;
import paramonov.valentine.loan_service.common.validators.exceptions.InvalidAmountException;
import paramonov.valentine.loan_service.common.validators.exceptions.InvalidPasswordException;
import paramonov.valentine.loan_service.common.validators.exceptions.InvalidTermException;
import paramonov.valentine.loan_service.common.validators.exceptions.InvalidUserNameException;
import paramonov.valentine.loan_service.common.vos.LoanApplicationVo;
import paramonov.valentine.loan_service.properties.LoanServiceProperties;
import paramonov.valentine.loan_service.util.ValidationUtils;

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

    @Override
    public void validateUserName(String userName) {
        final Integer minUserNameLength = loanServiceProperties.minUserNameLength();
        if(isShorterThan(userName, minUserNameLength)) {
            throw new InvalidUserNameException();
        }
    }

    @Override
    public void validatePassword(String password) {
        final Integer minPasswordLength = loanServiceProperties.minPasswordLength();
        if(isShorterThan(password, minPasswordLength)) {
            throw new InvalidPasswordException();
        }
    }

    private boolean isShorterThan(String string, int length) {
        if(string == null) {
            return true;
        }

        final String trimmedString = string.trim();
        final int stringLength = trimmedString.length();

        return stringLength < length;
    }

    private void validateAmount(BigDecimal applicationAmount) {
        final BigDecimal minAmount = loanServiceProperties.minAmount();
        final BigDecimal maxAmount = loanServiceProperties.maxAmount();

        if(!ValidationUtils.isBetweenIncluding(applicationAmount, minAmount, maxAmount)) {
            throw new InvalidAmountException();
        }
    }

    private void validateTerm(Integer term) {
        final Integer minTerm = loanServiceProperties.minTermDays();
        final Integer maxTerm = loanServiceProperties.maxTermDays();

        if(!ValidationUtils.isBetweenIncluding(term, minTerm, maxTerm)) {
            throw new InvalidTermException();
        }
    }
}
