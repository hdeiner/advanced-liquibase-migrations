#!/usr/bin/env bash

figlet -w 200 -f standard "Liquibase migrate to V1 (create initial database)"

mvn compile

cd src/main/java/common/data
./runAt1PerCent.sh
cd -

figlet -w 160 -f small "Liquibase V1_1 (initial schema)"
cd src/main/resources/db/migration
python3 manage_dbchangelog.py -i dbchangelog.xml -v V1_1
cd -
mvn liquibase:update

figlet -w 160 -f small "Liquibase V1_2 (initial static data)"
cd src/main/resources/db/migration
python3 manage_dbchangelog.py -i dbchangelog.xml -v V1_2
cd -
mvn liquibase:update

cd src/test/python
behave -v features/step_2_tests.feature
cd -