package com.example.owner.chongmuapp.Model.Data.Basic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.owner.chongmuapp.Common.Constant;
import com.example.owner.chongmuapp.Model.Info.GroupInfo;
import com.example.owner.chongmuapp.Presenter.InfoHandler.GroupHandler;

/**
 * Created by Owner on 2018-01-23.
 */

public class SQLiteGroup extends SQLiteOpenHelper{
    public SQLiteGroup (Context context, String db_name, SQLiteDatabase.CursorFactory factory, int version) {
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

    public void insert(String group){
        ContentValues values = new ContentValues();
        values.put("name", group);
        values.put("pin", 0);
        this.getWritableDatabase().insert(Constant.GROUP_TABLE, null, values);
    }

    public void delete (int gid) {
        String[] gidArgs = {String.valueOf(gid)};
        this.getWritableDatabase().delete(Constant.GROUP_TABLE, "gid = ?", gidArgs);
    }

    public GroupInfo getInfo (String gName) {
        try{
            GroupInfo groupInfo;
            Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + Constant.GROUP_TABLE + " WHERE name = '" + gName + "'", null);
            cursor.moveToFirst();
            groupInfo = new GroupInfo(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
            cursor.close();
            return groupInfo;
        }catch(Exception e){
            Log.e("error catch", e+"");
            return null;
        }
    }
    public boolean getInfoAll(GroupHandler groupHandler){
        try{
            Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + Constant.GROUP_TABLE +" ORDER BY name", null);
            if(cursor.moveToFirst()) {
                do {
                    groupHandler.getInstance().addInfo(new GroupInfo(cursor.getInt(0), cursor.getString(1), cursor.getInt(2)));
                } while (cursor.moveToNext());
            }
            cursor.close();
            return true;
        }catch(Exception e){
            Log.e("return ", "false");
            return false;
        }
    }
}
