package com.nnayram.databaseandroid.db;

import android.provider.BaseColumns;

/**
 * Created by Rufo on 11/5/2016.
 */
public class DBContract {
    private DBContract() {}

    public static abstract class StudentTable implements BaseColumns {
        public static final String TABLE_NAME = "students";
        public static final String COLUMN_NAME = "student_name";
        public static final String COLUMN_STATE = "state";
        public static final String COLUMN_GRADE = "grade";
    }

    public static abstract class CourseTable implements BaseColumns {
        public static final String TABLE_NAME = "courses";
        public static final String COLUMN_NAME = "course_name";
    }

    public static abstract class ClassTable implements  BaseColumns {
        public static final String TABLE_NAME = "classes";
        public static final String COLUMN_STUDENT = "students_id";
        public static final String COLUMN_COURSE = "courses_id";
    }

}
