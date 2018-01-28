package com.example.owner.chongmuapp.Presenter;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.example.owner.chongmuapp.Common.Constant;
import com.example.owner.chongmuapp.Model.Data.Basic.SQLiteGroup;
import com.example.owner.chongmuapp.Model.Data.Basic.SQLiteMember;
import com.example.owner.chongmuapp.Model.Info.GroupInfo;
import com.example.owner.chongmuapp.Model.Info.MemberInfo;
import com.example.owner.chongmuapp.Presenter.InfoHandler.MemberHandler;
import com.example.owner.chongmuapp.Views.Adapter.GroupAdapter;
import com.example.owner.chongmuapp.Views.Adapter.MemberAdapter;

import java.util.ArrayList;

/**
 * Created by Owner on 2018-01-23.
 */

public class MemberPresenter {
    /* Presenter Setting */
    private View view;
    private Context context;
    private static MemberPresenter memberPresenter = null;

    /* RecyclerView Setting */
    private static MemberAdapter adapter;

    private MemberHandler memberHandler;

    public interface View {
        void showAdapter(MemberAdapter adapter);
    }

    public void setContext(Context context) {
        this.context = context;
    }


    /* Presenter Method */
    public MemberPresenter(View view) {
        this.view = view;
        memberHandler = new MemberHandler().getInstance();

        setReyclerView();

        if (memberHandler.isInitialized() == false) {
            getMemberInfo();
        }
    }

    public MemberPresenter() {}

    private void setReyclerView(){
        adapter = new MemberAdapter(memberHandler.getInstance().getInfoList());
        memberHandler.getInstance().attach(adapter);
    }

    public static MemberPresenter getInstance(){
        if(memberPresenter == null)
            memberPresenter = new MemberPresenter();
        return memberPresenter;
    }

    /* ListView Method */
    public MemberAdapter getAdapter() {
        return adapter;
    }
    public ArrayList<MemberInfo> getItemList() { return memberHandler.getInstance().getInfoList(); }

    /* Logic */

    /* Fragment Method */
    public void showAll() {
        view.showAdapter(adapter);
    }
    public void confirmDel(int position, int gid) {
        //adapter 에서 지우기
        memberHandler.getInstance().delInfo(position);

        //DB에서 지우기
        SQLiteMember DB = new SQLiteMember(context, Constant.DB_NAME, null, 4);
        DB.delete(gid);
        DB.close();
    }
    public int confirmAdd(String memberName){
        //DB
        MemberInfo memberInfo;
        SQLiteMember DB = new SQLiteMember(context, Constant.DB_NAME, null, 4);
        DB.insert(memberName);
        Log.e("DB.getInfo", DB.getInfo(memberName)+"");
        memberInfo = DB.getInfo(memberName);
        memberHandler.getInstance().addInfo(memberInfo);
        DB.close();
        return memberInfo.getId();
    }
    private void getMemberInfo() {
        //db에서 데이터
        SQLiteMember memberTable = new SQLiteMember(context, Constant.DB_NAME, null, 4);
        memberTable.getInfoAll(memberHandler);
        memberTable.close();

        memberHandler.getInstance().setInitialized(true);

    }
}
