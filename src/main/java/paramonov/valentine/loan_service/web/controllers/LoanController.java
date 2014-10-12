package paramonov.valentine.loan_service.web.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import paramonov.valentine.loan_service.web.managers.LoanManager;
import paramonov.valentine.loan_service.web.managers.RequestManager;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@Controller
@RequestMapping(value = "/loan")
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
    @RequestMapping(value = "/apply", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void apply(
        @RequestParam(required = true) String amount,
        @RequestParam(required = true) String term,
        HttpServletRequest request) {

        final String ipAddress = requestManager.getIpAddress(request);

        log.info("Request IP: {}", ipAddress);
        log.info("Request User: {}", request.getRemoteUser());
        log.info("Request Session ID: {}", request.getRequestedSessionId());

        loanManager.sayHi();
    }
}
