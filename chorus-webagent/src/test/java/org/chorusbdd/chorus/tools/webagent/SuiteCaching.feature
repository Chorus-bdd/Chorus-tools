
Uses: Processes

Feature: Suite Caching
  Start a chorus interpreter as a child process with a web agent configured as a remote listener
  Check the web agent receives a feature and inserts it into the web agent cache

  Scenario: Web Agent can receive a test suite from a chorus interpreter
    Given the web agent cache is empty
    And I start a chorusInterpreter process
    Then the web agent cache contains 1 test suite


