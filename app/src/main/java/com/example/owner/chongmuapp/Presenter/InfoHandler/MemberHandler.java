package com.example.owner.chongmuapp.Presenter.InfoHandler;

/**
 * Created by Owner on 2018-01-22.
 */

import com.example.owner.chongmuapp.Model.Info.IdPin;
import com.example.owner.chongmuapp.Model.Info.MemberInfo;
import com.example.owner.chongmuapp.Presenter.Subject;

import java.util.ArrayList;

public class MemberHandler extends Subject{
    private static MemberHandler memberHandler = null;
    private ArrayList<MemberInfo> memberInfoList = new ArrayList<>();
    private ArrayList<MemberInfo> saverInfoList = new ArrayList<>();

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

    public void addArrInfo(ArrayList<MemberInfo> memberList){
        memberInfoList.addAll(memberList);
    }

    public void delInfo(int position){
        memberInfoList.remove(position);
        notifyObservers();
    }
    /*add member function*/
    public boolean isChecked(int pos, int isChecked){
        memberInfoList.get(pos).setPin(isChecked);
        notifyObservers();
        return true;
    }

    public ArrayList<MemberInfo> getInfoList() {
        return this.memberInfoList;
    }
    public void flashInfoList(){
        saverInfoList.clear();
        // saverGroupInfoList.addAll(groupInfoList);
        for(MemberInfo m: memberInfoList)
            saverInfoList.add(new MemberInfo(m.getId(), m.getName(), m.getPin()));
    }
    public void loadInfoList(){
        memberInfoList.clear();
        memberInfoList.addAll(saverInfoList);

        notifyObservers();
    }

    public void setPin(ArrayList<IdPin> idPinList, int increase){
        for(MemberInfo memberInfo: memberInfoList){
            for(IdPin idPin: idPinList) {
                if (memberInfo.getId() ==idPin.getId()){
                    memberInfo.setPin(memberInfo.getPin()+increase);
                    idPinList.remove(idPin);
                    break;
                }
            }
        }
        notifyObservers();
    }
}
