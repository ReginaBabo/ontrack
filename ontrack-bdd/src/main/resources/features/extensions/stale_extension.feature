Feature: Stale extension

  @feature=extension
  @extension=stale
  Scenario: Stale property
    Given a project "a"
    Then stale property on project "a" is not defined
    When stale property on project "a" is set to:
      | disablingDuration | 15 |
      | deletingDuration  | 30 |
      | promotionsToKeep  |    |
    Then stale property on project "a" is equal to:
      | disablingDuration | 15 |
      | deletingDuration  | 30 |
      | promotionsToKeep  |    |

  @feature=extension
  @extension=stale
  Scenario: Stale property with promotions to keep
    Given a project "a"
    Then stale property on project "a" is not defined
    When stale property on project "a" is set to:
      | disablingDuration | 15                  |
      | deletingDuration  | 30                  |
      | promotionsToKeep  | DELIVERY,PRODUCTION |
    Then stale property on project "a" is equal to:
      | disablingDuration | 15                  |
      | deletingDuration  | 30                  |
      | promotionsToKeep  | DELIVERY,PRODUCTION |
