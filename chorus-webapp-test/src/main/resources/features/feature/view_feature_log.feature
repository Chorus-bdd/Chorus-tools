Uses: Chorus Webapp
Uses: Chorus Feature
Uses: Chorus Feature History
Uses: Processes

Feature: As a user, I want to view a features history
         So that...

  Feature-Start:
    Given chorus-web is running with no features
      And the package Foo.Log exists with sub-features

  Scenario: Feature created in root package is logged
    Given the feature LogFeature has the text "i like dinosaurs"
    When a user views the features log
    Then the log must have one entry
     And the comment must be "Created feature 'LogFeature'"
     And the author name must be "Unknown User"

  Scenario: Feature created in sub-package is logged
    Given the feature Foo.Log.Feature1 has the text "i like dinosaurs"
    When a user views the features log
    Then the log must have one entry
     And the comment must be "Created feature 'Foo.Log.Feature1'"

  Scenario: Feature modification is logged
    Given the feature Foo.Log.Feature2 has the text "i like dinosaurs"
      And the feature was modified with the text "cats are better"
     When a user views the features log
     Then the log must have 2 entries
      And on the most recent entry
          the comment must be "Modified feature 'Foo.Log.Feature2'"

  Scenario: Multiple feature modifications are all logged
    Given the feature Foo.Log.Feature3 has the text "i like dinosaurs"
      And the feature was modified with the text "cats are better"
      And the feature was modified with the text "no dinosaurs!"
     When a user views the features log
     Then the log must have 3 entries
      And on the most recent entry
          the comment must be "Modified feature 'Foo.Log.Feature3'"

  Scenario: Feature deletion is logged
    Given the feature Foo.Log.Feature4 has the text "i like dinosaurs"
      And the feature was deleted
     When a user views the features log
     Then the log must have 2 entries
      And on the most recent entry
          the comment must be "Deleted feature 'Foo.Log.Feature4'"

  Scenario: Feature move is logged and the log can be found by the new feature name
    Given the feature Foo.Log.Feature5 exists
      And the feature was moved to Foo.Log.Target.Feature6
     When a user views the log for feature Foo.Log.Target.Feature6
     Then the log must have 2 entries
      And on the most recent entry
          the comment must be "Moved feature 'Foo.Log.Feature5' to 'Foo.Log.Target.Feature6'"

  Scenario: Feature move is logged and the log can be found by the old feature name
    Given the feature Foo.Log.Feature7 exists
      And the feature was moved to Foo.Log.Target.Feature8
     When a user views the log for feature Foo.Log.Feature7
     Then the log must have 2 entries
      And on the most recent entry
          the comment must be "Moved feature 'Foo.Log.Feature7' to 'Foo.Log.Target.Feature8'"

  ## -------------------------------------------------------------------------

  Step-Macro: the package <package> exists with sub-features
       As the feature <package>.Bar.Far.StuffingFeature1 exists
      And the feature <package>.Bar.StuffingFeature2 exists
      And the feature <package>.StuffingFeature3 exists
      And the feature <package>.StuffingFeature4 exists

  Feature-End:
    I stop the stop the process named foo
