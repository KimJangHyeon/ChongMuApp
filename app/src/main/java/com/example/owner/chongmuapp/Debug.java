package com.example.owner.chongmuapp;

import android.util.Log;

import com.example.owner.chongmuapp.Model.Info.LogInfo;

import java.util.ArrayList;

/**
 * Created by Owner on 2018-02-01.
 */

public class Debug {
    public void stringList(String title, ArrayList<String> list){
        Log.e(title, "DEBUG");
        for(String str: list){
            Log.e("list element", str);
        }
    }
    public void LogList(String title, ArrayList<LogInfo> list){
        Log.e(title, "DEBUG");
        for(LogInfo l: list){
            Log.e("list element name", l.getName());
            Log.e("list element id", l.getMid()+"");
            Log.e("list element done", l.getDone()+"");
            Log.e("list element pay", l.getPayables()+"");
        }
    }
}
