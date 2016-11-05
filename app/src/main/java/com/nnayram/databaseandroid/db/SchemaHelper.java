package com.nnayram.databaseandroid.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

import static com.nnayram.databaseandroid.db.DBContract.ClassTable;
import static com.nnayram.databaseandroid.db.DBContract.CourseTable;
import static com.nnayram.databaseandroid.db.DBContract.StudentTable;

/**
 * Created by Rufo on 11/5/2016.
 */
public class SchemaHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "adv_data.db";
    private static final int DATABASE_VERSION = 1;

    // Use the in-memory database for testing by setting the second parameter to null
    // See http://bit.ly/2fmk64S
    public SchemaHelper(Context context) {
        super(context, null, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create student table
        db.execSQL("CREATE TABLE " + StudentTable.TABLE_NAME + " ("
                + StudentTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + StudentTable.COLUMN_NAME + " TEXT,"
                + StudentTable.COLUMN_STATE + " TEXT,"
                + StudentTable.COLUMN_GRADE + " INTEGER);"
        );
        // create course table
        db.execSQL("CREATE TABLE " + CourseTable.TABLE_NAME + " ("
                + CourseTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CourseTable.COLUMN_NAME + " TEXT);"
        );
        // create class table
        db.execSQL("CREATE TABLE " + ClassTable.TABLE_NAME + " ("
                + ClassTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ClassTable.COLUMN_STUDENT + " INTEGER,"
                + ClassTable.COLUMN_COURSE + " INTEGER);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("LOG_TAG", "Updating database from version " + oldVersion  + " to " + newVersion + " which will destroy all data.");
        // drop table if upgraded
        db.execSQL("DROP TABLE IF EXISTS " + StudentTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CourseTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ClassTable.TABLE_NAME);
        onCreate(db);
    }

    /*
    public long insert(String table, String nullColumnHack, ContentValues values)
     */

    // wrapper method for adding a student
    public long addStudent(String name, String state, int grade) {
        ContentValues cv = new ContentValues();
        cv.put(StudentTable.COLUMN_NAME, name);
        cv.put(StudentTable.COLUMN_STATE, state);
        cv.put(StudentTable.COLUMN_GRADE, grade);

        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(StudentTable.TABLE_NAME, StudentTable.COLUMN_NAME, cv);
        return result;
    }

    // wrapper method for adding a course
    public long addCourse(String name) {
        ContentValues cv = new ContentValues();
        cv.put(CourseTable.COLUMN_NAME, name);

        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(CourseTable.TABLE_NAME, CourseTable.COLUMN_NAME, cv);
        return result;
    }

    // wrapper method for enrolling a student into a course
    public boolean enrollStudentClass(int studentId, int courseId) {
        ContentValues cv = new ContentValues();
        cv.put(ClassTable.COLUMN_STUDENT, studentId);
        cv.put(ClassTable.COLUMN_COURSE, courseId);

        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(ClassTable.TABLE_NAME, ClassTable.COLUMN_STUDENT, cv);
        return (result >= 0);
    }

    /*
    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
    public Cursor query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
     */

    // get all students in a course
    public Cursor getStudentsForCourse(int courseId) {
        SQLiteDatabase db = this.getReadableDatabase();

        // we only need to return student ids
        String[] cols = new String[]{ClassTable.COLUMN_STUDENT};
        String[] selectionArgs = new String[] {String.valueOf(courseId)};

        // query class map for students in course
        Cursor c = db.query(ClassTable.TABLE_NAME, cols, ClassTable.COLUMN_COURSE+"=?", selectionArgs, null, null, null);
        return c;
    }

    // get all courses for given student
    public Cursor getCoursesForStudent(int studentId) {
        SQLiteDatabase db = this.getReadableDatabase();

        // we only need to return course ids
        String[] cols = new String[] {ClassTable.COLUMN_COURSE};
        String[] selectionArgs = new String[]{String.valueOf(studentId)};

        Cursor c = db.query(ClassTable.TABLE_NAME, cols, ClassTable.COLUMN_STUDENT+"=?", selectionArgs, null, null, null);
        return c;
    }

    public Set<Long> getStudentsByGradeForCourse(int courseId, int grade) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] cols = new String[]{ClassTable.COLUMN_STUDENT};
        String[] selectionArsg = new String[]{String.valueOf(courseId)};
        Cursor c = db.query(ClassTable.TABLE_NAME, cols, ClassTable.COLUMN_COURSE+"=?", selectionArsg, null, null, null);
        Set<Long> returnIds = new HashSet<>();
        while (c.moveToNext()) {
            long id = c.getLong(c.getColumnIndex(ClassTable.COLUMN_STUDENT));
            returnIds.add(id);
        }

        cols = new String[]{StudentTable._ID};
        selectionArsg = new String[]{String.valueOf(grade)};
        c = db.query(StudentTable.TABLE_NAME, cols, StudentTable.COLUMN_GRADE+"=?", selectionArsg, null, null, null);
        Set<Long> gradeIds = new HashSet<>();
        while (c.moveToNext()) {
            long id = c.getLong(c.getColumnIndex(StudentTable._ID));
            gradeIds.add(id);
        }

        returnIds.retainAll(gradeIds);
        return  returnIds;
    }

    /*
    public int delete(String table, String whereClause, String[] whereArgs)
     */

    public boolean removeStudent(int studentId) {
        SQLiteDatabase db = this.getWritableDatabase();

        String[] whereArgs = new String[]{String.valueOf(studentId)};
        int result = db.delete(ClassTable.TABLE_NAME, ClassTable.COLUMN_STUDENT+"=?", whereArgs);
        return (result > 0);
    }

    public boolean removeCourse(int courseId) {
        SQLiteDatabase db = this.getWritableDatabase();

        String[] whereArgs = new String[]{String.valueOf(courseId)};

        // remove course from all students enrolled
        db.delete(ClassTable.TABLE_NAME, ClassTable.COLUMN_COURSE+"=?", whereArgs);
        // then delete course
        int result = db.delete(CourseTable.TABLE_NAME, CourseTable._ID+"=?", whereArgs);
        return (result > 0);

    }
}
