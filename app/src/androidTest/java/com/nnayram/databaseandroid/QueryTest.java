package com.nnayram.databaseandroid;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.test.AndroidTestCase;

import com.nnayram.databaseandroid.db.SchemaHelper;

import static com.nnayram.databaseandroid.db.DBContract.ClassTable;
import static com.nnayram.databaseandroid.db.DBContract.CourseTable;
import static com.nnayram.databaseandroid.db.DBContract.StudentTable;

/**
 * Created by Rufo on 11/5/2016.
 */
public class QueryTest extends AndroidTestCase {

    private SchemaHelper helper;

    private long student1;
    private long student2;
    private long student3;
    private long student4;
    private long student5;
    private long course1;
    private long course2;
    private long course3;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        helper = new SchemaHelper(mContext);
        initStudentAndCourse();
        /**
         * SELECT query
         * SELECT COLUMNS query
         * WHERE FILTER - Filter by state
         * AND/OR Clauses
         * DISTINCT
         * LIMIT
         * ORDER BY clauses
         * GROUP BY clauses
         * HAVING filters
         * SQL Functions
         * JOINS
         */
    }

    private void initStudentAndCourse() {
        // add students
        student1 = helper.addStudent("Jason", "IL", 12);
        student2 = helper.addStudent("Du", "AR", 12);
        student3 = helper.addStudent("George", "CA", 11);
        student4 = helper.addStudent("Mark", "CA", 11);
        student5 = helper.addStudent("Bobby", "IL", 12);
        // add courses
        course1 = helper.addCourse("Math51");
        course2 = helper.addCourse("CS106A");
        course3 = helper.addCourse("Econ1A");
        // enroll student in class
        helper.enrollStudentClass((int) student1, (int) course1);
        helper.enrollStudentClass((int) student1, (int) course2);
        helper.enrollStudentClass((int) student2, (int) course2);
        helper.enrollStudentClass((int) student3, (int) course1);
        helper.enrollStudentClass((int) student3, (int) course2);
        helper.enrollStudentClass((int) student4, (int) course3);
        helper.enrollStudentClass((int) student5, (int) course2);
    }

    /*
    SELECT query
     */
    public void testSelectQuery() {
        SQLiteDatabase sqlDb = helper.getReadableDatabase();

        println("METHOD 1");
        // Method #1 : SQLiteDatabase rawquery()
        Cursor c = sqlDb.rawQuery("SELECT * FROM " + StudentTable.TABLE_NAME, null);
        while(c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(StudentTable.COLUMN_NAME));
            println("GOT STUDENT " + name);
        }

        println("METHOD 2");
        // Method #2 : SQLiteDatabase query()
        c = sqlDb.query(StudentTable.TABLE_NAME, null, null, null, null, null, null);
        while(c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(StudentTable.COLUMN_NAME));
            println("GOT STUDENT " + name);
        }

        println("METHOD 3");
        // Method #3 : SQLiteQueryBuilder
        String query = SQLiteQueryBuilder.buildQueryString(false, StudentTable.TABLE_NAME, null, null, null, null, null, null);
        println(query);
        c = sqlDb.rawQuery(query, null);
        while(c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(StudentTable.COLUMN_NAME));
            println("GOT STUDENT " + name);
        }
    }

    /*
    SELECT COLUMNS query
     */
    public void testSelectColumnsQuery() {
        SQLiteDatabase sqlDb = helper.getReadableDatabase();

        println("METHOD 1");
        // Method #1 : SQLiteDatabase rawquery()
        Cursor c = sqlDb.rawQuery("SELECT " + StudentTable.COLUMN_NAME + "," + StudentTable.COLUMN_STATE + " from " + StudentTable.TABLE_NAME, null);
        while(c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(StudentTable.COLUMN_NAME));
            String state = c.getString(c.getColumnIndex(StudentTable.COLUMN_STATE));
            println("GOT STUDENT " + name + " from " + state);
        }

        println("METHOD 2");
        // Method #2 : SQLiteDatabase query()
        String[] cols = new String[]{StudentTable.COLUMN_NAME, StudentTable.COLUMN_STATE};
        c = sqlDb.query(StudentTable.TABLE_NAME, cols, null, null, null, null, null);
        while(c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(StudentTable.COLUMN_NAME));
            String state = c.getString(c.getColumnIndex(StudentTable.COLUMN_STATE));
            println("GOT STUDENT " + name + " from " + state);
        }

        println("METHOD 3");
        // Method #3 : SQLiteQueryBuilder
        String query = SQLiteQueryBuilder.buildQueryString(false, StudentTable.TABLE_NAME, cols, null, null, null, null, null);
        println(query);
        c = sqlDb.rawQuery(query, null);
        while(c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(StudentTable.COLUMN_NAME));
            String state = c.getString(c.getColumnIndex(StudentTable.COLUMN_STATE));
            println("GOT STUDENT " + name + " from " + state);
        }
    }

    /*
    WHERE FILTER - Filter by state
     */
    public void testWhereFilter() {
        SQLiteDatabase sqlDb = helper.getReadableDatabase();

        println("METHOD 1");
        // Method #1 : SQLiteDatabase rawquery()
        Cursor c = sqlDb.rawQuery("SELECT * FROM " + StudentTable.TABLE_NAME + " WHERE " + StudentTable.COLUMN_STATE + "=?", new String[]{"IL"});
        while(c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(StudentTable.COLUMN_NAME));
            String state = c.getString(c.getColumnIndex(StudentTable.COLUMN_STATE));
            println("GOT STUDENT " + name + " from " + state);
        }

        println("METHOD 2");
        // Method #2 : SQLiteDatabase query()
        String selection = StudentTable.COLUMN_STATE + "=?";
        String[] selectionArgs = new String[]{"IL"};
        c = sqlDb.query(StudentTable.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        while(c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(StudentTable.COLUMN_NAME));
            String state = c.getString(c.getColumnIndex(StudentTable.COLUMN_STATE));
            println("GOT STUDENT " + name + " from " + state);
        }

        println("METHOD 3");
        // Method #3 : SQLiteQueryBuilder
        String query = SQLiteQueryBuilder.buildQueryString(false, StudentTable.TABLE_NAME, null, StudentTable.COLUMN_STATE + "='IL'", null, null, null, null);
        println(query);
        c = sqlDb.rawQuery(query, null);
        while(c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(StudentTable.COLUMN_NAME));
            String state = c.getString(c.getColumnIndex(StudentTable.COLUMN_STATE));
            println("GOT STUDENT " + name + " from " + state);
        }
    }

    /*
    AND/OR Clauses
     */
    public void testAndOrClause() {
        SQLiteDatabase sqlDb = helper.getReadableDatabase();

        println("METHOD 1");
        // Method #1 : SQLiteDatabase rawquery()
        Cursor c = sqlDb.rawQuery("SELECT * FROM " + StudentTable.TABLE_NAME + " WHERE "
                + StudentTable.COLUMN_STATE + "=? OR " + StudentTable.COLUMN_STATE + "=?",
                new String[]{"IL", "AR"});
        while(c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(StudentTable.COLUMN_NAME));
            String state = c.getString(c.getColumnIndex(StudentTable.COLUMN_STATE));
            println("GOT STUDENT " + name + " from " + state);
        }

        println("METHOD 2");
        // Method #2 : SQLiteDatabase query()
        String selection = StudentTable.COLUMN_STATE + "=? OR " + StudentTable.COLUMN_STATE + "=?";
        String[] selectionArgs = new String[]{"IL", "AR"};
        c = sqlDb.query(StudentTable.TABLE_NAME, null, selection, selectionArgs, null, null, null, null);
        while(c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(StudentTable.COLUMN_NAME));
            String state = c.getString(c.getColumnIndex(StudentTable.COLUMN_STATE));
            println("GOT STUDENT " + name + " from " + state);
        }

        println("METHOD 3");
        // Method #3 : SQLiteQueryBuilder
        String query =SQLiteQueryBuilder.buildQueryString(false, StudentTable.TABLE_NAME, null,
                StudentTable.COLUMN_STATE + "='IL' OR " + StudentTable.COLUMN_STATE + "='AR'", null, null, null, null);
        println(query);
        c = sqlDb.rawQuery(query, null);
        while(c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(StudentTable.COLUMN_NAME));
            String state = c.getString(c.getColumnIndex(StudentTable.COLUMN_STATE));
            println("GOT STUDENT " + name + " from " + state);
        }
    }

    /*
    DISTINCT
     */
    public void testDistinct() {
        SQLiteDatabase sqlDb = helper.getReadableDatabase();

        println("METHOD 1");
        // Method #1 : SQLiteDatabase rawquery()
        Cursor c = sqlDb.rawQuery("SELECT DISTINCT " + StudentTable.COLUMN_STATE + " FROM " + StudentTable.TABLE_NAME, null);
        while(c.moveToNext()) {
            String state = c.getString(c.getColumnIndex(StudentTable.COLUMN_STATE));
            println("GOT DISTINCT STATE " + state);
        }

        println("METHOD 2");
        // Method #2 : SQLiteDatabase query()
        String[] col = new String[]{StudentTable.COLUMN_STATE};
        c = sqlDb.query(true, StudentTable.TABLE_NAME, col, null, null, null, null, null, null);
        while(c.moveToNext()) {
            String state = c.getString(c.getColumnIndex(StudentTable.COLUMN_STATE));
            println("GOT DISTINCT STATE " + state);
        }

        println("METHOD 3");
        // Method #3 : SQLiteQueryBuilder
        String query = SQLiteQueryBuilder.buildQueryString(true, StudentTable.TABLE_NAME, new String[]{StudentTable.COLUMN_STATE}, null, null, null, null, null);
        println(query);
        c = sqlDb.rawQuery(query, null);
        while(c.moveToNext()) {
            String state = c.getString(c.getColumnIndex(StudentTable.COLUMN_STATE));
            println("GOT DISTINCT STATE " + state);
        }

    }

    /*
    LIMIT
     */
    public void testLimit() {
        SQLiteDatabase sqlDb = helper.getReadableDatabase();

        println("METHOD 1");
        // Method #1 : SQLiteDatabase rawquery()
        Cursor c = sqlDb.rawQuery("SELECT * FROM " + StudentTable.TABLE_NAME + " LIMIT 0,3", null);
        while(c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(StudentTable.COLUMN_NAME));
            println("GOT STUDENT " + name);
        }

        println("METHOD 2");
        // Method #2 : SQLiteDatabase query()
        c = sqlDb.query(StudentTable.TABLE_NAME, null, null, null, null, null, null, "3");
        while(c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(StudentTable.COLUMN_NAME));
            println("GOT STUDENT " + name);
        }

        println("METHOD 3");
        // Method #3 : SQLiteQueryBuilder
        String query = SQLiteQueryBuilder.buildQueryString(false, StudentTable.TABLE_NAME, null, null, null, null, null, "3");
        println(query);
        c = sqlDb.rawQuery(query, null);
        while(c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(StudentTable.COLUMN_NAME));
            println("GOT STUDENT " + name);
        }
    }

    /*
    ORDER BY clauses
     */
    public void testOrderBy() {
        SQLiteDatabase sqlDb = helper.getReadableDatabase();

        println("METHOD 1");
        // Method #1 : SQLiteDatabase rawquery()
        Cursor c = sqlDb.rawQuery("SELECT * FROM " + StudentTable.TABLE_NAME + " ORDER BY " + StudentTable.COLUMN_STATE + " ASC", null);
        while (c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(StudentTable.COLUMN_NAME));
            String state = c.getString(c.getColumnIndex(StudentTable.COLUMN_STATE));
            println("GOT STUDENT " + name + " from " + state);
        }

        println("METHOD 2");
        // Method #2 : SQLiteDatabase query()
        String orderBy = StudentTable.COLUMN_STATE + " ASC";
        c = sqlDb.query(StudentTable.TABLE_NAME, null, null, null, null, null, orderBy);
        while (c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(StudentTable.COLUMN_NAME));
            String state = c.getString(c.getColumnIndex(StudentTable.COLUMN_STATE));
            println("GOT STUDENT " + name + " from " + state);
        }

        println("METHOD 3");
        // Method #3 : SQLiteQueryBuilder
        String query = SQLiteQueryBuilder.buildQueryString(false, StudentTable.TABLE_NAME, null, null, null, null, StudentTable.COLUMN_STATE + " ASC", null);
        println(query);
        c = sqlDb.rawQuery(query, null);
        while (c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(StudentTable.COLUMN_NAME));
            String state = c.getString(c.getColumnIndex(StudentTable.COLUMN_STATE));
            println("GOT STUDENT " + name + " from " + state);
        }
    }

    /*
    GROUP BY clauses
     */
    public void testGroupBy() {
        SQLiteDatabase sqlDb = helper.getReadableDatabase();

        println("METHOD 1");
        // Method #1 : SQLiteDatabase rawquery()
        String colCountName = "COUNT(" + StudentTable.COLUMN_STATE + ")";
        Cursor c = sqlDb.rawQuery("SELECT " + StudentTable.COLUMN_STATE + "," + colCountName + " FROM " + StudentTable.TABLE_NAME + " GROUP BY " + StudentTable.COLUMN_STATE, null);
        while (c.moveToNext()) {
            String state = c.getString(c.getColumnIndex(StudentTable.COLUMN_STATE));
            int count = c.getInt(c.getColumnIndex(colCountName));
            println("STATE " + state + " HAS COUNT " + count);
        }

        println("METHOD 2");
        // Method #2 : SQLiteDatabase query()
        String[] cols = new String[]{StudentTable.COLUMN_STATE, colCountName};
        String groupBy = StudentTable.COLUMN_STATE;
        c = sqlDb.query(StudentTable.TABLE_NAME, cols, null, null, groupBy, null, null);
        while (c.moveToNext()) {
            String state = c.getString(c.getColumnIndex(StudentTable.COLUMN_STATE));
            int count = c.getInt(c.getColumnIndex(colCountName));
            println("STATE " + state + " HAS COUNT " + count);
        }

        println("METHOD 3");
        // Method #3 : SQLiteQueryBuilder
        String query = SQLiteQueryBuilder.buildQueryString(false, StudentTable.TABLE_NAME, cols, null, groupBy, null, null, null);
        println(query);
        c = sqlDb.rawQuery(query, null);
        while (c.moveToNext()) {
            String state = c.getString(c.getColumnIndex(StudentTable.COLUMN_STATE));
            int count = c.getInt(c.getColumnIndex(colCountName));
            println("STATE " + state + " HAS COUNT " + count);
        }
    }

    /*
    HAVING filters - The HAVING filter is to be used only with a GROUPBY clause
     */
    public void testHaving() {
        SQLiteDatabase sqlDb = helper.getReadableDatabase();

        println("METHOD 1");
        // Method #1 : SQLiteDatabase rawquery()
        String colCountName = "COUNT(" + StudentTable.COLUMN_STATE + ")";
        Cursor c = sqlDb.rawQuery("SELECT " + StudentTable.COLUMN_STATE + "," + colCountName + " FROM " + StudentTable.TABLE_NAME
                + " GROUP BY " + StudentTable.COLUMN_STATE + " HAVING " + colCountName + " > 1", null);
        while (c.moveToNext()) {
            String state = c.getString(c.getColumnIndex(StudentTable.COLUMN_STATE));
            int count = c.getInt(c.getColumnIndex(colCountName));
            println("STATE " + state + " HAS COUNT " + count);
        }

        println("METHOD 2");
        // Method #2 : SQLiteDatabase query()
        String[] cols = new String[]{StudentTable.COLUMN_STATE, colCountName};
        String groupBy = StudentTable.COLUMN_STATE;
        String having = /*colCountName + " > 1"*/ StudentTable.COLUMN_STATE + "='IL'";
        c = sqlDb.query(StudentTable.TABLE_NAME, cols, null, null, groupBy, having, null);
        while (c.moveToNext()) {
            String state = c.getString(c.getColumnIndex(StudentTable.COLUMN_STATE));
            int count = c.getInt(c.getColumnIndex(colCountName));
            println("STATE " + state + " HAS COUNT " + count);
        }

        println("METHOD 3");
        // Method #3 : SQLiteQueryBuilder
        String query = SQLiteQueryBuilder.buildQueryString(false, StudentTable.TABLE_NAME, cols, null, groupBy, having, null, null);
        println(query);
        c = sqlDb.rawQuery(query, null);
        while (c.moveToNext()) {
            String state = c.getString(c.getColumnIndex(StudentTable.COLUMN_STATE));
            int count = c.getInt(c.getColumnIndex(colCountName));
            println("STATE " + state + " HAS COUNT " + count);
        }
    }

    /*
    SQL Functions - MIN/MAX/AVG/UPPER/LOWER/SUBSTR
     */
    public void testSqlFunctionsOne() {
        SQLiteDatabase sqlDb = helper.getReadableDatabase();

        String colMinName = "MIN(" + StudentTable.COLUMN_GRADE + ")";
        String colMaxName = "MAX(" + StudentTable.COLUMN_GRADE + ")";
        String colAvgName = "AVG(" + StudentTable.COLUMN_GRADE + ")";

        println("METHOD 1");
        // Method #1 : SQLiteDatabase rawquery()
        String colName = colMaxName;
        Cursor c = sqlDb.rawQuery("SELECT " + colName + " FROM " + StudentTable.TABLE_NAME, null);
        while (c.moveToNext()) {
            int result = c.getInt(c.getColumnIndex(colName));
            println("Result: " + result);
        }

        println("METHOD 2");
        // Method #2 : SQLiteDatabase query()
        String[] cols = new String[]{colName};
        c = sqlDb.query(StudentTable.TABLE_NAME, cols, null, null, null, null, null);
        while (c.moveToNext()) {
            int result = c.getInt(c.getColumnIndex(colName));
            println("Result: " + result);
        }

        println("METHOD 3");
        // Method #3 : SQLiteQueryBuilder
        colName = colAvgName;
        cols = new String[] {colName};
        String query = SQLiteQueryBuilder.buildQueryString(false, StudentTable.TABLE_NAME, cols, null, null, null, null, null);
        println(query);
        c = sqlDb.rawQuery(query, null);
        while (c.moveToNext()) {
            double result = c.getDouble(c.getColumnIndex(colName));
            println("Result: " + result);
        }

        String colUpperName = "UPPER(" + StudentTable.COLUMN_NAME + ")";
        String colLowerName = "LOWER(" + StudentTable.COLUMN_NAME + ")";
        String colSubstrName = "SUBSTR(" + StudentTable.COLUMN_NAME + ",1,2)";

        println("METHOD 1");
        // Method #1 : SQLiteDatabase rawquery()
        colName = colSubstrName;
        cols = new String[]{colName};
        c = sqlDb.rawQuery("SELECT " + colName + " FROM " + StudentTable.TABLE_NAME, null);
        while (c.moveToNext()) {
            String result = c.getString(c.getColumnIndex(colName));
            println("Result: " + result);
        }

        println("METHOD 2");
        // Method #2 : SQLiteDatabase query()
        c = sqlDb.query(StudentTable.TABLE_NAME, cols, null, null, null, null, null);
        while (c.moveToNext()) {
            String result = c.getString(c.getColumnIndex(colName));
            println("Result: " + result);
        }

        println("METHOD 3");
        // Method #3 : SQLiteQueryBuilder
        query = SQLiteQueryBuilder.buildQueryString(false, StudentTable.TABLE_NAME, cols, null, null, null, null, null);
        println(query);
        while (c.moveToNext()) {
            String result = c.getString(c.getColumnIndex(colName));
            println("Result: " + result);
        }
    }

    /*
    JOINS
     */
    public void testJoins() {
        SQLiteDatabase sqlDb = helper.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        String courseIdCol = CourseTable.TABLE_NAME + "." + CourseTable._ID;
        String classCourseIdCol = ClassTable.TABLE_NAME + "." + ClassTable.COLUMN_COURSE;
        String classIdCol = ClassTable.TABLE_NAME + "." + ClassTable._ID;
        String[] cols = new String[]{classIdCol, ClassTable.COLUMN_COURSE, CourseTable.COLUMN_NAME};

        builder.setTables(ClassTable.TABLE_NAME + " INNER JOIN " + CourseTable.TABLE_NAME + " ON (" + classCourseIdCol  + "=" + courseIdCol +")");

        String query = builder.buildQuery(cols, null, null, null, null, null);
        println(query);
        Cursor c = sqlDb.rawQuery(query, null);
        while (c.moveToNext()) {
            long rowId = c.getLong(c.getColumnIndex(ClassTable._ID));
            long courseId = c.getLong(c.getColumnIndex(ClassTable.COLUMN_COURSE));
            String courseName = c.getString(c.getColumnIndex(CourseTable.COLUMN_NAME));
            println(rowId + "|| COURSE ID " + courseId + " " + courseName);
        }
    }

    private void println(String message) {
        System.out.println(getClass() + " : " + message);
    }
}
