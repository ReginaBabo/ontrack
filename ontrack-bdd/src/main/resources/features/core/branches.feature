Feature: Management of branches

  Scenario: Complete creation of a branch
    Given login as admin
    And a unique name for project "P"
    And a unique name for branch "master"
    When I create a project "<project:P>"
    And I go to project "<project:P>"
    And I create a branch "<branch:master>"
    Then branch "<branch:master>" is present in project page

  Scenario: Creation of a branch after direct access to the project page
