package paramonov.valentine.loan_service.common.validators.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import paramonov.valentine.loan_service.common.validators.exceptions.InvalidAmountException;
import paramonov.valentine.loan_service.common.validators.exceptions.InvalidPasswordException;
import paramonov.valentine.loan_service.common.validators.exceptions.InvalidTermException;
import paramonov.valentine.loan_service.common.validators.exceptions.InvalidUserNameException;
import paramonov.valentine.loan_service.common.vos.LoanApplicationVo;
import paramonov.valentine.loan_service.db.entities.User;
import paramonov.valentine.loan_service.properties.LoanServiceProperties;

import java.math.BigDecimal;

public class LoanApplicationValidatorImplTest {
    private LoanApplicationValidatorImpl loanApplicationValidatorImpl;
    private LoanServiceProperties loanServiceProperties;

    @Before
    public void setUp() {
        setupMocks();
        loanApplicationValidatorImpl = new LoanApplicationValidatorImpl();
        loanApplicationValidatorImpl.loanServiceProperties = loanServiceProperties;
    }

    @Test
    public void testValidateLoanApplication_WhenTermAndAmountAreValid_ShouldPass() {
        final LoanApplicationVo application = newApplication(5, 5);

        loanApplicationValidatorImpl.validateLoanApplication(application);
    }

    @Test(expected = InvalidAmountException.class)
    public void testValidateLoanApplication_WhenTermIsValidAmountIsNot_ShouldThrowException() {
        final LoanApplicationVo application = newApplication(-20, 5);

        loanApplicationValidatorImpl.validateLoanApplication(application);
    }

    @Test(expected = InvalidTermException.class)
    public void testValidateLoanApplication_WhenAmountIsValidTermIsNot_ShouldThrowException() {
        final LoanApplicationVo application = newApplication(5, 666);

        loanApplicationValidatorImpl.validateLoanApplication(application);
    }

    @Test
    public void testValidateUserName_WhenNameHasValidLength_ShouldPass() {
        final String userName = "Jenova";

        loanApplicationValidatorImpl.validateUserName(userName);
    }

    @Test(expected = InvalidUserNameException.class)
    public void testValidateUserName_WhenNameIsNull_ShouldThrowException() {
        final String userName = null;

        loanApplicationValidatorImpl.validateUserName(userName);
    }

    @Test(expected = InvalidUserNameException.class)
    public void testValidateUserName_WhenNameIsEmpty_ShouldThrowException() {
        final String userName = "";

        loanApplicationValidatorImpl.validateUserName(userName);
    }

    @Test(expected = InvalidUserNameException.class)
    public void testValidateUserName_WhenNameIsBlank_ShouldThrowException() {
        final String userName = "   ";

        loanApplicationValidatorImpl.validateUserName(userName);
    }

    @Test(expected = InvalidUserNameException.class)
    public void testValidateUserName_WhenNameIsShort_ShouldThrowException() {
        final String userName = "Sid";

        loanApplicationValidatorImpl.validateUserName(userName);
    }    
    
    @Test
    public void testValidatePassword_WhenPasswordHasValidLength_ShouldPass() {
        final String password = "pass@word";

        loanApplicationValidatorImpl.validatePassword(password);
    }

    @Test(expected = InvalidPasswordException.class)
    public void testValidatePassword_WhenPasswordIsNull_ShouldThrowException() {
        final String password = null;

        loanApplicationValidatorImpl.validatePassword(password);
    }

    @Test(expected = InvalidPasswordException.class)
    public void testValidatePassword_WhenPasswordIsEmpty_ShouldThrowException() {
        final String password = "";

        loanApplicationValidatorImpl.validatePassword(password);
    }

    @Test(expected = InvalidPasswordException.class)
    public void testValidatePassword_WhenPasswordIsBlank_ShouldThrowException() {
        final String password = "    ";

        loanApplicationValidatorImpl.validatePassword(password);
    }

    @Test(expected = InvalidPasswordException.class)
    public void testValidatePassword_WhenPasswordIsShort_ShouldThrowException() {
        final String password = "4c3";

        loanApplicationValidatorImpl.validatePassword(password);
    }

    private void setupMocks() {
        loanServiceProperties = Mockito.mock(LoanServiceProperties.class);
        Mockito.when(loanServiceProperties.minAmount()).thenReturn(BigDecimal.ZERO);
        Mockito.when(loanServiceProperties.maxAmount()).thenReturn(BigDecimal.TEN);
        Mockito.when(loanServiceProperties.minTermDays()).thenReturn(1);
        Mockito.when(loanServiceProperties.maxTermDays()).thenReturn(10);
        Mockito.when(loanServiceProperties.minUserNameLength()).thenReturn(4);
        Mockito.when(loanServiceProperties.minPasswordLength()).thenReturn(6);
    }

    private LoanApplicationVo newApplication(final Number amount, final Integer term) {
        return new LoanApplicationVo() {
            @Override
            public User getApplicant() {
                return null;
            }

            @Override
            public BigDecimal getAmount() {
                return new BigDecimal(amount.toString());
            }

            @Override
            public Integer getTerm() {
                return term;
            }

            @Override
            public String getApplicantIp() {
                return null;
            }
        };
    }
}
