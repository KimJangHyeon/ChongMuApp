package com.example.owner.chongmuapp.Model.Data.Join;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.owner.chongmuapp.Common.Constant;
import com.example.owner.chongmuapp.Debug;
import com.example.owner.chongmuapp.Model.Info.IdPin;
import com.example.owner.chongmuapp.Model.Info.LogInfo;
import com.example.owner.chongmuapp.Presenter.InfoHandler.LogHandler;

import java.util.ArrayList;

/**
 * Created by Owner on 2018-01-23.
 */

public class SQLiteLog extends SQLiteOpenHelper{
    private String Table = "payables";
    private final String mTable = "member";
    public SQLiteLog (Context context, String db_name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, db_name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //group table
        db.execSQL("CREATE TABLE if not exists " + Constant.GROUP_TABLE +
                " (gid INTEGER PRIMARY KEY AUTOINCREMENT, name STRING, pin INTEGER)"
        );

        //member table
        db.execSQL("CREATE TABLE if not exists " + Constant.MEMBER_TABLE
                + " (mid INTEGER PRIMARY KEY AUTOINCREMENT, name STRING, pin INTEGER)"
        );

        //event table
        db.execSQL("CREATE TABLE if not exists " + Constant.EVENT_TABLE
                + " (gid INTEGER, eid INTEGER PRIMARY KEY AUTOINCREMENT, name STRING, pay INTEGER, datetime DATETIME DEFAULT CURRENT_TIMESTAMP, left INTEGER)"
        );

        //group & member table
        db.execSQL("CREATE TABLE if not exists " + Constant.GM_JOIN_TABLE
                + " (gid INTEGER NOT NULL, mid INTEGER NOT NULL, " +
                "FOREIGN KEY (gid) REFERENCES " + Constant.GROUP_TABLE
                +" (gid), "+
                "FOREIGN KEY (mid) REFERENCES " + Constant.MEMBER_TABLE
                +" (mid)) "
        );

        //payables table
        db.execSQL("CREATE TABLE if not exists " + Constant.PAY_TABLE
                + " (gid INTEGER, eid INTEGER, mid INTEGER, pay INTEGER, payables BOOLEAN, "
                + "FOREIGN KEY (mid) REFERENCES " + Constant.MEMBER_TABLE + " (mid)) "
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        throw new UnsupportedOperationException("not implemented");
    }

    public void update(int gid, int eid, int mid, boolean payables){
        ContentValues values = new ContentValues();
        values.put("payables",  payables);    //carNumber를 변경하고자 할때
        this.getWritableDatabase().update(Constant.PAY_TABLE, values, "gid = ? AND eid = ? AND mid = ?", new String[] {String.valueOf(gid), String.valueOf(eid), String.valueOf(mid)});
    }

    public void insert(int gid, int eid, int mid, int pay, boolean payables){
        ContentValues values = new ContentValues();
        values.put("gid", gid);
        values.put("eid", eid);
        values.put("mid", mid);
        values.put("pay", pay);
        values.put("payables", payables);
        this.getWritableDatabase().insert(Constant.PAY_TABLE, null, values);
    }

    public void insertAll(int gid, int eid, ArrayList<LogInfo> logList){

        for(LogInfo logInfo: logList){
            ContentValues values = new ContentValues();
            values.put("gid", gid);
            values.put("eid", eid);
            values.put("mid", logInfo.getMid());
            values.put("pay", logInfo.getPayables());
            values.put("payables", false);
            this.getWritableDatabase().insert(Constant.PAY_TABLE, null, values);
        }
    }

    public void delete(ArrayList<String> midList, int eid){
//        String[] idArgs = {String.valueOf(gid), String.valueOf(eid), String.valueOf(mid)};
//        this.getWritableDatabase().delete(Table, "gid = ? AND eid = ? AND mid = ?", idArgs);
    }

    public void eventDel(int eid){
        this.getWritableDatabase().delete(Constant.PAY_TABLE, "eid = ?", new String[]{ String.valueOf(eid)});
    }

    public void getInfoAll(int gid, int eid, LogHandler logHandler){
        boolean done;
        //try {
        this.getReadableDatabase();
            Log.e("gid, eid", gid + " " + eid);
            Cursor cursor = this.getReadableDatabase().rawQuery("SELECT " + Constant.MEMBER_TABLE + ".mid, " + Constant.MEMBER_TABLE + ".name, " + Constant.PAY_TABLE + ".pay, " + Constant.PAY_TABLE + ".payables"
                    + " FROM " + Constant.PAY_TABLE + " JOIN " + Constant.MEMBER_TABLE
                    + " ON " + Constant.MEMBER_TABLE + ".mid = " + Constant.PAY_TABLE + ".mid"
                    + " WHERE " + Constant.PAY_TABLE + ".gid = " + gid + " AND " + Constant.PAY_TABLE + ".eid = " + eid, null);
//            Cursor cursor = this.getReadableDatabase().rawQuery("SELECT "+Constant.MEMBER_TABLE+".mid, " +Constant.MEMBER_TABLE+".name, "+Constant.MEMBER_TABLE+".pin "
//                    + "FROM "+Constant.MEMBER_TABLE+" JOIN "+Constant.GM_JOIN_TABLE
//                    +" ON "+ Constant.MEMBER_TABLE+".mid = "+Constant.GM_JOIN_TABLE+".mid"
//                    +" WHERE "+Constant.GM_JOIN_TABLE+".gid = "+gid, null);
            if (cursor.moveToFirst()) {
                do {
                    if (cursor.getInt(3) > 0) done = true;
                    else done = false;
                    logHandler.getInstance().addInfo(new LogInfo(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), done));
                } while (cursor.moveToNext());
            }
//        }catch(Exception e){
//            Log.e("GM return ", "false");
//            Log.e("GM Error", e+"");
//            return;
//        }
    }
    public ArrayList<IdPin> getmemIdPin(int gid, int eid) {
        ArrayList<IdPin> result = new ArrayList<>();
        Cursor cursor = this.getWritableDatabase().rawQuery("SELECT " + Constant.MEMBER_TABLE + ".mid, " + Constant.MEMBER_TABLE + ".pin "
                + "FROM " + Constant.MEMBER_TABLE + " JOIN " + Constant.PAY_TABLE
                + " ON " + Constant.MEMBER_TABLE + ".mid = " + Constant.PAY_TABLE + ".mid"
                + " WHERE " + Constant.PAY_TABLE + ".gid = " + gid + " AND " + Constant.PAY_TABLE + ".eid = " + eid, null);

        if (cursor.moveToFirst()) {
            do {
                result.add(new IdPin(cursor.getInt(0), cursor.getInt(1)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }


    public boolean isPin(int gid, int mid){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM "+Constant.PAY_TABLE+" WHERE gid = "+gid+" AND mid = "+mid, null);
        if (cursor.moveToFirst()) {
            do {
                return true;
            } while (cursor.moveToNext());
        } else{
            return false;
        }
    }
}
