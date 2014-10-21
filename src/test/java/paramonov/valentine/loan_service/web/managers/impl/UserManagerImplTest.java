package paramonov.valentine.loan_service.web.managers.impl;

import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import paramonov.valentine.loan_service.common.validators.LoanApplicationValidator;
import paramonov.valentine.loan_service.common.validators.exceptions.InvalidPasswordException;
import paramonov.valentine.loan_service.common.validators.exceptions.InvalidUserNameException;
import paramonov.valentine.loan_service.db.entities.User;
import paramonov.valentine.loan_service.db.repositories.UserRepository;
import paramonov.valentine.loan_service.web.managers.exceptions.UserNameAlreadyExistsException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ManagerTestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserManagerImplTest {
    private static String userName = "Marcus";
    private static String password= "Taargus";

    @Autowired
    private UserManagerImpl userManagerImpl;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoanApplicationValidator loanApplicationValidator;

    @Autowired
    private StringEncryptor stringEncryptor;

    @Test(expected = UserNameAlreadyExistsException.class)
    public void testCheckUserNameAvailable_WhenUserExists_ThrowsException() {
        doReturn(new User()).when(userRepository).getUserByName(userName);

        userManagerImpl.checkUserNameAvailable(userName);
    }

    @Test(expected = InvalidUserNameException.class)
    public void testRegisterUser_WhenValidatorThrowsInvalidUserNameException_ShouldBePropagated() {
        doThrow(InvalidUserNameException.class).when(loanApplicationValidator).validateUserName(userName);

        userManagerImpl.registerUser(userName, password);
    }

    @Test(expected = InvalidPasswordException.class)
    public void testRegisterUser_WhenValidatorThrowsInvalidPasswordException_ShouldBePropagated() {
        doThrow(InvalidPasswordException.class).when(loanApplicationValidator).validatePassword(password);

        userManagerImpl.registerUser(userName, password);
    }

    @Test
    public void testRegisterUser_WhenNoValidationExceptions_UserShouldBeSaved() {
        final String encryptedPassword = "encP4$$";
        doReturn(encryptedPassword).when(stringEncryptor).encrypt(password);

        userManagerImpl.registerUser(userName, password);

        verify(userRepository, atMost(1)).newUser(userName, encryptedPassword);
    }
}
