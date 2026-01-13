package com.example.controller.admin;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.example.model.Teacher;

public class TeacherController extends Controller {

    public void index() {
        int pageNumber = getParaToInt("page", 1); // 默认为第一页
        int pageSize = 3; // 每页3行
        Page<Teacher> teacherPage = Teacher.dao.paginate(pageNumber, pageSize,
                "select *",
                "from teacher order by id desc");
        setAttr("teacherPage", teacherPage);
        render("/view/admin/teacher/index.html");
    }
    
    public void add() {
        render("/view/admin/teacher/add.html");
    }
    
    public void save() {
        Teacher teacher = getModel(Teacher.class, "teacher");
        if (teacher.save()) {
            redirect("/admin/teacher");
        } else {
            render("/view/admin/teacher/add.html");
        }
    }
    
    public void edit() {
        Teacher teacher = Teacher.dao.findById(getParaToInt());
        setAttr("teacher", teacher);
        render("/view/admin/teacher/edit.html");
    }
    
    public void update() {
        Teacher teacher = getModel(Teacher.class, "teacher");
        if (teacher.update()) {
            redirect("/admin/teacher");
        } else {
            render("/view/admin/teacher/edit.html");
        }
    }
    
    public void delete() {
        Teacher.dao.deleteById(getParaToInt());
        redirect("/admin/teacher");
    }
} 