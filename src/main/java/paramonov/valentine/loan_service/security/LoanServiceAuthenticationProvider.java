package paramonov.valentine.loan_service.security;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import paramonov.valentine.loan_service.db.entities.User;
import paramonov.valentine.loan_service.db.repositories.UserRepository;

import javax.annotation.PostConstruct;

@Component("authenticationProvider")
public class LoanServiceAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StringEncryptor encryptor;

    private Logger log;

    @PostConstruct
    private void init() {
        log = LogManager.getLogger(getClass());
    }

    @Transactional(readOnly = true)
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String userName = authentication.getName();
        final User user = userRepository.getUserByName(userName);
        if(user == null) {
            throw newBadCredentialsException();
        }

        final String password = (String) authentication.getCredentials();
        final String userPassword = user.getPassword();
        final String decryptedUserPassword = decryptPassword(userPassword);

        if(!decryptedUserPassword.equals(password)) {
            throw newBadCredentialsException();
        }

        user.setPassword(null);

        return new UsernamePasswordAuthenticationToken(user, null, null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private BadCredentialsException newBadCredentialsException() {
        return new BadCredentialsException("Bad credentials");
    }

    private String decryptPassword(String userPassword) {
        try {
            return encryptor.decrypt(userPassword);
        } catch(EncryptionOperationNotPossibleException eonpe) {
            log.catching(Level.ERROR, eonpe);
            throw newBadCredentialsException();
        }
    }
}
