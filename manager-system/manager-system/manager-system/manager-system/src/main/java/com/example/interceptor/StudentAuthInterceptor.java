package com.example.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.example.model.Student;

import java.util.List;

public class StudentAuthInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation inv) {
        // 登录相关的URL不需要验证
        String actionKey = inv.getActionKey();
        if (actionKey.startsWith("/student/login")) {
            inv.invoke();
            return;
        }
        
        // 验证是否已登录
        if (inv.getController().getSessionAttr("student") != null) {
            Student student = inv.getController().getSessionAttr("student");
            

            
            inv.invoke();
        } else {
            inv.getController().redirect("/student/login");
        }
    }
} 