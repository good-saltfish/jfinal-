package com.example.controller.student;

import com.jfinal.core.Controller;
import com.example.model.Score;
import com.example.model.Student;
import com.jfinal.plugin.activerecord.Page;
import com.example.util.SemesterUtil;

public class StudentScoreController extends Controller {
    public void index() {
        Student student = getSessionAttr("student");
        student = Student.dao.findById(student.getId());
        setAttr("student", student);
        
        int pageNumber = getParaToInt("page", 1);
        String semester = getPara("semester");
        
        String select = "SELECT s.*, c.course_code as course_code, c.course_name as course_name, " +
                       "c.credit as course_credit, c.classroom as classroom";
        String from = "FROM score s " +
                     "INNER JOIN course c ON s.course_id = c.id " +
                     "WHERE s.student_id = ? ";
            
        if (semester != null && !semester.isEmpty()) {
            from += "AND s.semester = ? ORDER BY s.id DESC";
            setAttr("selectedSemester", semester);
            setAttr("scorePage", Score.dao.paginate(pageNumber, 10, select, from, student.getId(), semester));
        } else {
            from += "ORDER BY s.id DESC";
            setAttr("scorePage", Score.dao.paginate(pageNumber, 10, select, from, student.getId()));
        }
        
        setAttr("semesters", SemesterUtil.getAllSemesters());
        
        System.out.println("Query: " + select + " " + from);
        
        render("/view/student/score/index.html");
    }
} 