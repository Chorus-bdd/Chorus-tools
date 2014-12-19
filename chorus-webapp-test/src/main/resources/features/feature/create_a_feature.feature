Uses: Chorus Webapp
Uses: Chorus Feature
Uses: Chorus Package
Uses: Processes

@InProcess
Feature: As a user, I want to create features
         So that the system under test is clearly documented which:
            * reduces the learning curve for new team members
            * lowers maintenance costs - older features and fringe behaviour is not forgotten
            * improves the communication of requirements

  Feature-Start:
    Given chorus-web is running with no features

  Scenario: Creates feature in the root package
      When a user creates the feature Hello with the text "foo bar far"
      Then the feature Hello must have the text "foo bar far"
                         And must have the name "Hello"
                         And must have the package ""


  Scenario: Creates feature with numbers in the name
      When a user creates the feature HelloWorld123 with the text "foo bar far"
      Then the feature HelloWorld123 must have the text "foo bar far"
                                 And must have the name "HelloWorld123"

  Scenario: Creates feature with sentence name
      When a user creates the feature Hello_cruel_World1_2 with the text "foo bar far"
      Then the feature Hello_cruel_World1_2 must have the text "foo bar far"

  #@NotImplemented - need to figure out rules for escaping characters etc
  #Scenario: Creates feature with special characters
  #    When a user creates the feature Hello with the text "foo\nbar\r\n \tfar"
  #    Then the feature Hello must have the text "foo\nbar\r\n \tfar"

  Scenario: Creates feature in a package that does not exist
        When a user creates the feature Foo.Bar.Hello_World with the text "foo bar far"
        Then the feature Foo.Bar.Hello_World must have the text "foo bar far"

  Scenario: Creates feature in a package that already exist
        Given the package Foo exists
        When a user creates the feature Foo.Hello_World with the text "foo bar far"
        Then the feature Foo.Hello_World must have the text "foo bar far"

  Scenario: Creates multiple feature in the same package
       Given the feature Foo.Bar.Hello_World exists
        When a user creates the feature Foo.Bar.Another_World with the text "can haz requirements!"
        Then the feature Foo.Bar.Another_World must have the text "can haz requirements!"


  Feature-End:
    I stop the stop the process named foo
