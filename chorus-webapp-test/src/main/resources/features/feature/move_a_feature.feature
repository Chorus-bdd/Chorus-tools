Uses: Chorus Webapp
Uses: Chorus Feature
Uses: Chorus Package
Uses: Processes

Feature: As a user, I want to move features
         So that the documentation continuously reflects the evolving system

  Feature-Start:
    Given chorus-web is running with no features

  Scenario: User moves a feature
     Given the feature MoveFeature.Foo.Bar.Hello_World exists
      When a user moves the feature to MoveFeature.Foo.GoodbyeWorld
      Then the feature MoveFeature.Foo.Bar.Hello_World must not exist
       And the feature MoveFeature.Foo.GoodbyeWorld must exist

  Scenario: User moves a package
     Given the package MovePackage.Foo exists with sub-features
      When a user moves the package MovePackage.Foo to MovePackage.Sar.Lar
      Then the package MovePackage.Foo sub-features must not exist
       And the sub-features must exist in MovePackage.Sar.Lar

  Feature-End:
    I stop the stop the process named foo

  ## --------------------------------------------------------------------------

  Step-Macro: the package <package> exists with sub-features
       As the feature <package>.Bar.Far.Feature1 exists
      And the feature <package>.Bar.Feature2 exists
      And the feature <package>.Feature3 exists
      And the feature <package>.Feature4 exists

  Step-Macro: the package <package> sub-features must not exist
       As the feature <package>.Bar.Far.Feature1 must not exist
      And the feature <package>.Bar.Feature2 must not exist
      And the feature <package>.Feature3 must not exist
      And the feature <package>.Feature4 must not exist

  Step-Macro: the sub-features must exist in <package>
        As the feature <package>.Bar.Far.Feature1 must exist
       And the feature <package>.Bar.Feature2 must exist
       And the feature <package>.Feature3 must exist
       And the feature <package>.Feature4 must exist
