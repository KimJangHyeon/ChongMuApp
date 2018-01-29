package com.example.owner.chongmuapp.Presenter;

import android.content.Context;
import android.util.Log;

import com.example.owner.chongmuapp.Common.Constant;
import com.example.owner.chongmuapp.Model.Data.Join.SQLiteGM;

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
    public void addArrInfo(ArrayList<String> gidList, int mid){
        gmJoinDB  = new SQLiteGM(context, Constant.DB_NAME, null, 4);
        for(String gid_str: gidList){
            Log.e("addArrInfo", gid_str);
            gmJoinDB.insert(Integer.parseInt(gid_str), mid);
        }
        gmJoinDB.close();
    }
    public void delArrInfo(ArrayList<String> gidList, int mid){
        gmJoinDB  = new SQLiteGM(context, Constant.DB_NAME, null, 4);
        for(String gid_str: gidList){
            Log.e("delArrInfo", gid_str);
            gmJoinDB.delete(mid, Integer.parseInt(gid_str));
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
        Log.e("matchingGid", matchingGid+"");
        return matchingGid;
    }

}
