package com.example.controller.student;

import com.example.model.Course;
import com.example.model.Score;
import com.example.model.Student;
import com.example.util.SemesterUtil;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.SqlPara;

public class StudentTestController extends Controller {
    // 我的课程
    public void index() {

        Student student = getSessionAttr("student");
        student = Student.dao.findById(student.getId());
        setAttr("student", student);

        int pageNumber = getParaToInt("page", 1);
        Page<Course> coursePage = Course.dao.paginate(pageNumber, 10,
                "select c.*, t.name as teacher_name",
                "from course c " +
                        "left join teacher t on c.teacher_id = t.id " +
                        "where c.id in (select course_id from course_selection where student_id = ?)",
                student.getId());
        setAttr("coursePage", coursePage);
        render("/view/student/test/index.html");
    }

    // 可选课程列表
    public void available() {
        Student student = getSessionAttr("student");
        // 重新获取最新的学生信息
        student = Student.dao.findById(student.getId());
        setAttr("student", student);

        int pageNumber = getParaToInt("page", 1);
        Page<Course> coursePage = Course.dao.paginate(pageNumber, 10,
                "select c.*, t.name as teacher_name",
                "from course c " +
                        "left join teacher t on c.teacher_id = t.id " +
                        "where c.status = 'not_started' " +
                        "and c.id not in (select course_id from course_selection where student_id = ?)",
                student.getId());
        setAttr("coursePage", coursePage);
        render("/view/student/course/available.html");
    }

    // 选课
    public void select() {
        Student student = getSessionAttr("student");
        // 重新获取最新的学生信息
        student = Student.dao.findById(student.getId());
        setAttr("student", student);

        Integer courseId = getParaToInt(0);

        // 检查课程状态
        Course course = Course.dao.findById(courseId);
        if (course == null || !"not_started".equals(course.getStr("status"))) {
            setAttr("error", "该课程不可选");
            available();
            return;
        }

        // 检查是否已选
        Long count = Db.queryLong(
                "select count(*) from course_selection where student_id = ? and course_id = ?",
                student.getId(), courseId);

        if (count > 0) {
            setAttr("error", "已经选过这门课程了");
            available();
            return;
        }

        // 添加选课记录
        try {
            Db.update("insert into course_selection(student_id, course_id) values(?, ?)",
                    student.getId(), courseId);
            setAttr("msg", "选课成功");
            index();
        } catch (Exception e) {
            setAttr("error", "选课失败，请稍后重试");
            available();
        }
    }
} 