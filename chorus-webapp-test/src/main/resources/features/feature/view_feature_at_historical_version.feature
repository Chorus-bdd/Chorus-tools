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

  Scenario: View feature prior to move
   Given the feature SomePackage1.FeatureRetrieval.Foo has the text "i like otters"
     And the feature was moved to SomePackage1.FeatureRetrieval.Bar
    When a user views the previous version of feature SomePackage1.FeatureRetrieval.Bar
    Then the feature must have the text "i like otters"

  Scenario: View feature prior to move using old name
   Given the feature SomePackage2.FeatureRetrieval.Foo has the text "i like otters"
     And the feature was modified with the text "i really likes otters"
     And the feature was moved to SomePackage2.FeatureRetrieval.Bar
    When a user views the previous version of feature SomePackage2.FeatureRetrieval.Foo
    Then the feature must have the text "i really likes otters"

  Scenario: View feature prior to deletion and restoration
   Given the feature SomePackage4.FeatureRetrieval.Foo has the text "i like dinosaurs"
     And the feature SomePackage4.FeatureRetrieval.Foo was deleted
     And the feature SomePackage4.FeatureRetrieval.Foo has the text "i like otters"
    When a user views the -2 version of feature SomePackage4.FeatureRetrieval.Foo
    Then the feature must have the text "i like dinosaurs"

  Scenario: Feature retrieval from deleted version displays "file not present in version" error
   Given the feature SomePackage.FeatureRetrieval5.Foo has the text "i like dinosaurs"
     And the feature SomePackage.FeatureRetrieval5.Foo was deleted
     And the feature SomePackage.FeatureRetrieval5.Foo has the text "i like otters"
    When a user views the -1 version of feature SomePackage.FeatureRetrieval5.Foo
    Then the user is notified that "the feature does not exist in this version"

#  This could be fixed by adding a modal 'follow-moves' flag, by default 'follow' is currently on, we could allow
#  the user to turn this off in the web-interface which in this scenario would expose the lost file
#  We could keep this option hidden by default and only display it if there is a difference in the 'follow' and
# 'non-follow' log.
  Scenario: A deleted feature which is replaced with a 'move' is lost :-(
   Given the feature SomePackage.FeatureRetrieval6.Foo has the text "i like dinosaurs"
     And the feature SomePackage.FeatureRetrieval6.Bar has the text "i don't like dinosaurs"
     And the feature SomePackage.FeatureRetrieval6.Bar was deleted
     And the feature SomePackage.FeatureRetrieval6.Foo was moved to SomePackage.FeatureRetrieval6.Bar
    When a user views the -1 version of feature SomePackage.FeatureRetrieval6.Bar
    Then the feature must have the text "i like dinosaurs"

  Feature-End:
    I stop the stop the process named foo