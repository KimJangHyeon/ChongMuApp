package com.example.owner.chongmuapp.Presenter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;

import com.example.owner.chongmuapp.Common.Constant;
import com.example.owner.chongmuapp.Model.Data.Basic.SQLiteGroup;
import com.example.owner.chongmuapp.Model.Data.Basic.SQLiteMember;
import com.example.owner.chongmuapp.Model.Info.GroupInfo;
import com.example.owner.chongmuapp.Model.Info.MemberInfo;
import com.example.owner.chongmuapp.Presenter.InfoHandler.GroupHandler;
import com.example.owner.chongmuapp.Presenter.InfoHandler.MemberHandler;

/**
 * Created by Owner on 2018-01-23.
 */

public class DataPresenter {
    private View view;

    public DataPresenter(View view) { this.view = view; }
    public interface View {

    }

    /* DB Setting */
    private SQLiteGroup groupDB;
    private SQLiteMember memberDB;

    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public void loadDB() {
        loadGroup();
        loadMember();
    }

    public void loadGroup(){
        groupDB = new SQLiteGroup(context, Constant.DB_NAME, null, 4);
        GroupHandler.getInstance().refresh();

        Cursor cursor = groupDB.getReadableDatabase().rawQuery("SELECT * FROM " + Constant.GROUP_TABLE +" ORDER BY name", null);
        if(cursor.moveToFirst()) {
            do {
                GroupHandler.getInstance().addInfo(new GroupInfo(cursor.getInt(0), cursor.getString(1), cursor.getInt(2)));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void loadMember(){
        memberDB = new SQLiteMember(context, Constant.DB_NAME, null,4 );
        MemberHandler.getInstance().refresh();

        Cursor cursor = memberDB.getReadableDatabase().rawQuery("SELECT * FROM " + Constant.MEMBER_TABLE +" ORDER BY name", null);
        if(cursor.moveToFirst()) {
            do {
                MemberHandler.getInstance().addInfo(new MemberInfo(cursor.getInt(0), cursor.getString(1), cursor.getInt(2)));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
