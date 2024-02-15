Feature: Rest feature

  Background: regenerate
    Given regenerate context

#  Scenario: trade happy pass
#    Given rest one security "WSB" and two users "Diamond" and "Paper" exist
#    When user "Diamond" puts a buy order for security "WSB" with a price of 101 and quantity of 50
#    And user "Paper" puts a sell order for security "WSB" with a price of 100 and a quantity of 100
#    Then a trade occurs with the price of 100 and quantity of 50

  @API
  Scenario: create user via Rest API
    Given a user with "rest" username exists via rest
    When the client requests GET users lastCreatedId
    Then the latest user response status code should be 200
    And the response should contain user "rest" username

  @API
  Scenario: test data are not dependant on other tests
    Given a user with "rest" username exists
    When the client requests GET users lastCreatedId
    Then the latest user response status code should be 200
    And the response should contain user "rest" username

  @API
  Scenario: Create security via repo
    Given security with "security" name exists
    When the client requests GET security lastCreatedId
    Then the latest security response status code should be 200
    And the response should contain security "rest" name

  @API
  Scenario: Create security via rest
    Given security with "security" name exists via rest
    When the client requests GET security lastCreatedId
    Then the latest security response status code should be 200
    And the response should contain security "rest" name
