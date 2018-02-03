package com.example.owner.chongmuapp.Presenter;

import android.content.Context;
import android.util.Log;

import com.example.owner.chongmuapp.Common.Constant;
import com.example.owner.chongmuapp.Model.Data.Join.SQLiteGM;
import com.example.owner.chongmuapp.Model.Info.MemberInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Owner on 2018-01-28.
 */

public class GMJoinPresenter {
    /* Presenter Setting */
    Context context;

    SQLiteGM gmJoinDB;

    ArrayList<String> matchingGid;

    public void setContext(Context context) {
        this.context = context;
    }

    public void addInfo(int gid, int mid){
        gmJoinDB  = new SQLiteGM(context, Constant.DB_NAME, null, 4);
        gmJoinDB.insert(gid, mid);
        gmJoinDB.close();
    }
    public void addArrInfo(ArrayList<String> idList, int id, boolean isGroup){
        gmJoinDB = new SQLiteGM(context, Constant.DB_NAME, null, 4);
        if(isGroup) {
            for (String gid_str : idList) {
                Log.e("Group addArrInfo", gid_str);
                gmJoinDB.insert(Integer.parseInt(gid_str), id);
            }
        }else{
            for (String mid_str : idList) {
                Log.e("Member addArrInfo", mid_str);
                gmJoinDB.insert(id, Integer.parseInt(mid_str));
            }
        }
        gmJoinDB.close();
    }
    public void delArrInfo(ArrayList<String> idList, int id, boolean isGroup){
        gmJoinDB = new SQLiteGM(context, Constant.DB_NAME, null, 4);
        if(isGroup) {
            for (String gid_str : idList) {
                Log.e("delArrInfo", gid_str);
                gmJoinDB.delete(id, Integer.parseInt(gid_str));
            }
        } else{
            for (String mid_str : idList) {
                Log.e("delArrInfo", mid_str);
                gmJoinDB.delete(Integer.parseInt(mid_str), id);
            }
        }
        gmJoinDB.close();
    }
    public void delByGroup(int gid){
        gmJoinDB = new SQLiteGM(context, Constant.DB_NAME, null, 4);
        gmJoinDB.deleteByGid(gid);
        gmJoinDB.close();
    }
    public void delByMember(int mid){
        gmJoinDB = new SQLiteGM(context, Constant.DB_NAME, null, 4);
        gmJoinDB.deleteByMid(mid);
        gmJoinDB.close();
    }
    public ArrayList<String> getMatchingGid(int mid){
        matchingGid = new ArrayList<>();
        gmJoinDB  = new SQLiteGM(context, Constant.DB_NAME, null, 4);
        gmJoinDB.getMatchingGroup(mid, matchingGid);
        gmJoinDB.close();
        Log.e("matchingGid", matchingGid+"");
        return matchingGid;
    }

    public ArrayList<MemberInfo> getGroupJoin(int gid){
        ArrayList<MemberInfo> result = new ArrayList<>();
        gmJoinDB  = new SQLiteGM(context, Constant.DB_NAME, null, 4);
        gmJoinDB.getGroupJoin(gid, result);
        gmJoinDB.close();
        return result;
    }

}
