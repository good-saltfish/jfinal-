package com.example.routes;

import com.jfinal.config.Routes;
import com.example.controller.student.*;
import com.example.interceptor.StudentAuthInterceptor;

public class StudentRoutes extends Routes {
    @Override
    public void config() {
        setBaseViewPath("/view/student");
        
        // 所有路由都添加拦截器，在拦截器中处理例外情况
        addInterceptor(new StudentAuthInterceptor());
        
        // 登录控制器不设置视图路径前缀
        add("/student/login", StudentLoginController.class, "");
        
        // 其他控制器使用默认视图路径前缀
        add("/student", StudentIndexController.class);
        add("/student/course", StudentCourseController.class);
        add("/student/test", StudentTestController.class);
        add("/student/score", StudentScoreController.class);
        add("/student/profile", StudentProfileController.class);
    }
} 