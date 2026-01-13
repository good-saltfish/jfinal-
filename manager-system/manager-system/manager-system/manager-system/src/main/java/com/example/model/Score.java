package com.example.model;

import com.jfinal.plugin.activerecord.Model;

public class Score extends Model<Score> {
    public static final Score dao = new Score().dao();
} 