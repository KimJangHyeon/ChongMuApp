package com.example.owner.chongmuapp.Presenter.InfoHandler;

import com.example.owner.chongmuapp.Model.Info.GroupInfo;
import com.example.owner.chongmuapp.Presenter.Subject;

import java.util.ArrayList;

/**
 * Created by Owner on 2018-01-22.
 */

public class GroupHandler extends Subject{
    private static GroupHandler groupHandler = null;
    private ArrayList<GroupInfo> groupInfoList = new ArrayList<>();

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

    public void refresh() { groupInfoList.clear(); }

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
}
