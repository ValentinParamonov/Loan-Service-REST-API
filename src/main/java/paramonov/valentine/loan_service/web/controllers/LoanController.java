package paramonov.valentine.loan_service.web.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import paramonov.valentine.loan_service.common.dtos.LoanEventDto;
import paramonov.valentine.loan_service.common.vos.LoanApplicationVo;
import paramonov.valentine.loan_service.common.vos.LoanServiceVoBuilder;
import paramonov.valentine.loan_service.db.entities.User;
import paramonov.valentine.loan_service.web.annotations.ActiveUser;
import paramonov.valentine.loan_service.web.managers.LoanManager;
import paramonov.valentine.loan_service.web.managers.RequestManager;
import paramonov.valentine.loan_service.web.managers.exceptions.ApplicationDeniedException;
import paramonov.valentine.loan_service.common.validators.exceptions.InvalidAmountException;
import paramonov.valentine.loan_service.web.managers.exceptions.InvalidApplicationIdException;
import paramonov.valentine.loan_service.common.validators.exceptions.InvalidTermException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

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
    @RequestMapping(method = RequestMethod.POST)
    public void apply(
        @RequestParam(required = true) BigDecimal amount,
        @RequestParam(required = true) Integer term,
        HttpServletRequest request,
        HttpServletResponse response,
        @ActiveUser User user) throws IOException {

        final String ipAddress = requestManager.getIpAddress(request);
        final LoanApplicationVo applicationDetails = loanServiceVoBuilder.newLoanApplicationVo(user, amount, term, ipAddress);

        try {
            loanManager.applyForLoan(applicationDetails);
        } catch(InvalidAmountException iae) {
            response.sendError(HttpStatus.BAD_REQUEST_400, "Invalid amount");
        } catch(InvalidTermException ite) {
            response.sendError(HttpStatus.BAD_REQUEST_400, "Invalid term");
        } catch(ApplicationDeniedException ade) {
            response.sendError(HttpStatus.FORBIDDEN_403, "Application denied");
        }

        response.setStatus(HttpStatus.OK_200);
    }

    @Transactional
    @RequestMapping(value = "/{applicationId}", method = RequestMethod.POST)
    public void extend(
        @PathVariable Long applicationId,
        HttpServletRequest request,
        HttpServletResponse response,
        @ActiveUser User user) throws IOException {

        final String ipAddress = requestManager.getIpAddress(request);
        final LoanApplicationVo application = loanServiceVoBuilder.newLoanApplicationVo(user, ipAddress);

        try {
            loanManager.extendLoan(application, applicationId);
        } catch(InvalidApplicationIdException iaie) {
            response.sendError(HttpStatus.BAD_REQUEST_400, "Invalid application id");
        } catch(ApplicationDeniedException ade) {
            response.sendError(HttpStatus.FORBIDDEN_403, "Extension application denied");
        }

        response.setStatus(HttpStatus.OK_200);
    }

    @Transactional(readOnly = true)
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseStatus(org.springframework.http.HttpStatus.OK)
    public @ResponseBody List<LoanEventDto> getHistory(@ActiveUser User user) {
        return loanManager.getLoanHistory(user);
    }
}
