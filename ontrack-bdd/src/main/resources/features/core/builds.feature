Feature: Management of builds

  Scenario: Build page
    Given login as admin
    And a build "b"
    And I go to build page for "b"
    Then build page title is "Build <build:b>"
