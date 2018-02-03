package com.example.owner.chongmuapp.Presenter;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.example.owner.chongmuapp.Common.Constant;
import com.example.owner.chongmuapp.Model.Data.Basic.SQLiteMember;
import com.example.owner.chongmuapp.Model.Info.AddItem;
import com.example.owner.chongmuapp.Model.Info.MemberInfo;
import com.example.owner.chongmuapp.Presenter.InfoHandler.MemberHandler;
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
        Log.e("MemberPresenter", "set adapter");
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

    /*Activity method*/
    public void setAddMode(ArrayList<String> gidList, ArrayList<AddItem> memberList){
        for(int i =0;i<memberHandler.getInstance().getInfoList().size();i++){
            memberHandler.getInstance().getInfoList().get(i).setPin(0);
            memberList.add(new AddItem(memberHandler.getInstance().getInfoList().get(i).getId(), 0));
        }

        for(String gidStr: gidList) {
            for (int i = 0; i < memberHandler.getInstance().getInfoList().size(); i++) {
                if(memberHandler.getInstance().getInfoList().get(i).getId() == Integer.parseInt(gidStr)) {
                    memberHandler.getInstance().getInfoList().get(i).setPin(1);
                    memberList.get(i).setIsChecked(1);
                    break;
                }
            }
        }
        memberHandler.getInstance().notifyObservers();
    }
    public boolean addMemberIsChecked(int pos, int isCheck){
        memberHandler.getInstance().isChecked(pos, isCheck);
        return true;
    }

    public void flashList(){
        memberHandler.getInstance().flashInfoList();
    }
    public void loadList(){
        memberHandler.getInstance().loadInfoList();
    }

    public void setAddGroupJoin(ArrayList<MemberInfo> memberList){
        memberHandler.getInstance().refresh();
        memberHandler.getInstance().addArrInfo(memberList);
    }

    public void setPin(int mid, int pin){
        SQLiteMember memDB = new SQLiteMember(context, Constant.DB_NAME, null, 4);
        memDB.update(mid, pin);
        memDB.close();
    }
}
