package paramonov.valentine.loan_service.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import paramonov.valentine.loan_service.web.managers.UserManager;
import paramonov.valentine.loan_service.web.managers.exceptions.UserNameAlreadyExistsException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
class SecurityController {
    @Autowired
    private UserManager userManager;

    @Transactional
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void register(
        @RequestParam(required = true) String userName,
        @RequestParam(required = true) String password,
        HttpServletResponse response) throws IOException {

        try {
            userManager.registerUser(userName, password);
        } catch(UserNameAlreadyExistsException unaee) {
            response.sendError(org.eclipse.jetty.http.HttpStatus.FORBIDDEN_403, "User name taken");
        }
    }
}
