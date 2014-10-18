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
import paramonov.valentine.loan_service.common.dtos.LoanServiceErrorDto;
import paramonov.valentine.loan_service.common.vos.LoanApplicationVo;
import paramonov.valentine.loan_service.common.vos.LoanServiceVoBuilder;
import paramonov.valentine.loan_service.db.entities.User;
import paramonov.valentine.loan_service.web.resolvers.annotations.ActiveUser;
import paramonov.valentine.loan_service.web.managers.LoanManager;
import paramonov.valentine.loan_service.web.managers.RequestManager;
import paramonov.valentine.loan_service.web.managers.exceptions.ApplicationRejectedException;
import paramonov.valentine.loan_service.common.validators.exceptions.InvalidAmountException;
import paramonov.valentine.loan_service.web.managers.exceptions.InvalidApplicationIdException;
import paramonov.valentine.loan_service.common.validators.exceptions.InvalidTermException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(value = "/loans")
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
    public @ResponseBody LoanServiceErrorDto apply(
        @RequestParam(required = true) BigDecimal amount,
        @RequestParam(required = true) Integer term,
        HttpServletRequest request,
        HttpServletResponse response,
        @ActiveUser User user) {

        final String ipAddress = requestManager.getIpAddress(request);
        final LoanApplicationVo applicationDetails = loanServiceVoBuilder.newLoanApplicationVo(user, amount, term, ipAddress);

        try {
            loanManager.applyForLoan(applicationDetails);
        } catch(InvalidAmountException iae) {
            response.setStatus(HttpStatus.BAD_REQUEST_400);
            return errorMessage("Invalid amount");
        } catch(InvalidTermException ite) {
            response.setStatus(HttpStatus.BAD_REQUEST_400);
            return errorMessage("Invalid term");
        } catch(ApplicationRejectedException ade) {
            response.setStatus(HttpStatus.FORBIDDEN_403);
            return errorMessage("Application rejected");
        }

        response.setStatus(HttpStatus.OK_200);
        return null;
    }

    @Transactional
    @RequestMapping(value = "/{applicationId}", method = RequestMethod.POST)
    public @ResponseBody LoanServiceErrorDto extend(
        @PathVariable Long applicationId,
        HttpServletRequest request,
        HttpServletResponse response,
        @ActiveUser User user) {

        final String ipAddress = requestManager.getIpAddress(request);
        final LoanApplicationVo application = loanServiceVoBuilder.newLoanApplicationVo(user, ipAddress);

        try {
            loanManager.extendLoan(application, applicationId);
        } catch(InvalidApplicationIdException iaie) {
            response.setStatus(HttpStatus.BAD_REQUEST_400);
            return errorMessage("Invalid application id");
        } catch(ApplicationRejectedException ade) {
            response.setStatus(HttpStatus.FORBIDDEN_403);
            return errorMessage("Extension application rejected");
        }

        response.setStatus(HttpStatus.OK_200);
        return null;
    }

    @Transactional(readOnly = true)
    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(org.springframework.http.HttpStatus.OK)
    public @ResponseBody List<LoanEventDto> getHistory(@ActiveUser User user) {
        return loanManager.getLoanHistory(user);
    }

    private LoanServiceErrorDto errorMessage(String message) {
        return new LoanServiceErrorDto()
            .setError(message);
    }
}
