package com.example.owner.chongmuapp.Views.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.owner.chongmuapp.Model.Info.AddItem;
import com.example.owner.chongmuapp.Model.Info.GroupInfo;
import com.example.owner.chongmuapp.Model.Info.MemberInfo;
import com.example.owner.chongmuapp.Presenter.GMJoinPresenter;
import com.example.owner.chongmuapp.Presenter.MemberPresenter;
import com.example.owner.chongmuapp.R;
import com.example.owner.chongmuapp.Views.Adapter.GroupAdapter;
import com.example.owner.chongmuapp.Views.Adapter.MemberAdapter;

import java.util.ArrayList;

public class AddMemberActivity extends AppCompatActivity implements MemberPresenter.View, View.OnClickListener{
    //Presenter
    private MemberPresenter memberPresenter;
    private GMJoinPresenter gmJoinPresenter;

    //widget
    RecyclerView recyclerView;
    TextView textApply;

    //DB set array
    private int gid;
    private String gName;
    ArrayList<String> midList;
    ArrayList<AddItem> oldItemList;
    ArrayList<AddItem> newItemList;
    ArrayList<String> delMemberList;
    ArrayList<String> addMemberList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        recyclerView = (RecyclerView)findViewById(R.id.recy_member);
        textApply = (TextView) findViewById(R.id.btn_apply_txt);
        textApply.setOnClickListener(this);
        setDBArray();
        setIntent();
        setPresenter();
    }
    private void setIntent(){
        Intent intent = getIntent();
        gid = intent.getExtras().getInt("gid");
        gName = intent.getExtras().getString("gName");

    }
    private void setDBArray(){
        oldItemList = new ArrayList<>();
        newItemList = new ArrayList<>();
        delMemberList = new ArrayList<>();
        addMemberList = new ArrayList<>();
        midList = new ArrayList<>();
    }
    private void setPresenter(){
        gmJoinPresenter = new GMJoinPresenter();
        memberPresenter = new MemberPresenter(this);

        memberPresenter.setContext(getApplicationContext());
        gmJoinPresenter.setContext(getApplicationContext());

        memberPresenter.flashList();
        memberPresenter.showAll();
    }

    @Override
    public void onBackPressed() {
        memberPresenter.loadList();

        super.onBackPressed();
    }

    /* View Interface */
    @Override
    public void showAdapter(MemberAdapter adapter) {
        recyclerView.setAdapter(adapter);
        memberPresenter.setAddMode(gmJoinPresenter.getMatchingGid(gid), oldItemList);
        adapter.setItemClick(new MemberAdapter.ItemClick(){
            @Override
            public void onClick(MemberInfo memberInfo, int position) {
                if(memberInfo.getPin() == 1){
                    memberPresenter.addMemberIsChecked(position, 0);
                    memberInfo.setPin(0);
                    midList.remove(String.valueOf(memberInfo.getId()));
                }
                else if (memberInfo.getPin() == 0) {
                    memberPresenter.addMemberIsChecked(position, 1);
                    midList.add(String.valueOf(memberInfo.getId()));
                }
            }

            @Override
            public void onLongClick(MemberInfo memberInfo, int position){
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_apply_txt:
                setNewItemList(memberPresenter.getItemList());
                setDelAddList();
                gmJoinPresenter.addArrInfo(addMemberList, gid, false);
                gmJoinPresenter.delArrInfo(delMemberList, gid, false);
                //memberPresenter.debugLogSave();
                forDelAdd();
                Intent intent = new Intent(AddMemberActivity.this, StartActivity.class);
                //intent fragment로 history지우기
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
                    addMemberList.add(String.valueOf(gid));
                else
                    delMemberList.add(String.valueOf(gid));
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
        for(int i = 0;i< addMemberList.size();i++){
            Log.e("add list", addMemberList.toString());
        }
        for (int i = 0;i< delMemberList.size();i++){
            Log.e("del list", delMemberList.toString());
        }
    }


    public void setNewItemList(ArrayList<MemberInfo> memberList){
        for(MemberInfo memberInfo: memberList){
            newItemList.add(new AddItem(memberInfo.getId(), memberInfo.getPin()));
        }
    }
}
