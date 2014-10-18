package paramonov.valentine.loan_service.acceptance;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import paramonov.valentine.loan_service.db.entities.User;

import java.math.BigDecimal;

public class ApplicationStepDefinitions {
    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    @Given("^a user \"(.*)\" with password \"(.*)\"$")
    public void aUserWithPassword(String userName, String password) {
        final User user = new User()
            .setName(userName)
            .setPassword(password);

        sessionFactory
            .getCurrentSession()
            .save(user);
    }

    @When("^the user performs (.*) request with the amount of (.*) and the term of (.*)$")
    public void theUserPerformsRequestWithAmountAndTerm(RequestMethod method, BigDecimal amount, Integer term) {
        throw new PendingException();
    }

    @Then("the response status should be (.*)")
    public void theResponseStatusShouldBe(String status) {
        throw new PendingException();
    }
}
