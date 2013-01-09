Uses: Timers
Uses: Web Agent Self Test

Feature: Feature Which Fails
  This is a test feature with two scenarios which fail

  Scenario: Scenario Which Fails With Undefined Steps
    Given I run a scenario with several steps
    And at least one step is undefined
    Then the scenario will fail horribly
    And show up in red in the web agent html

  Scenario: Scenario Which Fails Due To Step Failure
    Given I run a scenario with several steps
    And a step fails an assertion
    Then the scenario will be failed
    And the feature will be failed
    And subsequent steps are skipped
    And the web agent html will be a sea of red

  Scenario: Scenario Which Failes Due To Timeout
    Given I run a scenario with several steps
    And chorus scenario timeout is set to 2 seconds
    And I wait for four seconds for timeout
    Then the previous step timed out
    And subsequent steps are skipped




