Feature: Version 1 Database

  Scenario: Ensure that the V1_1 and V1_2 Liquibase migrations happened correctly
    Given the database is at version "V1_2"
    Then the "zipster" database schema should be
     |TABLE_NAME           |COLUMN_NAME      |ORDINAL_POSITION|DATA_TYPE|CHARACTER_MAXIMUM_LENGTH|
     |DATABASECHANGELOG    |ID               |1               |varchar  |255                     |
     |DATABASECHANGELOG    |AUTHOR           |2               |varchar  |255                     |
     |DATABASECHANGELOG    |FILENAME         |3               |varchar  |255                     |
     |DATABASECHANGELOG    |DATEEXECUTED     |4               |datetime |NULL                    |
     |DATABASECHANGELOG    |ORDEREXECUTED    |5               |int      |NULL                    |
     |DATABASECHANGELOG    |EXECTYPE         |6               |varchar  |10                      |
     |DATABASECHANGELOG    |MD5SUM           |7               |varchar  |35                      |
     |DATABASECHANGELOG    |DESCRIPTION      |8               |varchar  |255                     |
     |DATABASECHANGELOG    |COMMENTS         |9               |varchar  |255                     |
     |DATABASECHANGELOG    |TAG              |10              |varchar  |255                     |
     |DATABASECHANGELOG    |LIQUIBASE        |11              |varchar  |20                      |
     |DATABASECHANGELOG    |CONTEXTS         |12              |varchar  |255                     |
     |DATABASECHANGELOG    |LABELS           |13              |varchar  |255                     |
     |DATABASECHANGELOG    |DEPLOYMENT_ID    |14              |varchar  |10                      |
     |DATABASECHANGELOGLOCK|ID               |1               |int      |NULL                    |
     |DATABASECHANGELOGLOCK|LOCKED           |2               |bit      |NULL                    |
     |DATABASECHANGELOGLOCK|LOCKGRANTED      |3               |datetime |NULL                    |
     |DATABASECHANGELOGLOCK|LOCKEDBY         |4               |varchar  |255                     |
     |NAME                 |NCONST           |1               |varchar  |31                      |
     |NAME                 |PRIMARY_NAME     |2               |varchar  |255                     |
     |NAME                 |BIRTH_YEAR       |3               |int      |NULL                    |
     |NAME                 |DEATH_YEAR       |4               |int      |NULL                    |
     |NAME_PROFESSION      |NCONST           |1               |varchar  |31                      |
     |NAME_PROFESSION      |PROFESSION       |2               |varchar  |31                      |
     |NAME_TITLE           |NCONST           |1               |varchar  |31                      |
     |NAME_TITLE           |TCONST           |2               |varchar  |31                      |
     |TITLE                |TCONST           |1               |varchar  |31                      |
     |TITLE                |TITLE_TYPE       |2               |varchar  |31                      |
     |TITLE                |PRIMARY_TITLE    |3               |varchar  |1023                    |
     |TITLE                |ORIGINAL_TITLE   |4               |varchar  |1023                    |
     |TITLE                |IS_ADULT         |5               |bit      |NULL                    |
     |TITLE                |START_YEAR       |6               |int      |NULL                    |
     |TITLE                |END_YEAR         |7               |int      |NULL                    |
     |TITLE                |RUNTIME_MINUTES  |8               |int      |NULL                    |
     |TITLE_AKA            |TCONST           |1               |varchar  |31                      |
     |TITLE_AKA            |ORDERING         |2               |int      |NULL                    |
     |TITLE_AKA            |TITLE            |3               |varchar  |1023                    |
     |TITLE_AKA            |REGION           |4               |varchar  |15                      |
     |TITLE_AKA            |LANGUAGE         |5               |varchar  |63                      |
     |TITLE_AKA            |TYPES            |6               |varchar  |63                      |
     |TITLE_AKA            |ATTRIBUTES       |7               |varchar  |63                      |
     |TITLE_AKA            |IS_ORIGINAL_TITLE|8               |bit      |NULL                    |
     |TITLE_DIRECTOR       |TCONST           |1               |varchar  |31                      |
     |TITLE_DIRECTOR       |NCONST           |2               |varchar  |31                      |
     |TITLE_EPISODE        |TCONST           |1               |varchar  |31                      |
     |TITLE_EPISODE        |TCONST_PARENT    |2               |varchar  |31                      |
     |TITLE_EPISODE        |SEASON_NUMBER    |3               |int      |NULL                    |
     |TITLE_EPISODE        |EPISODE_NUMBER   |4               |int      |NULL                    |
     |TITLE_GENRE          |TCONST           |1               |varchar  |31                      |
     |TITLE_GENRE          |GENRE            |2               |varchar  |31                      |
     |TITLE_PRINCIPALS     |TCONST           |1               |varchar  |31                      |
     |TITLE_PRINCIPALS     |ORDERING         |2               |int      |NULL                    |
     |TITLE_PRINCIPALS     |NCONST           |3               |varchar  |31                      |
     |TITLE_PRINCIPALS     |CATEGORY         |4               |varchar  |63                      |
     |TITLE_PRINCIPALS     |JOB              |5               |varchar  |255                     |
     |TITLE_PRINCIPALS     |CHARACTER_PLAYED |6               |varchar  |255                     |
     |TITLE_RATING         |TCONST           |1               |varchar  |31                      |
     |TITLE_RATING         |AVERAGE_RATING   |2               |varchar  |15                      |
     |TITLE_RATING         |NUMBER_OF_VOTES  |3               |int      |NULL                    |
     |TITLE_WRITER         |TCONST           |1               |varchar  |31                      |
     |TITLE_WRITER         |NCONST           |2               |varchar  |31                      |
   And the following tables have the following row counts:
     |TABLE_NAME      |ROW_COUNT|
     |NAME            |     6234|
     |NAME_PROFESSION |    16300|
     |NAME_TITLE      |    24389|
     |TITLE           |     3735|
     |TITLE_AKA       |     2572|
     |TITLE_DIRECTOR  |     3620|
     |TITLE_EPISODE   |     2540|
     |TITLE_GENRE     |     7228|
     |TITLE_PRINCIPALS|    21255|
     |TITLE_RATING    |      612|
     |TITLE_WRITER    |     2449|
