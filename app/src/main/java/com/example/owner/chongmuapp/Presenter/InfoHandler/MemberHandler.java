package com.example.owner.chongmuapp.Presenter.InfoHandler;

/**
 * Created by Owner on 2018-01-22.
 */

import com.example.owner.chongmuapp.Model.Info.MemberInfo;
import com.example.owner.chongmuapp.Presenter.Subject;

import java.util.ArrayList;

public class MemberHandler extends Subject{
    private static MemberHandler memberHandler = null;
    private ArrayList<MemberInfo> memberInfoList = new ArrayList<>();

    private boolean initialized = false;

    public boolean isInitialized() {
        return initialized;
    }
    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public MemberHandler(){}
    public static MemberHandler getInstance(){
        if(memberHandler == null)
            memberHandler = new MemberHandler();
        return memberHandler;
    }

    public void refresh() {
        memberInfoList.clear();
        notifyObservers();
    }

    public void addInfo(MemberInfo memberInfo){
        memberInfoList.add(memberInfo);
        notifyObservers();
    }

    public void delInfo(int position){
        memberInfoList.remove(position);
        notifyObservers();
    }

    public ArrayList<MemberInfo> getInfoList() {
        return this.memberInfoList;
    }
}
