package com.example.controller.student;

import com.jfinal.core.Controller;
import com.example.model.Student;

public class StudentLoginController extends Controller {
    
    public void index() {
        if (getSessionAttr("student") != null) {
            redirect("/student");
            return;
        }
        render("/view/student/login.html");
    }
    
    public void login() {
        String studentNo = getPara("student_no");
        String password = getPara("password");
        
        Student student = Student.dao.findFirst("select * from student where student_no = ? and password = ?", studentNo, password);
        
        if (student != null) {
            setSessionAttr("student", student);
            redirect("/student");
        } else {
            setAttr("error", "学号或密码错误");
            render("/view/student/login.html");
        }
    }
    
    public void logout() {
        removeSessionAttr("student");
        redirect("/student/login");
    }
} 