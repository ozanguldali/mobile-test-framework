@deneme
Feature: Deneme Feature

  Scenario: 0 - android test init scenario

    Given I use following capabilities:
      |platformName|Android|
      |platformVersion|9|
      |browserName|Chrome|
      |deviceName|127.0.0.1:5554|

#    Given I start appium server for:
#      |host|127.0.0.1|
#      |port|4723     |
#      |platformName|Android|
#      |platformVersion|9|
#      |browserName|Chrome|
#      |deviceName|127.0.0.1:5554|

    And I wait for 2 seconds

    Given I use chrome driver

    Then I open amazon page

    And I close appium server