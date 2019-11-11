Feature: Management of projects

  Scenario: Project creation
    Given login as admin
    When I create a project "a"
    Then project "a" is present in home page
