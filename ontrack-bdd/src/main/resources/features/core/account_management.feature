Feature: Management of accounts

  Scenario: Account with special characters in password
    Given an account "test" having "test§" as password
    When login with the "test" account
    Then I am on the home page
