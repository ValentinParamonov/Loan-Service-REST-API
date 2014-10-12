package paramonov.valentine.loan_service.web.managers.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import paramonov.valentine.loan_service.web.managers.RequestManager;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class RequestManagerImplTest {
    private static final String XFF_HEADER = "X-FORWARDED-FOR";
    private static final String IP_ADDRESS_XFF = "13.37.14.88";
    private static final String IP_ADDRESS_WO_XFF = "88.14.37.13";

    private RequestManager requestManager;

    @Mock
    private HttpServletRequest requestWithXff;

    @Mock
    private HttpServletRequest requestWithoutXff;

    @Before
    public void setUp() {
        requestManager = new RequestManagerImpl();
        setupRequestMocks();
    }

    @Test
    public void testGetIpAddress_WhenNoXFF_ShouldBeIpAddressWoXff() {
        final String ipAddress = requestManager.getIpAddress(requestWithoutXff);

        assertThat(ipAddress, equalTo(IP_ADDRESS_WO_XFF));
    }

    @Test
    public void testGetIpAddress_WhenWithXFF_ShouldBeIpAddressXff() {
        final String ipAddress = requestManager.getIpAddress(requestWithXff);

        assertThat(ipAddress, equalTo(IP_ADDRESS_XFF));
    }

    private void setupRequestMocks() {
        Mockito.when(requestWithXff.getHeader(XFF_HEADER)).thenReturn(IP_ADDRESS_XFF);
        Mockito.when(requestWithoutXff.getRemoteAddr()).thenReturn(IP_ADDRESS_WO_XFF);
    }
}
