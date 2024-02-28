@CoreRegression
Feature: Dependency injection feature

  Scenario: Dependency injection demonstration
    Given access ScenarioScoped in StepHooks and check onInit is true
    When setOnCall true in CoreRegressionStepDefs
    Then check onCall value is true in RestSteps

  Scenario: Dependency injection is Scenario independent
    Given access ScenarioScoped in StepHooks and check onInit is true
    Then check onCall value is false in RestSteps