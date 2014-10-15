package paramonov.valentine.loan_service.web.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import paramonov.valentine.loan_service.common.dtos.LoanApplicationDto;
import paramonov.valentine.loan_service.db.entities.User;
import paramonov.valentine.loan_service.web.annotations.ActiveUser;
import paramonov.valentine.loan_service.web.managers.LoanManager;
import paramonov.valentine.loan_service.web.managers.RequestManager;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@RestController
@RequestMapping(value = "/loan")
@PreAuthorize("isAuthenticated()")
class LoanController {
    @Autowired
    private LoanManager loanManager;

    @Autowired
    private RequestManager requestManager;

    private Logger log;

    @PostConstruct
    private void init() {
        log = LogManager.getLogger(getClass());
    }

    @Transactional
    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void apply(
        @RequestParam(required = true) BigDecimal amount,
        @RequestParam(required = true) Integer term,
        HttpServletRequest request,
        @ActiveUser User user) {

        final String ipAddress = requestManager.getIpAddress(request);
        final LoanApplicationDto application = new LoanApplicationDto()
            .withApplicant(user)
            .withAmount(amount)
            .withTerm(term)
            .withApplicantIp(ipAddress);

        try {
            loanManager.applyForLoan(application);
        } catch(RuntimeException re) {

        }
    }
}
