package com.example.owner.chongmuapp.Views.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.chongmuapp.Common.Constant;
import com.example.owner.chongmuapp.Model.Info.GroupInfo;
import com.example.owner.chongmuapp.Model.Info.AddItem;
import com.example.owner.chongmuapp.Presenter.GMJoinPresenter;
import com.example.owner.chongmuapp.Presenter.GroupPresenter;
import com.example.owner.chongmuapp.Presenter.PayablesPresenter;
import com.example.owner.chongmuapp.R;
import com.example.owner.chongmuapp.Views.Adapter.GroupAdapter;

import java.util.ArrayList;

public class AddGroupActivity extends AppCompatActivity implements GroupPresenter.View, View.OnClickListener {
    private GroupPresenter groupPresenter;
    private GMJoinPresenter gmJoinPresenter;
    private PayablesPresenter payablesPresenter;

    RecyclerView recyclerView;
    GroupAdapter groupAdapter = null;
    ArrayList<String> gidList;
    TextView textApply;
    private int mid;
    private String mName;
    ArrayList<AddItem> oldItemList;
    ArrayList<AddItem> newItemList;
    ArrayList<String> delGroupList;
    ArrayList<String> addGroupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        oldItemList = new ArrayList<>();
        newItemList = new ArrayList<>();
        delGroupList = new ArrayList<>();
        addGroupList = new ArrayList<>();
        gidList = new ArrayList<>();

        //set Intent arr
        Intent intent = getIntent();
        mid = intent.getExtras().getInt("mid");
        mName = intent.getExtras().getString("mName");

        //widget
        recyclerView = (RecyclerView)findViewById(R.id.recy_group);
        textApply = (TextView) findViewById(R.id.btn_apply_txt);
        textApply.setOnClickListener(this);

        //set Presenter
        gmJoinPresenter = new GMJoinPresenter();
        groupPresenter = new GroupPresenter(this);
        payablesPresenter = new PayablesPresenter();

        groupPresenter.setContext(getApplicationContext());
        gmJoinPresenter.setContext(getApplicationContext());
        payablesPresenter.setContext(getApplicationContext());

        groupPresenter.flashList();
        groupPresenter.showAll();
    }

    @Override
    public void onBackPressed() {
        groupPresenter.loadList();

        super.onBackPressed();
    }

    /* View Interface */
    @Override
    public void showAdapter(GroupAdapter adapter) {
        recyclerView.setAdapter(adapter);
//        Intent intent = getIntent();
//        mid = intent.getExtras().getInt("mid");
//        mName = intent.getExtras().getString("mName");
        Log.e("showAdapter mid", mid+"");
        groupPresenter.setAddMode(gmJoinPresenter.getMatchingGid(mid), oldItemList);
        adapter.setItemClick(new GroupAdapter.ItemClick(){
            @Override
            public void onClick(GroupInfo groupInfo, int position) {
                if(!payablesPresenter.isPin(groupInfo.getId(), mid)){
                    if(groupInfo.getPin() == 1){
                        groupPresenter.addGroupIsChecked(position, 0);
                        groupInfo.setPin(0);
                        gidList.remove(String.valueOf(groupInfo.getId()));
                    }
                    else if (groupInfo.getPin() == 0) {
                        groupPresenter.addGroupIsChecked(position, 1);
                        gidList.add(String.valueOf(groupInfo.getId()));
                    }
                } else{
                    Toast.makeText(AddGroupActivity.this, Constant.MEM_PINED, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onLongClick(GroupInfo groupInfo, int position){
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_apply_txt:
                setNewItemList(groupPresenter.getItemList());
                setDelAddList();
                gmJoinPresenter.addArrInfo(addGroupList, mid, true);
                gmJoinPresenter.delArrInfo(delGroupList, mid, true);
                groupPresenter.debugLogSave();
                forDelAdd();
                Intent intent = new Intent(AddGroupActivity.this, StartActivity.class);
                //intent fragment로 history지우기
                intent.addFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }
    public void setDelAddList(){
        int gid;
        for(int i = 0;i<oldItemList.size();i++){

            if(oldItemList.get(i).getIsChecked() != newItemList.get(i).getIsChecked()){
                gid = oldItemList.get(i).getGid();
                if(oldItemList.get(i).getIsChecked() == 0)
                    addGroupList.add(String.valueOf(gid));
                else
                    delGroupList.add(String.valueOf(gid));
            }
        }
    }

    public void forLogList(){
        for(int i =0;i<oldItemList.size();i++) {
            Log.e("old list gid", oldItemList.get(i).getGid()+"");
            Log.e("old list is checked", oldItemList.get(i).getIsChecked()+"");
            Log.e("new list gid", newItemList.get(i).getGid()+"");
            Log.e("new list is checked", newItemList.get(i).getIsChecked()+"");
        }
    }

    public void forDelAdd(){
        for(int i = 0;i< addGroupList.size();i++){
            Log.e("add list", addGroupList.toString());
        }
        for (int i = 0;i< delGroupList.size();i++){
            Log.e("del list", delGroupList.toString());
        }
    }


    public void setNewItemList(ArrayList<GroupInfo> groupList){
        for(GroupInfo groupInfo: groupList){
            newItemList.add(new AddItem(groupInfo.getId(), groupInfo.getPin()));
        }
    }
}
