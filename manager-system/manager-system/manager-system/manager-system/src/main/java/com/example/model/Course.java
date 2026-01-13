package com.example.model;

import com.jfinal.plugin.activerecord.Model;

public class Course extends Model<Course> {
    public static final Course dao = new Course().dao();
} 