package com.example.owner.chongmuapp.Model.Data.Basic;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.owner.chongmuapp.Common.Constant;

/**
 * Created by Owner on 2018-01-23.
 */

public class SQLiteEvent extends SQLiteOpenHelper{

    private String Table = "event";

    public SQLiteEvent (Context context, String db_name, SQLiteDatabase.CursorFactory factory, int version) {
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

    public boolean insert(int gid, String event, String datetime, int member_count){
        ContentValues values = new ContentValues();
        values.put("gid", gid);
        values.put("name", event);
        values.put("left", member_count);
        if(!datetime.equals("defaultTime")) values.put("datetime", datetime);

        if(this.getWritableDatabase().insert(Table, null, values) == -1) return false;
        return true;
    }

    public void delete (int eid) {
        String[] eidArgs = {String.valueOf(eid)};
        this.getWritableDatabase().delete(Table, "eid = ?", eidArgs);
    }
}
