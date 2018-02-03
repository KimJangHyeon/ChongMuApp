package com.example.owner.chongmuapp.Model.Data.Basic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.owner.chongmuapp.Common.Constant;
import com.example.owner.chongmuapp.Model.Info.EventInfo;
import com.example.owner.chongmuapp.Model.Info.GroupInfo;
import com.example.owner.chongmuapp.Presenter.InfoHandler.EventHandler;

/**
 * Created by Owner on 2018-01-23.
 */

public class SQLiteEvent extends SQLiteOpenHelper{



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

    public boolean insert(int gid, String event, int pay, String datetime, int member_count){
        Log.e("pay", pay+"");
        ContentValues values = new ContentValues();
        values.put("gid", gid);
        values.put("name", event);
        values.put("pay", pay);
        values.put("left", member_count);
        if(!datetime.equals("defaultTime")) values.put("datetime", datetime);

        if(this.getWritableDatabase().insert(Constant.EVENT_TABLE, null, values) == -1) return false;
        return true;
    }
    public void update(int eid, int count, int increase){
        ContentValues values = new ContentValues();
        values.put("left",  count + increase);    //carNumber를 변경하고자 할때
        this.getWritableDatabase().update(Constant.EVENT_TABLE, values, "eid = ?", new String[] { String.valueOf(eid) });

    }

    public void delete (int eid) {
        String[] eidArgs = {String.valueOf(eid)};
        this.getWritableDatabase().delete(Constant.EVENT_TABLE, "eid = ?", eidArgs);
    }

    /*Activity logic*/
    public boolean getInfoAll(EventHandler eventHandler, int gid){
        try{
            Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + Constant.EVENT_TABLE +" WHERE gid = "+gid+" ORDER BY datetime", null);
            if(cursor.moveToFirst()) {
                do {
                    Log.e("get EVENT", "INFO");
                    eventHandler.getInstance().addInfo(new EventInfo(cursor.getInt(1), cursor.getString(2), cursor.getInt(3), cursor.getString(4), cursor.getInt(5)));
                } while (cursor.moveToNext());
            }
            cursor.close();
            return true;
        }catch(Exception e){
            Log.e("return ", "false");
            Log.e("DB error", e+"");
            return false;
        }
    }
    public EventInfo getInfo(int gid, String eventName, String getTime){
        try{
            EventInfo eventInfo;
            Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + Constant.EVENT_TABLE + " WHERE gid=" + gid + " AND name = '" + eventName + "' AND datetime = '"+getTime+"'", null);
            cursor.moveToFirst();
            eventInfo = new EventInfo(cursor.getInt(1), cursor.getString(2), cursor.getInt(3), cursor.getString(4), cursor.getInt(5));
            cursor.close();
            return eventInfo;
        }catch(Exception e){
            Log.e("error catch", e+"");
            return null;
        }
    }

}
