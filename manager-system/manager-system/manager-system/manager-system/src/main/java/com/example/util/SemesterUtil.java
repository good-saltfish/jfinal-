package com.example.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Collections;

public class SemesterUtil {
    
    public static String getCurrentSemester() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1; // Calendar.MONTH 从 0 开始
        
        // 2月前为上一年的秋季学期，6月前为春季学期，其他为秋季学期
        if (month < 2) {
            return (year-1) + "-" + year + "秋";
        } else if (month < 6) {
            return (year-1) + "-" + year + "春";
        } else {
            return year + "-" + (year+1) + "秋";
        }
    }
    
    public static List<String> getAllSemesters() {
        List<String> semesters = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        
        // 从当前年份到2010年，倒序添加
        for (int year = currentYear; year >= 2010; year--) {
            // 先添加秋季学期
            semesters.add(year + "-" + (year+1) + "秋");
            // 再添加春季学期
            semesters.add(year + "-" + (year+1) + "春");
        }
        
        return semesters;
    }
} 