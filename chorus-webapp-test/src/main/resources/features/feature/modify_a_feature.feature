Uses: Chorus Webapp
Uses: Chorus Feature
Uses: Chorus Package
Uses: Processes

@InProcess
Feature: As a user, I want to modify features
         So that the documentation continuously reflects the evolving system

  Feature-Start:
    Given chorus-web is running with no features

  Scenario: User modifies an existing feature
     Given the feature Modify.Hello_World has the text "i am feature"
      When a user modifies the feature text with "now is my feature"
      Then the feature must have the text "now is my feature"

  Scenario: User modifies a feature which conflicts with another users modification
     Given the feature Modify_Conflict.Hello_World exists
       And a user Mary has opened the feature for editing
      When a different user modifies the feature text with "i haz many important business requirements!1!!"
       And Mary submits her edit with the text "product should does thingz"
      Then Mary must be presented with a Modification Conflict Error
       And the feature Modify_Conflict.Hello_World must have the text "i haz many important business requirements!1!!"

  Scenario: User modifies a feature which conflicts with another users deletion
     Given the feature Delete_Conflict.Hello_World exists
       And a user Mary has opened the feature for editing
      When a different user deletes the feature
       And Mary submits her edit with the text "i like cats"
      Then Mary must be presented with a Modification Conflict Error
       And the feature Delete_Conflict.Hello_World must not exist

  Feature-End:
    I stop the stop the process named foo