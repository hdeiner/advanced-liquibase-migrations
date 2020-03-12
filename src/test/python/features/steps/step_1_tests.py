import sys, os, time, pymysql
from behave import *
from hamcrest import *

@given(u'the database has no IMDB_Schema')
def step_the_database_has_no_IMDB_Schema(context):
    pass

@then(u'the "{column_to_test}" column in the {table_to_test} table should be')
def step_check_column_in_table(context, column_to_test, table_to_test):
    try:
        connection = pymysql.connect(host='localhost', user='root', password='password', database='zipster', connect_timeout=5)
        query = 'SELECT ' + column_to_test + ' FROM ' + table_to_test.strip('\"') + '\n'
        result = []
        with connection.cursor() as cursor:
            cursor.execute(query)
            result = cursor.fetchall()
            result_count = cursor.rowcount
            connection.close()
    except pymysql.MySQLError as e:
        print("ERROR: Unexpected error: MySQL error = " + e)

    results_expected = []
    result_count_expected = 0
    for row in context.table:
        results_expected.append([row["User"]])
        result_count_expected += 1

    assert_that(result_count, equal_to(result_count_expected), 'bad result count')

    for i in range(0, result_count_expected):
        results_received = [result[i][0]]
        assert_that(results_received, equal_to(results_expected[i]), 'in line '+str(i+1))

