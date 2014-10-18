Feature: Loan Application
  As a user,
  I want to submit a loan application
  So that I can get a loan

  Scenario: When user applies for a loan the response should be OK

    Given a user "John" with password "pass@word"
    When the user performs POST request with the amount of 200 and the term of 20
    Then the response status should be OK
