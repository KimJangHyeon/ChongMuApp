package com.example.owner.chongmuapp.Model.Data.Basic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.owner.chongmuapp.Common.Constant;
import com.example.owner.chongmuapp.Model.Info.GroupInfo;
import com.example.owner.chongmuapp.Model.Info.IdPin;
import com.example.owner.chongmuapp.Model.Info.MemberInfo;
import com.example.owner.chongmuapp.Presenter.InfoHandler.GroupHandler;
import com.example.owner.chongmuapp.Presenter.InfoHandler.MemberHandler;

import java.util.ArrayList;

/**
 * Created by Owner on 2018-01-23.
 */

public class SQLiteMember extends SQLiteOpenHelper {
    public SQLiteMember (Context context, String db_name, SQLiteDatabase.CursorFactory factory, int version) {
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

    public void insert(String member){
        ContentValues values = new ContentValues();
        values.put("name", member);
        values.put("pin", 0);
        this.getWritableDatabase().insert(Constant.MEMBER_TABLE, null, values);
    }

    public void update(int mid, int pin){
        ContentValues values = new ContentValues();
        values.put("pin", pin);
        this.getWritableDatabase().update(Constant.MEMBER_TABLE, values, "mid=?", new String[] { String.valueOf(mid)});
    }


    public void delete (int mid) {
        String[] midArgs = {String.valueOf(mid)};
        this.getWritableDatabase().delete(Constant.MEMBER_TABLE, "mid = ?", midArgs);
    }

    public MemberInfo getInfo (String gName) {
        try{
            MemberInfo memberInfo;
            Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + Constant.MEMBER_TABLE + " WHERE name = '" + gName + "'", null);
            cursor.moveToFirst();
            memberInfo = new MemberInfo(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
            cursor.close();
            return memberInfo;
        }catch(Exception e){
            Log.e("error catch", e+"");
            return null;
        }
    }
    public boolean getInfoAll(MemberHandler memberHandler){
        try{

            Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + Constant.MEMBER_TABLE +" ORDER BY name", null);
            if(cursor.moveToFirst()) {
                do {
                    memberHandler.getInstance().addInfo(new MemberInfo(cursor.getInt(0), cursor.getString(1), cursor.getInt(2)));
                } while (cursor.moveToNext());
            }
            cursor.close();
            return true;
        }catch(Exception e){
            Log.e("return ", "false");
            return false;
        }
    }

    public void setMemPin(ArrayList<IdPin> idPinList, int increase){
        ContentValues values = new ContentValues();
        for(IdPin idPin: idPinList){
            values.put("pin", idPin.getPin()+ increase);
            this.getWritableDatabase().update(Constant.MEMBER_TABLE, values, "mid=?", new String[] { String.valueOf(idPin.getId()) });
        }
    }
}
