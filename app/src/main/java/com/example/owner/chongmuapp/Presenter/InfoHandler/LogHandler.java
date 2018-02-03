package com.example.owner.chongmuapp.Presenter.InfoHandler;

import com.example.owner.chongmuapp.Model.Info.IdPin;
import com.example.owner.chongmuapp.Model.Info.LogInfo;
import com.example.owner.chongmuapp.Presenter.Subject;

import java.util.ArrayList;

/**
 * Created by Owner on 2018-01-22.
 */

public class LogHandler extends Subject{
    private static LogHandler logHandler = null;
    private ArrayList<LogInfo> logInfoList = new ArrayList<>();

    private boolean initialized = false;

    public LogHandler(){}

    public static LogHandler getInstance(){
        if(logHandler == null)
            logHandler = new LogHandler();
        return logHandler;
    }

    public boolean isInitialized() {
        return initialized;
    }
    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }


    public void refresh() { logInfoList.clear(); }

    public void addInfo(LogInfo logInfo){
        logInfoList.add(logInfo);
        notifyObservers();
    }

//    public void delInfo(int position){
//        logInfoList.remove(position);
//    }

    public ArrayList<LogInfo> getInfoList() {
        return this.logInfoList;
    }

    public void setAvg(int newAvg){
        for(LogInfo logInfo: logInfoList){
            if(!logInfo.getDone()){
                logInfo.setPayables(newAvg);
            }
        }
        notifyObservers();
    }

    //leftPay
    //allPay - setPay
    //if done -> +old - new / leftMember 그대로
    //else -> -new / leftMember -1
    public int leftPay(int allPay, int setPay, int leftMember){
        int result = allPay;
        int count = 0;
        for(LogInfo logInfo: logInfoList){
            if(logInfo.getDone()){
                result = result - logInfo.getPayables();
                count++;
            }
            if(count== leftMember) break;
        }
        return result;
    }

    //fixed
    public LogInfo setDone(int pos){
        LogInfo logInfo = logInfoList.get(pos);
        if (logInfo.getDone()) {
            logInfo.setDone(false);
        } else{
            logInfo.setDone(true);
        }
        notifyObservers();

        return logInfo;
    }

}
