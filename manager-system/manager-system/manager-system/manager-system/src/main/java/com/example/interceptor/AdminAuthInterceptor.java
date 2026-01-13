package com.example.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class AdminAuthInterceptor implements Interceptor {
    
    @Override
    public void intercept(Invocation inv) {
        // 登录相关的URL不需要验证
        String actionKey = inv.getActionKey();
        // 字符串匹配检查
        if (actionKey.startsWith("/admin/login")) {
            inv.invoke();
            return;
        }
        
        // 验证是否已登录
        if (inv.getController().getSessionAttr("admin") != null) {
            inv.invoke();
        } else {
            inv.getController().redirect("/admin/login");
        }
    }
} 