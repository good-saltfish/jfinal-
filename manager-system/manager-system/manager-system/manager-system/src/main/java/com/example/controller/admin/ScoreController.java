package com.example.controller.admin;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.example.model.Score;
import com.example.model.Student;
import com.example.model.Course;
import com.example.util.SemesterUtil;

public class ScoreController extends Controller {
    
    public void index() {
        int pageNumber = getParaToInt("page", 1);
        String semester = getPara("semester", SemesterUtil.getCurrentSemester());
        
        String select = "select s.*, st.name as student_name, st.student_no, c.course_name";
        String from = "from score s " +
            "left join student st on s.student_id = st.id " +
            "left join course c on s.course_id = c.id " +
            "where s.semester = ? " +
            "order by s.id desc";
            
        Page<Score> scorePage = Score.dao.paginate(pageNumber, 10, select, from, semester);
        
        setAttr("scorePage", scorePage);
        setAttr("currentSemester", SemesterUtil.getCurrentSemester());
        render("/view/admin/score/index.html");
    }
    
    public void add() {
        setAttr("students", Student.dao.find("select * from student"));
        setAttr("courses", Course.dao.find("select * from course"));
        setAttr("currentSemester", SemesterUtil.getCurrentSemester());
        render("/view/admin/score/add.html");
    }
    
    public void save() {
        Score score = getModel(Score.class, "score");
        if (score.save()) {
            redirect("/admin/score");
        } else {
            render("/view/admin/score/add.html");
        }
    }
    
    public void edit() {
        Score score = Score.dao.findById(getParaToInt());
        setAttr("score", score);
        setAttr("students", Student.dao.find("select * from student"));
        setAttr("courses", Course.dao.find("select * from course"));
        render("/view/admin/score/edit.html");
    }
    
    public void update() {
        Score score = getModel(Score.class, "score");
        if (score.update()) {
            redirect("/admin/score");
        } else {
            render("/view/admin/score/edit.html");
        }
    }
    
    public void delete() {
        Score.dao.deleteById(getParaToInt());
        redirect("/admin/score");
    }
} 