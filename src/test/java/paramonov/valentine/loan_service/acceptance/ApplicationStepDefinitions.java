package paramonov.valentine.loan_service.acceptance;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.jasypt.contrib.org.apache.commons.codec_1_3.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ApplicationStepDefinitions {
    private ResponseEntity<?> response;
    private HttpEntity<String> authorizationRequest;

    @Given("^a user \"(.*)\" with password \"(.*)\"$")
    public void aUserWithPassword(final String userName, final String password) {
        final RestTemplate template = new RestTemplate();
        final String url = "http://localhost:{port}/user?userName={name}&password={password}";
        final ResponseEntity<?> response = template.exchange(url, HttpMethod.POST, null, (Class) null,
            CucumberConfig.TEST_PORT, userName, password);
        final HttpStatus status = response.getStatusCode();

        assertThat(status, equalTo(HttpStatus.CREATED));

        authorizationRequest = getAuthorizationRequest(userName, password);
    }

    @When("^the user performs (.*) request with the amount of (.*) and the term of (.*)$")
    public void theUserPerformsRequestWithAmountAndTerm(HttpMethod method, BigDecimal amount, Integer term) {
        final RestTemplate template = new RestTemplate();
        final String url = "http://localhost:{port}/loan?amount={amount}&term={term}";
        response = template.exchange(url, method, authorizationRequest, (Class) null, CucumberConfig.TEST_PORT, amount, term);
    }

    @Then("the response status should be (.*)")
    public void theResponseStatusShouldBe(HttpStatus status) {
        final HttpStatus responseStatus = response.getStatusCode();

        assertThat(responseStatus, equalTo(status));
    }

    private HttpEntity<String> getAuthorizationRequest(String userName, String password) {
        final String credentials = getCredentials(userName, password);
        final HttpHeaders headers = getAuthorizationHeaders(credentials);

        return new HttpEntity<>(headers);
    }

    private HttpHeaders getAuthorizationHeaders(String credentials) {
        final HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "Basic " + credentials);

        return headers;
    }

    private String getCredentials(String userName, String password) {
        final String plainCredentials = userName + ":" + password;
        final byte[] encodedCredentials = Base64.encodeBase64(plainCredentials.getBytes());
        return new String(encodedCredentials);
    }
}
