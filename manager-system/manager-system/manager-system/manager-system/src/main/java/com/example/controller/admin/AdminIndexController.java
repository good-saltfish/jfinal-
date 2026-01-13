package com.example.controller.admin;

import com.jfinal.core.Controller;

public class AdminIndexController extends Controller {
    public void index() {
        setAttr("admin", getSessionAttr("admin"));
        render("/view/admin/index.html");
    }
} 