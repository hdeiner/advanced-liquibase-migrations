Feature: Start MySQL

  Scenario: Ensure that proper users are in database to allow Liquibase migrations
    Given the database has no IMDB_Schema
    Then the "User" column in the "mysql.user" table should be
      | User          |
      | root          |
      | user          |
      | mysql.session |
      | mysql.sys     |
      | root          |