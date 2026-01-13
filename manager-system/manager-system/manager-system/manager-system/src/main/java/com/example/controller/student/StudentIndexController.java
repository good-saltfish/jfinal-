package com.example.controller.student;

import com.jfinal.core.Controller;
import com.example.model.Student;
import java.util.Date;

public class StudentIndexController extends Controller {
    public void index() {
        Student student = getSessionAttr("student");
        setAttr("student", student);
        setAttr("currentDate", new Date());
        render("/view/student/index.html");
    }
} 