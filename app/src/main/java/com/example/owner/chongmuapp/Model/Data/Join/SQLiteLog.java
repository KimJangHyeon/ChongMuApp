package com.example.owner.chongmuapp.Model.Data.Join;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.owner.chongmuapp.Common.Constant;

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
                + " (gid INTEGER, eid INTEGER PRIMARY KEY AUTOINCREMENT, name STRING, datetime DATETIME DEFAULT CURRENT_TIMESTAMP, left INTEGER)"
        );

        //group & member table
        db.execSQL("CREATE TABLE if not exists " + Constant.GM_JOIN_TABLE
                + " (gid INTEGER NOT NULL, mid INTEGER NOT NULL, PRIMARY KEY (gid, mid) " +
                "FOREIGN KEY (gid) REFERENCES " + Constant.GROUP_TABLE
                +" (gid), "+
                "FOREIGN KEY (mid) REFERENCES " + Constant.MEMBER_TABLE
                +" (mid) );"
        );

        //payables table
        db.execSQL("CREATE TABLE if not exists " + Constant.PAY_TABLE
                + " (gid INTEGER, eid INTEGER, mid INTEGER, pay INTEGER, payables BOOLEAN) "
                + "FOREIGN KEY (mid) REFERENCES " + Constant.MEMBER_TABLE + " (mid) );"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        throw new UnsupportedOperationException("not implemented");
    }

    public void insert(int gid, int eid, int mid, int pay, boolean payables){
        ContentValues values = new ContentValues();
        values.put("gid", gid);
        values.put("eid", eid);
        values.put("mid", mid);
        values.put("pay", pay);
        values.put("payables", payables);
        this.getWritableDatabase().insert(Table, null, values);
    }

    public void delete(int gid, int eid, int mid){
        String[] idArgs = {String.valueOf(gid), String.valueOf(eid), String.valueOf(mid)};
        this.getWritableDatabase().delete(Table, "gid = ? AND eid = ? AND mid = ?", idArgs);
    }
}
