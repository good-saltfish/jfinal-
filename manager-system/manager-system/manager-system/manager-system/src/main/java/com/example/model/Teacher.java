package com.example.model;

import com.jfinal.plugin.activerecord.Model;

public class Teacher extends Model<Teacher> {
    public static final Teacher dao = new Teacher().dao();
} 