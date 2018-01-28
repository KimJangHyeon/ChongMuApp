package com.example.owner.chongmuapp.Model.Data.Join;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.owner.chongmuapp.Common.Constant;

import java.util.ArrayList;

/**
 * Created by Owner on 2018-01-23.
 */

public class SQLiteGM extends SQLiteOpenHelper {
    private String Table = "GMjoin";
    private final String gTable = "group";
    private final String mTable = "member";

    public SQLiteGM (Context context, String db_name, SQLiteDatabase.CursorFactory factory, int version) {
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

    public void insert(int gid, int mid){
        ContentValues values = new ContentValues();
        values.put("gid", gid);
        values.put("mid", mid);

        this.getWritableDatabase().insert(Table, null, values);
    }

    public void delete (int mid, int gid) {
        String[] idArgs = {String.valueOf(mid), String.valueOf(gid)};
        this.getWritableDatabase().delete(Table, "mid = ? AND gid = ?", idArgs);
    }

    public void getMatchingGroup(int mid, ArrayList<String> matchingGid) {
        try{
            Cursor cursor = this.getReadableDatabase().rawQuery("SELECT gid FROM " + Constant.GROUP_TABLE +" WHERE mid ="+mid, null);
            if(cursor.moveToFirst()) {
                do {
                    matchingGid.add(String.valueOf(cursor.getInt(0)));
                } while (cursor.moveToNext());
            }
            cursor.close();
            return;
        }catch(Exception e){
            Log.e("GM return ", "false");
            return;
        }
    }
}
