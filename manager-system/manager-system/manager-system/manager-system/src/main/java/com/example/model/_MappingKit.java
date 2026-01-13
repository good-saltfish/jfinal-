package com.example.model;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

public class _MappingKit {
    public static void mapping(ActiveRecordPlugin arp) {
        arp.addMapping("student", "id", Student.class);
        arp.addMapping("course", "id", Course.class);
        arp.addMapping("score", "id", Score.class);
        arp.addMapping("teacher", "id", Teacher.class);
    }
} 