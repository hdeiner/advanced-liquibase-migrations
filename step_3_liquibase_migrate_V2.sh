#!/usr/bin/env bash

figlet -w 200 -f standard "Liquibase migrate to V2 (split name)"

figlet -w 160 -f small "Liquibase V2_1 (add first and last name)"
cd src/main/resources/db/migration
python3 manage_dbchangelog.py -i dbchangelog.xml -v V2_1
cd -
mvn liquibase:update

figlet -w 160 -f small "Liquibase V2_2 (split primary_name into first and last name)"
cd src/main/resources/db/migration
python3 manage_dbchangelog.py -i dbchangelog.xml -v V2_2
cd -
mvn liquibase:update

figlet -w 160 -f small "Liquibase V2_3 (remove primary_name)"
cd src/main/resources/db/migration
python3 manage_dbchangelog.py -i dbchangelog.xml -v V2_3
cd -
mvn liquibase:update
mvn liquibase:dbDoc

cd src/test/python
behave -v features/step_3_tests.feature
cd -