Feature: Basic access

  @scope:smoke
  @scope:production
  Scenario: Access to home page
    When I navigate to the home page
    Then I am on the home page

  @scope:smoke
  Scenario: Login
    Given I navigate to the home page
    And login as admin
    Then I am on the home page

  Scenario: Redirection when page is not authorised
    When I go to account management page
    Then I am on the home page
    And login screen is displayed
    When I log as admin
    Then I am on the account management page
