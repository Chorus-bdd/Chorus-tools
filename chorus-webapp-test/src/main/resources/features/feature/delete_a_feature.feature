Uses: Chorus Webapp
Uses: Chorus Feature
Uses: Chorus Package
Uses: Processes

@InProcess
Feature: As a user, I want to delete features
         So that the documentation continuously reflects the evolving system

  Feature-Start:
    Given chorus-web is running with no features

  Scenario: User deletes an existing feature
     Given the feature Delete.Hello_World exists
      When a user deletes the feature
      Then the feature Delete.Hello_World must not exist

  @RunMe
  Scenario: User deletes a package
     Given the feature DeletePackage.Feature_1 exists
      When a user deletes the package DeletePackage
      Then the feature DeletePackage.Feature_1 must not exist
       And the package DeletePackage must not exist

  Scenario: User deletes a package with sub-packages
     Given the feature DeletePackage2.Feature_1 exists
       And the feature DeletePackage2.Sub.Feature_2 exists
       And the feature DeletePackage2.Sub.Feature_3 exists
      When a user deletes the package DeletePackage2
      Then the feature DeletePackage2.Feature_1 must not exist
       And the feature DeletePackage2.Sub.Feature_2 must not exist
       And the feature DeletePackage2.Sub.Feature_3 must not exist
       And the package DeletePackage2 must not exist

  Feature-End:
    I stop the stop the process named foo

