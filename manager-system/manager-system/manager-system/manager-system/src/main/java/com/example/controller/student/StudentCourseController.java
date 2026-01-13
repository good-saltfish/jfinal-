package com.example.controller.student;

import com.jfinal.core.Controller;
import com.example.model.Course;
import com.example.model.Student;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Db;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

public class StudentCourseController extends Controller {
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


        Page<Course> coursePage2 = Course.dao.paginate(pageNumber, 10,
                "select c.*, t.name as teacher_name",
                "from course c left join teacher t on c.teacher_id = t.id order by c.id desc");

        setAttr("coursePage", coursePage);
        setAttr("coursePage2", coursePage2);
        render("/view/student/course/index.html");
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
    // 1. Create config object

    // 选课
    public void select() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        RedissonClient redisson = Redisson.create(config);
        Student student = getSessionAttr("student");
        // 重新获取最新的学生信息
        student = Student.dao.findById(student.getId());
        setAttr("student", student);

        Integer courseId = getParaToInt(0);

        // 检查课程状态
        Course course = Course.dao.findById(courseId);
        if (course == null || !"not_started".equals(course.getStr("status")) || course.getInt("have_stu") == 0)  {
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
        // 使用Redisson获取分布式锁
        RLock lock = redisson.getLock("courseSelectLock:" + courseId);
        // 添加选课记录
        try {
            // 尝试获取锁，等待时间设置为10秒，锁的持有时间设置为30秒
            boolean isLocked = lock.tryLock(10, 30, TimeUnit.SECONDS);
            if (isLocked) {
                try {
                    // 添加选课记录
                    Db.update("insert into course_selection(student_id, course_id) values(?, ?)",
                            student.getId(), courseId);
                    Db.update("update course set have_stu = have_stu - 1 where id = ? and have_stu > 0", courseId);
                    setAttr("msg", "选课成功");
                    index();
                } finally {
                    // 释放锁
                    lock.unlock();
                }
            }

        } catch (Exception e) {
            setAttr("error", "选课失败，请稍后重试");
            available();
        }
    }
} 