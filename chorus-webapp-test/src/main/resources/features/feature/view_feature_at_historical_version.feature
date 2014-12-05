Uses: Chorus Webapp
Uses: Chorus Feature
Uses: Chorus Feature History
Uses: Processes

Feature: As a user, I want to view features previous versions
         So that...

  Feature-Start:
    Given chorus-web is running with no features

  Scenario: View deleted feature prior to deletion
   Given the feature SomePackage.FeatureRetrieval.Foo has the text "i like dinosaurs"
     And the feature SomePackage.FeatureRetrieval.Foo was deleted
    When a user views the previous version of feature SomePackage.FeatureRetrieval.Foo
    Then the feature must have the text "i like dinosaurs"

  Feature-End:
    I stop the stop the process named foo
