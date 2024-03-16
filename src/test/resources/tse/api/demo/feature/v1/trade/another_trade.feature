@Trade2
@TradeAll
Feature: Another Trade feature to demonstrate Tag control from Runner class

  Background: another feature regenerate
    Given regenerate context

  Scenario: trade happy pass 2
    Given one security "WSB" and two users "Diamond" and "Paper" exist
    When user "Diamond" puts a buy order for security "WSB" with a price of 101 and quantity of 50
    And user "Paper" puts a sell order for security "WSB" with a price of 100 and a quantity of 100
    Then a trade occurs with the price of 100 and quantity of 50

  Scenario: buy order remain open after trade 2
    Given one security "WSB" and two users "Diamond" and "Paper" exist
    When user "Diamond" puts a buy order for security "WSB" with a price of 100 and quantity of 500
    And user "Paper" puts a sell order for security "WSB" with a price of 90 and a quantity of 70
    Then a trade occurs with the price of 90 and quantity of 70
    And after a trade buy order still open

  Scenario: no buy order price coverage 2
    Given one security "WSB" and two users "Diamond" and "Paper" exist
    When user "Diamond" puts a buy order for security "WSB" with a price of 99 and quantity of 50
    And user "Paper" puts a sell order for security "WSB" with a price of 100 and a quantity of 100
    Then a trade occurs with the price of 0 and quantity of 0

  Scenario: no buy order price coverage 2
    Given one security "WSB" and two users "Diamond" and "Paper" exist
    When user "Diamond" puts a buy order for security "WSB" with a price of 99 and quantity of 50
    And user "Paper" puts a sell order for security "WSB" with a price of 100 and a quantity of 100
    Then a trade occurs with the price of 0 and quantity of 0
