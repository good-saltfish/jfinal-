package com.example.routes;

import com.jfinal.config.Routes;
import com.example.controller.admin.*;
import com.example.interceptor.AdminAuthInterceptor;

public class AdminRoutes extends Routes {
    @Override
    public void config() {
        setBaseViewPath("/view/admin");

        addInterceptor(new AdminAuthInterceptor());

        add("/admin/login", AdminLoginController.class, "");

        add("/admin", AdminIndexController.class);
        add("/admin/student", StudentController.class);
        add("/admin/course", CourseController.class);
        add("/admin/score", ScoreController.class);
        add("/admin/teacher", TeacherController.class);

    }
} 