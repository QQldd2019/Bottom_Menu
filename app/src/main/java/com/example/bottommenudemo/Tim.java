package com.example.bottommenudemo;

import android.app.Application;

import java.util.Timer;

public class Tim extends Application {                                                              //新建定时器类，用于刷新界面显示
    private static Timer timer=null;
    public static Timer getTimer() {
        if(timer==null){
            timer = new Timer();
        }
        return timer;
    }
}
