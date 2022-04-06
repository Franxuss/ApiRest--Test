Feature: Testing Rick and Morty REST API

  Background:
    Given an open API connection

  Scenario: Number of pages
    When we get the characters
    Then the number of pages is correct

  Scenario: Next and previous page
    When we get a certain page of characters
    Then the next and previous page are correct

  Scenario: Invalid page
    When we get an invalid page an error is returned