package com.example.controller.admin;

import com.example.model.Student;
import com.jfinal.core.Controller;

public class AdminLoginController extends Controller {
    
    public void index() {
        if (getSessionAttr("admin") != null) {
            redirect("/admin");
            return;
        }
        render("/view/admin/login.html");
    }
    
    public void login() {
        String studentNo = getPara("username");
        String password = getPara("password");

        Student admin= Student.dao.findFirst("select * from student where student_no = ? and password = ?", studentNo, password);
        int role = Student.dao.findFirst("select role from student where student_no = ? and password = ?", studentNo, password).get("role");
        if (admin != null && role == 1) {
            setSessionAttr("admin", admin);
            redirect("/admin");
        } else {
            setAttr("error", "学号或密码错误");
            render("/view/admin/login.html");
        }


    }
    
    public void logout() {
        removeSessionAttr("admin");
        redirect("/admin/login");
    }
} 