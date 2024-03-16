@API
Feature: Rest feature

  Background: regenerate
    Given regenerate context for rest

  Scenario: Create User via Rest API
    Given a user with "Piper" username exists via rest
    When the client requests GET users lastCreatedId
    Then the latest user response status code should be 200
    And the response should contain user "Piper" username
    And User GET has "Piper" name

  Scenario: Create Security via Rest API
    Given security with "WSB" name exists via rest
    When the client requests GET security lastCreatedId
    Then the latest security response status code should be 200
    And the response should contain security "WSB" name
    And Security GET has "WSB" name

  Scenario: trade happy pass via Rest
    Given security with "WSB" name exists via rest
    And a user with "Piper" username exists via rest
    And a user with "Diamond" username exists via rest
    When user "Diamond" puts a "buy" order for security "WSB" with a price of 101 and a quantity of 50 via rest
    And user "Piper" puts a "sell" order for security "WSB" with a price of 100 and a quantity of 100 via rest
    And trigger make a trade job
    Then a trade occurs with the price of 100 and quantity of 50 via rest