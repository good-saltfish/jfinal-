package com.example.controller.admin;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.example.model.Student;
import com.example.validator.StudentValidator;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.kit.Ret;
import java.util.Date;
import java.util.List;

public class StudentController extends Controller {

    public void index() {
        int pageNumber = getParaToInt("page", 1);
        Page<Student> studentPage = Student.dao.paginate(pageNumber, 10, "select *", "from student order by id desc");
        setAttr("studentPage", studentPage);
        render("/view/admin/student/index.html");
    }

    public void add() {
        render("/view/admin/student/add.html");
    }

    @Before(StudentValidator.class)
    public void save() {
        Student student = getModel(Student.class, "student");
        if (student.save()) {
            redirect("/admin/student");
        } else {
            render("/view/admin/student/add.html");
        }
    }

    public void edit() {
        Student student = Student.dao.findById(getParaToInt());
        setAttr("student", student);
        render("/view/admin/student/edit.html");
    }

    @Before(StudentValidator.class)
    public void update() {
        Student student = getModel(Student.class, "student");
        String password = getPara("student.password");
        if (password == null || password.trim().isEmpty()) {
            Student oldStudent = Student.dao.findById(student.getId());
            student.set("password", oldStudent.getStr("password"));
        }

        if (student.update()) {
            redirect("/admin/student");
        } else {
            render("/view/admin/student/edit.html");
        }
    }

    public void delete() {
        Student.dao.deleteById(getParaToInt());
        redirect("/admin/student");
    }

    public void courses() {
        try {
            Long studentId = getParaToLong(0);
            Student student = Student.dao.findById(studentId);
            if (student == null) {
                redirect("/admin/student");
                return;
            }

            // 获取学生已选课程
            List<Record> selectedCourses = Db.find(
                    "SELECT c.*, t.name as teacher_name FROM course c " +
                            "LEFT JOIN teacher t ON c.teacher_id = t.id " +
                            "JOIN course_selection cs ON c.id = cs.course_id " +
                            "WHERE cs.student_id = ? " +
                            "ORDER BY c.course_name",
                    studentId
            );

            // 获取可选课程（排除已选的）
            List<Record> availableCourses = Db.find(
                    "SELECT c.*, t.name as teacher_name FROM course c " +
                            "LEFT JOIN teacher t ON c.teacher_id = t.id " +
                            "WHERE c.id NOT IN " +
                            "(SELECT course_id FROM course_selection WHERE student_id = ?) " +
                            "ORDER BY c.course_name",
                    studentId
            );

            setAttr("student", student);
            setAttr("selectedCourses", selectedCourses);
            setAttr("availableCourses", availableCourses);
            render("/view/admin/student/courses.html");

        } catch (Exception e) {
            e.printStackTrace();
            setAttr("message", "加载课程信息失败：" + e.getMessage());
            render("/view/admin/error.html");
        }
    }

    public void courseAdd() {
        try {
            Long studentId = getParaToLong("studentId");
            Long courseId = getParaToLong("courseId");

            if (studentId == null || courseId == null) {
                renderJson(new Ret().setFail().set("msg", "参数错误"));
                return;
            }

            // 检查是否已选
            Record exists = Db.findFirst(
                    "SELECT * FROM course_selection WHERE student_id = ? AND course_id = ?",
                    studentId, courseId
            );

            if (exists != null) {
                renderJson(new Ret().setFail().set("msg", "该课程已经选择"));
                return;
            }

            // 添加选课记录
            Record record = new Record();
            record.set("student_id", studentId);
            record.set("course_id", courseId);
            record.set("select_time", new Date());

            boolean success = Db.save("course_selection", record);

            if (success) {
                renderJson(new Ret().setOk());
            } else {
                renderJson(new Ret().setFail().set("msg", "添加失败"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(new Ret().setFail().set("msg", "系统错误：" + e.getMessage()));
        }
    }

} 