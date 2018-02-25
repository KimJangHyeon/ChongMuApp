package com.example.owner.chongmuapp.Presenter.InfoHandler;

import android.util.Log;

import com.example.owner.chongmuapp.Model.Info.GroupInfo;
import com.example.owner.chongmuapp.Presenter.Subject;

import java.util.ArrayList;

/**
 * Created by Owner on 2018-01-22.
 */

public class GroupHandler extends Subject{
    private static GroupHandler groupHandler = null;
    private ArrayList<GroupInfo> groupInfoList = new ArrayList<>();
    private ArrayList<GroupInfo> saverGroupInfoList = new ArrayList<>();

    private boolean initialized = false;

    public GroupHandler(){}

    public boolean isInitialized() {
        return initialized;
    }
    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public static GroupHandler getInstance(){
        if(groupHandler == null)
            groupHandler = new GroupHandler();
        return groupHandler;
    }

    public void refresh() {
        groupInfoList.clear();
    }

    public void addInfo(GroupInfo groupInfo){
        groupInfoList.add(groupInfo);
        notifyObservers();
    }

    public void delInfo(int position){
        groupInfoList.remove(position);
        notifyObservers();
    }

    public boolean isChecked(int pos, int isChecked){
        groupInfoList.get(pos).setPin(isChecked);
        notifyObservers();
        return true;
    }

    public ArrayList<GroupInfo> getInfoList() {
        return this.groupInfoList;
    }


    public void flashGroupInfoList(){
        saverGroupInfoList.clear();
        // saverGroupInfoList.addAll(groupInfoList);
        for(GroupInfo g: groupInfoList)
            saverGroupInfoList.add(new GroupInfo(g.getId(), g.getName(), g.getPin()));
    }
    public void loadGroupInfoList(){
        groupInfoList.clear();
        groupInfoList.addAll(saverGroupInfoList);

        notifyObservers();
    }
    public void debugLogSaver(){
        for(GroupInfo g: saverGroupInfoList){
            Log.e("saverGroupInfoList", "pin: " + g.getPin());
        }
    }

    public void setPin(int gid, int increase){
        for(GroupInfo groupInfo: groupInfoList){
            if(groupInfo.getId() == gid) {
                groupInfo.setPin(groupInfo.getPin()+increase);
                break;
            }
        }
        notifyObservers();
    }
}
