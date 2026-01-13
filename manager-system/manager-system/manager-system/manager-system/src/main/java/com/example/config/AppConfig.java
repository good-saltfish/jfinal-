package com.example.config;

import com.jfinal.config.*;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;
import com.example.model._MappingKit;
import com.example.routes.AdminRoutes;
import com.example.routes.StudentRoutes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AppConfig extends JFinalConfig {
    
    private static Prop p;
    
    static {
        p = PropKit.use("config.properties");
    }
    
    @Override
    public void configConstant(Constants me) {
        me.setDevMode(p.getBoolean("devMode", true));
        me.setInjectDependency(true);
    }
    
    @Override
    public void configRoute(Routes me) {
        me.add(new AdminRoutes());   // 管理员路由
        me.add(new StudentRoutes()); // 学生路由
    }
    
    @Override
    public void configEngine(Engine me) {
        me.addSharedFunction("/view/student/_layout.html");
        me.addSharedFunction("/view/admin/_layout.html");
    }
    
    @Override
    public void configPlugin(Plugins me) {
        DruidPlugin druidPlugin = new DruidPlugin(
            p.get("jdbcUrl"),
            p.get("user"),
            p.get("password")
        );
        me.add(druidPlugin);
        
        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        arp.setShowSql(p.getBoolean("devMode", true));
        _MappingKit.mapping(arp);
        arp.addSqlTemplate("/sql/init.sql");
        me.add(arp);
    }
    
    @Override
    public void configInterceptor(Interceptors me) {
    }
    
    @Override
    public void configHandler(Handlers me) {
        // 配置静态资源处理器
        me.add(new com.jfinal.handler.Handler() {
            public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
                if (target.startsWith("/static/")) {
                    try {
                        // 获取项目根路径
                        String webRoot = request.getServletContext().getRealPath("/");
                        // 构建静态资源的完整路径
                        String filePath = webRoot + target;
                        File file = new File(filePath);
                        
                        if (file.exists() && file.isFile()) {
                            // 设置响应类型
                            String contentType = request.getServletContext().getMimeType(target);
                            if (contentType != null) {
                                response.setContentType(contentType);
                            }
                            
                            // 设置缓存控制
                            response.setHeader("Cache-Control", "public, max-age=31536000");
                            
                            // 读取并输出文件内容
                            Files.copy(file.toPath(), response.getOutputStream());
                            isHandled[0] = true;
                            return;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                
                // 如果不是静态资源，继续下一个处理器
                next.handle(target, request, response, isHandled);
            }
        });
    }
} 