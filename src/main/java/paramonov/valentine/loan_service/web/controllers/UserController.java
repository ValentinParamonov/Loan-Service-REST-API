package paramonov.valentine.loan_service.web.controllers;

import org.eclipse.jetty.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import paramonov.valentine.loan_service.common.dtos.LoanServiceErrorDto;
import paramonov.valentine.loan_service.common.validators.exceptions.InvalidPasswordException;
import paramonov.valentine.loan_service.common.validators.exceptions.InvalidUserNameException;
import paramonov.valentine.loan_service.web.managers.UserManager;
import paramonov.valentine.loan_service.web.managers.exceptions.UserNameAlreadyExistsException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(value = "/users")
class UserController {
    @Autowired
    private UserManager userManager;

    @Transactional
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody LoanServiceErrorDto register(
        @RequestParam(required = true) String userName,
        @RequestParam(required = true) String password,
        HttpServletResponse response) throws IOException {

        try {
            userManager.registerUser(userName, password);
        } catch(InvalidUserNameException iune) {
            response.setStatus(HttpStatus.BAD_REQUEST_400);
            return errorMessage("Invalid user name value");
        } catch(InvalidPasswordException ipe) {
            response.setStatus(HttpStatus.BAD_REQUEST_400);
            return errorMessage("Invalid password value");
        } catch(UserNameAlreadyExistsException unaee) {
            response.setStatus(HttpStatus.FORBIDDEN_403);
            return errorMessage("User name taken");
        }

        response.setStatus(HttpStatus.CREATED_201);
        return null;
    }

    private LoanServiceErrorDto errorMessage(String message) {
        return new LoanServiceErrorDto()
            .setError(message);
    }
}
