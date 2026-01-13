package com.example.validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.example.model.Student;

public class StudentValidator extends Validator {
    
    @Override
    protected void validate(Controller c) {
        validateRequiredString("student.name", "nameMsg", "姓名不能为空");
        validateRequiredString("student.student_no", "studentNoMsg", "学号不能为空");
        validateEmail("student.email", "emailMsg", "邮箱格式不正确");
        
        String actionKey = getActionKey();
        if (actionKey.equals("/admin/student/save")) {
            validateRequiredString("student.password", "passwordMsg", "密码不能为空");
        }
    }
    
    @Override
    protected void handleError(Controller c) {
        c.keepModel(Student.class);
        
        String actionKey = getActionKey();
        if (actionKey.equals("/admin/student/save"))
            c.render("/view/admin/student/add.html");
        else if (actionKey.equals("/admin/student/update"))
            c.render("/view/admin/student/edit.html");
    }
} 