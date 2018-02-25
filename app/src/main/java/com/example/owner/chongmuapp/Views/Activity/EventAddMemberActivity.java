package com.example.owner.chongmuapp.Views.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.chongmuapp.Common.Constant;
import com.example.owner.chongmuapp.Model.Info.MemberInfo;
import com.example.owner.chongmuapp.Presenter.EventPresenter;
import com.example.owner.chongmuapp.Presenter.GMJoinPresenter;
import com.example.owner.chongmuapp.Presenter.MemberPresenter;
import com.example.owner.chongmuapp.Presenter.PayablesPresenter;
import com.example.owner.chongmuapp.R;
import com.example.owner.chongmuapp.Views.Adapter.MemberAdapter;

import java.lang.reflect.Array;
import java.lang.reflect.Member;
import java.util.ArrayList;

public class EventAddMemberActivity extends AppCompatActivity implements MemberPresenter.View, View.OnClickListener{

    MemberPresenter memberPresenter;
    EventPresenter eventPresenter;
    GMJoinPresenter gmJoinPresenter;

    //private AlertDialog.Builder alert;

    int eid;
    int gid;
    String gName;
    int count = 0;
    ArrayList<String> midList;
    ArrayList<String> nameList;

    RecyclerView recyclerView;
    TextView btn_apply_txt;
    EditText event_etxt;
    EditText pay_etxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_add_member);

        recyclerView = (RecyclerView) findViewById(R.id.recy_member);
        btn_apply_txt = (TextView) findViewById(R.id.btn_apply_txt);
        event_etxt = (EditText) findViewById(R.id.event_etxt);
        pay_etxt = (EditText) findViewById(R.id.pay_etxt);

        setIntent();
        setPresenter();
        //alert  = new AlertDialog.Builder(EventAddMemberActivity.this);
        midList = new ArrayList<>();
        nameList = new ArrayList<>();
        btn_apply_txt.setOnClickListener(this);
    }

    public void setPresenter(){
        memberPresenter = new MemberPresenter(this);
        eventPresenter = new EventPresenter();
        gmJoinPresenter = new GMJoinPresenter();

        memberPresenter.setContext(getApplicationContext());
        eventPresenter.setContext(getApplicationContext());
        gmJoinPresenter.setContext(getApplicationContext());

        memberPresenter.flashList();
        memberPresenter.showAll();
    }

    public void setIntent(){
        Intent intent = getIntent();
        gid = intent.getExtras().getInt("gid");
        gName = intent.getExtras().getString("gName");
    }
    @Override
    public void onBackPressed() {
        memberPresenter.loadList();

        super.onBackPressed();
    }
    @Override
    public void showAdapter(MemberAdapter adapter) {
        recyclerView.setAdapter(adapter);
        memberPresenter.setAddGroupJoin(gmJoinPresenter.getGroupJoin(gid));
        adapter.setItemClick(new MemberAdapter.ItemClick(){
            @Override
            public void onClick(MemberInfo memberInfo, int position) {
                if(memberInfo.getPin() == 1){
                    memberPresenter.addMemberIsChecked(position, 0);
                    memberInfo.setPin(0);
                    midList.remove(String.valueOf(memberInfo.getId()));
                    nameList.remove(memberInfo.getName());
                    count--;
                    //midList.remove(String.valueOf(memberInfo.getId()));
                }
                else if (memberInfo.getPin() == 0) {
                    memberPresenter.addMemberIsChecked(position, 1);
                    midList.add(String.valueOf(memberInfo.getId()));
                    nameList.add(memberInfo.getName());
                    count++;
                    //midList.add(String.valueOf(memberInfo.getId()));
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
                if(event_etxt.getText().toString().isEmpty()){
                    Toast.makeText(EventAddMemberActivity.this, Constant.EVENT_NAME_EMPTY,
                            Toast.LENGTH_LONG).show();
                    break;
                } else if (pay_etxt.getText().toString().isEmpty()){
                    Toast.makeText(EventAddMemberActivity.this, Constant.EVENT_PAY_EMPTY,
                            Toast.LENGTH_LONG).show();
                    break;
                } else if (midList.size()==0){
                    Toast.makeText(EventAddMemberActivity.this, Constant.EVENT_MEMBER_NULL,
                            Toast.LENGTH_LONG).show();
                    break;
                } else {
                    intentPayablesActivity(false);
                    break;
                }
        }
    }

    void intentPayablesActivity(boolean isFixed){
        //eid = eventPresenter.confirmAdd(event_etxt.getText().toString(), Integer.parseInt(pay_etxt.getText().toString()), gid, count);

        Intent intent = new Intent(EventAddMemberActivity.this, PayablesActivity.class);
        intent.putExtra("gid", gid);
        intent.putExtra("eName", event_etxt.getText().toString());
        intent.putExtra("ePay", Integer.parseInt(pay_etxt.getText().toString()));
        intent.putExtra("count", count);
        intent.putExtra("isFixed", isFixed);
        intent.putExtra("midList", midList);
        intent.putExtra("nameList", nameList);
        intent.putExtra("gName", gName);
        intent.putExtra("payables", Integer.parseInt(pay_etxt.getText().toString()));
        startActivity(intent);
    }
}
