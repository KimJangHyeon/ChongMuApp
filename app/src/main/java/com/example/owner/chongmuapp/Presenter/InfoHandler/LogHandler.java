package com.example.owner.chongmuapp.Presenter.InfoHandler;

import com.example.owner.chongmuapp.Model.Info.LogInfo;

import java.util.ArrayList;

/**
 * Created by Owner on 2018-01-22.
 */

public class LogHandler {
    private static LogHandler logHandler = null;
    private ArrayList<LogInfo> logInfoList = new ArrayList<>();

    private LogHandler(){}
    public static LogHandler getInstance(){
        if(logHandler == null)
            logHandler = new LogHandler();
        return logHandler;
    }

    public void refresh() { logInfoList.clear(); }

    public void addInfo(LogInfo logInfo){
        logInfoList.add(logInfo);
    }

    public void delInfo(int position){
        logInfoList.remove(position);
    }

    public ArrayList<LogInfo> getInfoList() {
        return this.logInfoList;
    }
}
