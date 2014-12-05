Uses: Chorus Webapp
Uses: Chorus Feature
Uses: Chorus Package
Uses: Chorus Feature History
Uses: Processes

Feature: As a user, I want to view a packages history
         So that...

  Feature-Start:
    Given chorus-web is running with no features
      And the package Foo.Log exists with sub-features

  Scenario: A package log should contain entries for each of its members
    Given the feature LogPackage.viewme.Feature1 exists
      And the feature LogPackage.viewme.Feature2 exists
      And the feature LogPackage.viewme.Feature2 was deleted
     When a user views the log for package LogPackage.viewme
     Then the log must have 3 entries
      And on the first entry
          the comment must be "Created feature 'LogPackage.viewme.Feature1'"
      And on the second entry
          the comment must be "Created feature 'LogPackage.viewme.Feature2'"
      And on the most recent entry
          the comment must be "Deleted feature 'LogPackage.viewme.Feature2'"

  Scenario: A package moved with multiple sub-features results in a log entry for the package
    Given the feature LogPackage.MP.Feature1 exists
      And the feature LogPackage.MP.Feature2 exists
      And the package LogPackage.MP was moved to LogPackage.PM
     When a user views the log for package LogPackage.PM
     Then the log must have one entry
      And the comment must be "Moved package 'LogPackage.MP' to 'LogPackage.PM'"
      And the author name must be "Unknown User"

  Scenario: A package moved with multiple sub-features results in log entries for each feature
    Given the feature LogPackage.MoveP.Feature1 exists
      And the feature LogPackage.MoveP.Feature2 exists
      And the package LogPackage.MoveP was moved to LogPackage.PMove
     When a user views the log for feature LogPackage.PMove.Feature1
     Then the log must have 2 entries
      And on the most recent entry
      And the comment must be "Moved package 'LogPackage.MoveP' to 'LogPackage.PMove'"
     When a user views the log for feature LogPackage.PMove.Feature2
     Then the log must have 2 entries
      And on the most recent entry
      And the comment must be "Moved package 'LogPackage.MoveP' to 'LogPackage.PMove'"

  Scenario: A package deleted results in a log entry for the package
    Given the feature LogPackage.DelP.Feature1 exists
      And the feature LogPackage.DelP.Feature2 exists
      And the package LogPackage.DelP was deleted
     When a user views the log for package LogPackage.DelP
     Then the log must have 3 entries
      And on the most recent entry
          the comment must be "Deleted package 'LogPackage.DelP'"

  Scenario: A package deleted with multiple sub-features results in log entries for each feature
    Given the feature LogPackage.deleteme.Feature1 exists
      And the feature LogPackage.deleteme.Feature2 exists
      And the package LogPackage.deleteme was deleted
     When a user views the log for feature LogPackage.deleteme.Feature1
     Then the log must have 2 entries
      And on the most recent entry
      And the comment must be "Deleted package 'LogPackage.deleteme'"
     When a user views the log for feature LogPackage.deleteme.Feature2
     Then the log must have 2 entries
      And on the most recent entry
      And the comment must be "Deleted package 'LogPackage.deleteme'"

  Scenario: The changeset for a package move includes all subfiles, before and after
    Given the feature LogPackage.changes.Feature1 exists
      And the feature LogPackage.changes.Feature2 exists
      And the package LogPackage.changes was moved to LogPackage.changes2
     When a user views the log for package LogPackage.changes
     Then on the most recent entries changeset
          the changeset contains DELETE LogPackage.changes.Feature1
      And the changeset contains DELETE LogPackage.changes.Feature2
      And the changeset contains ADD LogPackage.changes2.Feature1
      And the changeset contains ADD LogPackage.changes2.Feature2

  Scenario: The changeset for a package delete includes all subfiles
    Given the feature LogPackage.delchanges.Feature1 exists
      And the feature LogPackage.delchanges.Feature2 exists
      And the package LogPackage.delchanges was deleted
     When a user views the log for package LogPackage.delchanges
     Then on the most recent entries changeset
          the changeset contains DELETE LogPackage.delchanges.Feature1
      And the changeset contains DELETE LogPackage.delchanges.Feature2


  ## -------------------------------------------------------------------------

  Step-Macro: the package <package> exists with sub-features
       As the feature <package>.Bar.Far.StuffingFeature1 exists
      And the feature <package>.Bar.StuffingFeature2 exists
      And the feature <package>.StuffingFeature3 exists
      And the feature <package>.StuffingFeature4 exists

  Feature-End:
    I stop the stop the process named foo
