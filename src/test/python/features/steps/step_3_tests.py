import sys, os, time, pymysql
from behave import *
from hamcrest import *

@then(u'the NAME table has the following sample results')
def step_check_table_row_counts(context):
    results_expected = []
    result_count_expected = 0
    for row in context.table:
        results_expected.append([row["NCONST"],row["FIRST_NAME"],row["LAST_NAME"],row["BIRTH_YEAR"],row["DEATH_YEAR"]])
        result_count_expected += 1

    try:
        connection = pymysql.connect(host='localhost', user='root', password='password', database='zipster', connect_timeout=5)
        for row in results_expected:
            query = 'select NCONST, FIRST_NAME, LAST_NAME, BIRTH_YEAR, DEATH_YEAR FROM NAME WHERE NCONST=\"' + row[0] + '\"\n'
            with connection.cursor() as cursor:
                cursor.execute(query)
                result = cursor.fetchall()

            assert_that(len(result), equal_to(int(1)), 'couldn\'t find NAME row for NCONST='+row[0])
            assert_that(result[0][0], equal_to(row[0]), 'NCONST mismatch')
            assert_that(result[0][1], equal_to(row[1]), 'FIRST_NAME mismatch')
            assert_that(result[0][2], equal_to(row[2]), 'LAST_NAME mismatch')
            assert_that(result[0][3], equal_to(int(row[3])), 'BIRTH_YEAR mismatch')
            assert_that(result[0][4], equal_to(int(row[4])), 'DEATH_YEAR mismatch')

        connection.close()

    except pymysql.MySQLError as e:
        print("ERROR: Unexpected error: MySQL error = " + str(e))
