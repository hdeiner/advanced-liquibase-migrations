import pymysql
from behave import *
from behave import *
from hamcrest import *

@given(u'the database is at version "{schema_version}"')
def step_check_schema_version(context, schema_version):
    pass

@then(u'the "zipster" database schema should be')
def step_check_database_schema(context):
    try:
        connection = pymysql.connect(host='localhost', user='root', password='password', database='zipster', connect_timeout=5)
        query = "select TABLE_NAME, COLUMN_NAME, ORDINAL_POSITION, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH from information_schema.columns where table_schema = 'zipster' order by table_name,ordinal_position\n"
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
        results_expected.append([row["TABLE_NAME"], row["COLUMN_NAME"], row["ORDINAL_POSITION"], row["DATA_TYPE"], row["CHARACTER_MAXIMUM_LENGTH"]])
        result_count_expected += 1

    assert_that(result_count, equal_to(result_count_expected), 'bad result count')

    for i in range(0, result_count_expected):
        results_received = [result[i][0], result[i][1], str(result[i][2]), result[i][3], str(result[i][4])]
        if str(results_received[4]) == 'None':
            results_received[4] = 'NULL'
        assert_that(results_received, equal_to(results_expected[i]), 'in line '+str(i+1))

@then(u'the following tables have the following row counts')
def step_check_table_row_counts(context):
    try:
        connection = pymysql.connect(host='localhost', user='root', password='password', database='zipster', connect_timeout=5)
        for row in context.table:
            table_name = row["TABLE_NAME"]
            row_count  = row["ROW_COUNT"]
            query = "select COUNT(*) from " + table_name +"\n"
            result = []
            with connection.cursor() as cursor:
                cursor.execute(query)
                result = cursor.fetchall()

            assert_that(result[0][0], equal_to(int(row_count)), 'bad row count in table '+table_name)

        connection.close()

    except pymysql.MySQLError as e:
        print("ERROR: Unexpected error: MySQL error = " + str(e))
