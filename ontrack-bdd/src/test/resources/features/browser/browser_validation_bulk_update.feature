Feature: Buld update of validation stamps

  Scenario: Validation bulk update accessible to global validation manager
    Given a project "a"
    And a branch "b" in project "a"
    And a validation stamp "VS" in branch "b" of project "a"
    And a "ValidationManagers" account group
    And the "ValidationManagers" account group is granted the "GLOBAL_VALIDATION_MANAGER" role
    And a "test" account belonging to the "ValidationManagers" account group
    When logging with the "test" account
    And going to the page of the validation stamp "VS" in branch "b" of project "a"
    Then check that the validation stamp page contains the bulk update command
