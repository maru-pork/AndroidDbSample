package com.nnayram.databaseandroid;

import android.database.Cursor;
import android.test.AndroidTestCase;

import com.nnayram.databaseandroid.db.SchemaHelper;

import java.util.Set;

import static com.nnayram.databaseandroid.db.DBContract.ClassTable;

/**
 * Created by Rufo on 11/5/2016.
 */
public class CRUDTest extends AndroidTestCase {

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
        setUpStudentAndCourse();
    }

    private void setUpStudentAndCourse() {
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

    public void testAdd() {
        // get students for course
        int course = (int) course2;
        Cursor c = helper.getStudentsForCourse(course);
        while(c.moveToNext()) {
            long studentId = c.getLong(c.getColumnIndex(ClassTable.COLUMN_STUDENT));
            System.out.println("STUDENT " + studentId + " is enrolled in course " + course);
        }

        // get students for course and filter by grade
        int grade = 11;
        Set<Long> studentIds = helper.getStudentsByGradeForCourse(course, grade);
        for (Long id : studentIds) {
            System.out.println("STUDENT " + id + " of grade " + grade + " is enrolled in course" + course);
        }
    }

    public void testRemove() {
        int sid1 = (int) student1;
        Cursor c = helper.getCoursesForStudent(sid1);
        while(c.moveToNext()) {
            long courseId = c.getLong(c.getColumnIndex(ClassTable.COLUMN_COURSE)); // returns course1 and course2
            System.out.println("STUDENT " + sid1 + " is enrolled in course " + courseId);
        }

        // try removing a course
        int cid1 = (int) course1;
        System.out.println("----------------Removing course " + cid1 + "----------------");
        helper.removeCourse(cid1);

        c = helper.getCoursesForStudent(sid1);
        while(c.moveToNext()) {
            long courseId = c.getLong(c.getColumnIndex(ClassTable.COLUMN_COURSE)); // should return course2 only
            System.out.println("STUDENT " + sid1 + " is enrolled in course " + courseId);
        }
    }
}
