@API
Feature: Rest feature

  Background: regenerate
    Given regenerate context for rest

  Scenario: Create User via Rest API
    Given a user with "Piper" username exists via rest
    When the client requests GET users lastCreatedId
    Then the latest user response status code should be 200
    And the response should contain user "Piper" username

  Scenario: Create User via Rest API with already used name prefix
    Given a user with "Piper" username exists via rest
    When the client requests GET users lastCreatedId
    Then the latest user response status code should be 200
    And the response should contain user "Piper" username

  Scenario: Create Security via rest
    Given security with "WSB" name exists via rest
    When the client requests GET security lastCreatedId
    Then the latest security response status code should be 200
    And the response should contain security "WSB" name
