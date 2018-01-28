package com.example.owner.chongmuapp.Views.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.owner.chongmuapp.Model.Info.GroupInfo;
import com.example.owner.chongmuapp.Presenter.GMJoinPresenter;
import com.example.owner.chongmuapp.Presenter.GroupPresenter;
import com.example.owner.chongmuapp.R;
import com.example.owner.chongmuapp.Views.Adapter.GroupAdapter;

import java.util.ArrayList;

public class AddGroupActivity extends AppCompatActivity implements GroupPresenter.View, View.OnClickListener {
    private GroupPresenter groupPresenter;
    private GMJoinPresenter gmJoinPresenter;
    RecyclerView recyclerView;
    GroupAdapter groupAdapter = null;
    ArrayList<String> gidList;

    private int mid;
    private String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        recyclerView = (RecyclerView)findViewById(R.id.recy_group);
        groupPresenter = new GroupPresenter(this);
        groupPresenter.setContext(getApplicationContext());
        gidList = new ArrayList<>();
        groupPresenter.showAll();

        Intent intent = getIntent();
        mid = intent.getExtras().getInt("mid");
        mName = intent.getExtras().getString("mName");
    }
    /* View Interface */
    @Override
    public void showAdapter(GroupAdapter adapter) {
        recyclerView.setAdapter(adapter);
        groupPresenter.setAddMode(gmJoinPresenter.getMatchingGid(mid));
        adapter.setItemClick(new GroupAdapter.ItemClick(){
            @Override
            public void onClick(GroupInfo groupInfo, int position) {
                if(groupInfo.getPin() == 1){
                    Log.e("현재 핀 ", "1->0");
                    groupPresenter.addGroupIsChecked(position, 0);
                    groupInfo.setPin(0);
                    gidList.remove(String.valueOf(groupInfo.getId()));
                    Log.e("gidList  ", gidList.toString());
                }
                else if (groupInfo.getPin() == 0) {

                    Log.e("현재 핀 ", "0->1");
                    groupPresenter.addGroupIsChecked(position, 1);
                    gidList.add(String.valueOf(groupInfo.getId()));
                    Log.e("gidList  ", gidList.toString());
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
            case R.id.llayout_add:
                gmJoinPresenter.addArrInfo(gidList, mid);
                break;
        }

    }
}
