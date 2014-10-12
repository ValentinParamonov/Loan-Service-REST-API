package paramonov.valentine.loan_service.web.managers.impl;

import org.springframework.stereotype.Component;
import paramonov.valentine.loan_service.web.managers.RequestManager;

import javax.servlet.http.HttpServletRequest;

@Component("requestManager")
final class RequestManagerImpl implements RequestManager {
    @Override
    public String getIpAddress(HttpServletRequest request) {
        final String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if(ipAddress == null) {
            return request.getRemoteAddr();
        }

        return ipAddress;
    }
}
