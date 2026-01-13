package com.example.controller.admin;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.example.model.Course;
import com.example.model.Teacher;
import com.jfinal.plugin.activerecord.SqlPara;

public class CourseController extends Controller {
    
    public void index() {
        int pageNumber = getParaToInt("page", 1); // 默认为第一页
        int pageSize = 10; // 每页10行

        // 获取SQL模板参数
        SqlPara sqlPara = Db.getSqlPara("findCourses");

        // 使用分页查询
        Page<Course> coursePage = Course.dao.paginate(pageNumber, pageSize, sqlPara);

        setAttr("coursePage", coursePage); // 将分页结果设置为模型属性
        render("/view/admin/course/index.html"); // 渲染视图
    }
    
    public void add() {
        setAttr("teachers", Teacher.dao.find("select * from teacher"));
        render("/view/admin/course/add.html");  // 修改这里
    }
    
    public void save() {
        Course course = getModel(Course.class, "course");
        if (course.save()) {
            redirect("/admin/course");
        } else {
            render("/view/admin/course/add.html");  // 修改这里
        }
    }
    
    public void edit() {
        Course course = Course.dao.findById(getParaToInt());
        setAttr("course", course);
        setAttr("teachers", Teacher.dao.find("select * from teacher"));
        render("/view/admin/course/edit.html");  // 修改这里
    }
    
    public void update() {
        Course course = getModel(Course.class, "course");
        if (course.update()) {
            redirect("/admin/course");
        } else {
            render("/view/admin/course/edit.html");  // 修改这里
        }
    }
    
    public void delete() {
        Course.dao.deleteById(getParaToInt());
        redirect("/admin/course");
    }
} 