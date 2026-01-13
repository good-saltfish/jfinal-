package com.example.controller.student;

import com.jfinal.core.Controller;
import com.example.model.Student;
import com.jfinal.aop.Before;
import com.jfinal.upload.UploadFile;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class StudentProfileController extends Controller {
    
    public void index() {
        Student student = getSessionAttr("student");
        student = Student.dao.findById(student.getId());
        setAttr("student", student);
        render("/view/student/profile/index.html");
    }
    

    public void update() {
        Student student = getSessionAttr("student");
        
        // 判断是否是修改密码的请求
        String oldPassword = getPara("old_password");
        if (oldPassword != null) {
            // 修改密码
            String newPassword = getPara("student.password");
            Student dbStudent = Student.dao.findById(student.getId());
            dbStudent.set("password", newPassword);
            
            if (dbStudent.update()) {
                setSessionAttr("student", dbStudent);
                setAttr("passwordMsg", "密码修改成功");
            } else {
                setAttr("oldPasswordMsg", "系统错误，请稍后重试");
            }
        } else {
            // 修改基本信息
            String phone = getPara("student.phone");
            String email = getPara("student.email");
            
            Student newStudent = Student.dao.findById(student.getId());
            newStudent.set("phone", phone);
            newStudent.set("email", email);
            
            if (newStudent.update()) {
                setSessionAttr("student", newStudent);
                setAttr("msg", "基本信息更新成功");
            } else {
                setAttr("error", "基本信息更新失败");
            }
        }
        
        // 重新获取最新数据
        student = Student.dao.findById(student.getId());
        setAttr("student", student);
        render("/view/student/profile/index.html");
    }
    
    public void avatar() {
        Student student = getSessionAttr("student");
        UploadFile uploadFile = getFile("avatar");
        
        if (uploadFile != null) {
            try {
                String originalFileName = uploadFile.getOriginalFileName();
                String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
                String newFileName = "avatar_" + student.getId() + extension;
                
                // 确保上传目录存在
                String uploadPath = getRequest().getServletContext().getRealPath("/upload/avatar");
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                
                // 移动文件到正确的位置
                File targetFile = new File(uploadDir, newFileName);
                if (targetFile.exists()) {
                    targetFile.delete();
                }
                uploadFile.getFile().renameTo(targetFile);
                
                // 更新数据库
                String avatarUrl = "/upload/avatar/" + newFileName;
                student.set("avatar", avatarUrl).update();
                setSessionAttr("student", student);
                
                // 返回成功响应
                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                renderJson(result);
            } catch (Exception e) {
                // 返回错误响应
                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("msg", "头像上传失败：" + e.getMessage());
                renderJson(result);
            }
        } else {
            // 返回错误响应
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("msg", "请选择要上传的头像");
            renderJson(result);
        }
    }
} 