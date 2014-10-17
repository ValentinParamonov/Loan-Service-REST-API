package paramonov.valentine.loan_service.web.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import paramonov.valentine.loan_service.common.vos.LoanApplicationVo;
import paramonov.valentine.loan_service.common.vos.LoanServiceVoBuilder;
import paramonov.valentine.loan_service.db.entities.User;
import paramonov.valentine.loan_service.web.annotations.ActiveUser;
import paramonov.valentine.loan_service.web.managers.LoanManager;
import paramonov.valentine.loan_service.web.managers.RequestManager;
import paramonov.valentine.loan_service.web.managers.exceptions.ApplicationDeniedException;
import paramonov.valentine.loan_service.web.managers.exceptions.InvalidAmountException;
import paramonov.valentine.loan_service.web.managers.exceptions.InvalidTermException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@RestController
@RequestMapping(value = "/loan")
@PreAuthorize("isAuthenticated()")
class LoanController {
    @Autowired
    private LoanManager loanManager;

    @Autowired
    private RequestManager requestManager;

    @Autowired
    private LoanServiceVoBuilder loanServiceVoBuilder;

    private Logger log;

    @PostConstruct
    private void init() {
        log = LogManager.getLogger(getClass());
    }

    @Transactional
    @RequestMapping(method = RequestMethod.PUT)
    public void apply(
        @RequestParam(required = true) BigDecimal amount,
        @RequestParam(required = true) Integer term,
        HttpServletRequest request,
        HttpServletResponse response,
        @ActiveUser User user) throws IOException {

        final String ipAddress = requestManager.getIpAddress(request);
        final LoanApplicationVo application = loanServiceVoBuilder.newLoanApplicationVo(user, amount, term, ipAddress);

        try {
            loanManager.applyForLoan(application);
        } catch(InvalidAmountException iae) {
            response.sendError(HttpStatus.BAD_REQUEST_400, "Invalid amount");
        } catch(InvalidTermException ite) {
            response.sendError(HttpStatus.BAD_REQUEST_400, "Invalid term");
        } catch(ApplicationDeniedException ade) {
            response.sendError(HttpStatus.FORBIDDEN_403, "Application denied");
        }

        response.setStatus(HttpStatus.OK_200);
    }
}
