package paramonov.valentine.loan_service.web.managers;

import javax.servlet.http.HttpServletRequest;

public interface RequestManager {
    String getIpAddress(HttpServletRequest request);
}
