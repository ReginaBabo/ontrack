Feature: Management of projects

  Scenario: Project creation
    Given login as admin
    And a unique name for project "a"
    When I create a project "<project:a>"
    Then project "<project:a>" is present in home page

  Scenario: Project creation - name already exists
    Given login as admin
    And a unique name for project "a"
    When I create a project "<project:a>"
    And I create a project "<project:a>"
    Then project dialog shows error "Project name already exists: <project:a>"

  Scenario: Project API page must be accessible
    Given login as admin
    And a project "a"
    And I go to project page for "a"
    And I navigate to API page
    Then link on API page is "/structure/projects/<id:a>"
