package com.example.model;

import com.jfinal.plugin.activerecord.Model;

public class Student extends Model<Student> {
    public static final Student dao = new Student().dao();
    
    public Integer getId() {
        return getInt("id");
    }
    
    public String getStudentNo() {
        return getStr("student_no");
    }
    
    public String getName() {
        return getStr("name");
    }
    
    public String getGender() {
        return getStr("gender");
    }
    
    public Integer getAge() {
        return getInt("age");
    }
    
    public String getEmail() {
        return getStr("email");
    }
    
    public String getPhone() {
        return getStr("phone");
    }
    
    public String getPassword() {
        return getStr("password");
    }
} 