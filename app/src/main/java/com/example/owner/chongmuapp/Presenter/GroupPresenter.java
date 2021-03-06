package com.example.owner.chongmuapp.Presenter;

import android.content.Context;


import com.example.owner.chongmuapp.Common.Constant;
import com.example.owner.chongmuapp.Model.Data.Basic.SQLiteGroup;
import com.example.owner.chongmuapp.Model.Info.GroupInfo;
import com.example.owner.chongmuapp.Model.Info.AddItem;
import com.example.owner.chongmuapp.Presenter.InfoHandler.GroupHandler;
import com.example.owner.chongmuapp.Views.Adapter.GroupAdapter;

import java.util.ArrayList;

/**
 * Created by Owner on 2018-01-23.
 */

public class GroupPresenter {
    /* Presenter Setting */
    private View view;
    private Context context;
    private static GroupPresenter groupPresenter = null;

    /* RecyclerView Setting */
    private static GroupAdapter adapter;

    private GroupHandler groupHandler;
    /* View Method */
    public interface View {
        void showAdapter(GroupAdapter adapter);
    }
    public void setContext(Context context) {
        this.context = context;
    }

    /* Presenter Method */
    public GroupPresenter(View view) {
        this.view = view;
        groupHandler = new GroupHandler().getInstance();
        setReyclerView();

        if (groupHandler.isInitialized() == false) {
            getGroupInfo();
        }
    }
    public GroupPresenter() {}

    private void setReyclerView(){
        adapter = new GroupAdapter(groupHandler.getInstance().getInfoList());
        groupHandler.getInstance().attach(adapter);
    }

    /* ListView Method */
    public GroupAdapter getAdapter() {
        return adapter;
    }
    public ArrayList<GroupInfo> getItemList() { return groupHandler.getInstance().getInfoList(); }

    /* Logic */

    /* Fragment Method */
    public void showAll() {
        view.showAdapter(adapter);
    }
    public void confirmDel(int position, int gid) {
        //adapter 에서 지우기
        groupHandler.getInstance().delInfo(position);

        //DB에서 지우기
        SQLiteGroup DB = new SQLiteGroup(context, Constant.DB_NAME, null, 4);
        DB.delete(gid);
        DB.close();
    }
    public int confirmAdd(String groupName){
        //DB
        GroupInfo groupInfo;
        SQLiteGroup DB = new SQLiteGroup(context, Constant.DB_NAME, null, 4);
        DB.insert(groupName);

        groupInfo = DB.getInfo(groupName);
        groupHandler.getInstance().addInfo(groupInfo);
        DB.close();
        return groupInfo.getId();
    }
    private void getGroupInfo() {
        //db에서 데이터
        SQLiteGroup groupDB = new SQLiteGroup(context, Constant.DB_NAME, null, 4);
        groupDB.getInfoAll(groupHandler);
        groupDB.close();

        groupHandler.getInstance().setInitialized(true);
    }

    public void setAddMode(ArrayList<String> gidList, ArrayList<AddItem> groupList){
        for(int i =0;i<groupHandler.getInstance().getInfoList().size();i++){
            groupHandler.getInstance().getInfoList().get(i).setPin(0);
            groupList.add(new AddItem(groupHandler.getInstance().getInfoList().get(i).getId(), 0));
        }

        for(String gidStr: gidList) {
            for (int i = 0; i < groupHandler.getInstance().getInfoList().size(); i++) {
                if(groupHandler.getInstance().getInfoList().get(i).getId() == Integer.parseInt(gidStr)) {
                    groupHandler.getInstance().getInfoList().get(i).setPin(1);
                    groupList.get(i).setIsChecked(1);
                    break;
                }
            }
        }
        groupHandler.getInstance().notifyObservers();
    }
    public boolean addGroupIsChecked(int pos, int isCheck){
        groupHandler.getInstance().isChecked(pos, isCheck);
        return true;
    }
    public void flashList(){
        groupHandler.getInstance().flashGroupInfoList();
    }
    public void loadList(){
        groupHandler.getInstance().loadGroupInfoList();
    }

    public void debugLogSave(){
        groupHandler.debugLogSaver();
    }
}
