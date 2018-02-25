package com.example.owner.chongmuapp.Views.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.EventLog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.chongmuapp.Debug;
import com.example.owner.chongmuapp.Model.Info.LogInfo;
import com.example.owner.chongmuapp.Presenter.EventPresenter;
import com.example.owner.chongmuapp.Presenter.PayablesPresenter;
import com.example.owner.chongmuapp.R;
import com.example.owner.chongmuapp.Views.Adapter.PayablesAdapter;

import java.util.ArrayList;

public class PayablesActivity extends AppCompatActivity implements PayablesPresenter.View, View.OnClickListener{
    PayablesPresenter payablesPresenter;
    EventPresenter eventPresenter;

    int gid;
    int eid;
    int payables;
    int payableAvg;
    int oldPayables;
    int allSelectedPay;
    //int payAvg;
    int leftMember;
    boolean isFixed;
    String gName;
    /*isFix*/
    String eName;
    int count;
    int increase = 0;
    /*is not fix*/
    ArrayList<String> midList;
    ArrayList<String> nameList;
    int ePay;
    //interface

    //logic


    Debug debug;
    RecyclerView recyclerView;
    TextView event_txt;
    TextView apply_txt;
    private AlertDialog.Builder alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payables);

        recyclerView = (RecyclerView) findViewById(R.id.recy_paybles);
        event_txt = (TextView) findViewById(R.id.event_txt);
        apply_txt = (TextView) findViewById(R.id.btn_apply_txt);
        apply_txt.setOnClickListener(this);
        debug = new Debug();
        midList = new ArrayList<>();
        nameList = new ArrayList<>();
        alert  = new AlertDialog.Builder(PayablesActivity.this);
        setIntent();
        setEventText();
        setPresent();

    }
    void setEventText(){
        if(!isFixed) {
            event_txt.setText(payables - payables/nameList.size() * leftMember+"");
        } else{
            event_txt.setText(eName);
        }
    }


    protected void onPause(){
        super.onPause();
        Log.e("onPause", "실행");

        Toast.makeText(PayablesActivity.this, " too", Toast.LENGTH_SHORT);
    }
    protected  void onDestroy(){
        super.onDestroy();
        Log.e("onDestroy", "실행");
        Toast.makeText(PayablesActivity.this, " to", Toast.LENGTH_SHORT);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(isFixed) {
            eventPresenter.setCount(eid, count, increase);
        } else{
            eventPresenter.delEvent(eid);
        }
    }

    private void setPresent(){
        eventPresenter = new EventPresenter();
        eventPresenter.setContext(getApplicationContext());
        if(isFixed) {
            payablesPresenter = new PayablesPresenter(this, gid, eid);
        } else
            payablesPresenter = new PayablesPresenter(this, midList, nameList, payables);

        payablesPresenter.setContext(getApplicationContext());
        payablesPresenter.showAll();
    }

     public void setIntent(){
         Intent intent = getIntent();
         gid = intent.getExtras().getInt("gid");
         eName =  intent.getExtras().getString("eName");
         isFixed = intent.getExtras().getBoolean("isFixed");
         count = intent.getExtras().getInt("count");
         if(!isFixed) {
             midList = (ArrayList<String>) getIntent().getSerializableExtra("midList");
             nameList = (ArrayList<String>) getIntent().getSerializableExtra("nameList");
             payables = intent.getExtras().getInt("payables");
             ePay = intent.getExtras().getInt("ePay");
             //payAvg = payables/nameList.size();
             leftMember = nameList.size();
         } else{
             gName = intent.getExtras().getString("gName");
             eid = intent.getExtras().getInt("eid");
         }

     }

    /* View Interface */
    @Override
    public void showAdapter(PayablesAdapter adapter) {
        recyclerView.setAdapter(adapter);
        payablesPresenter.refresh();
        Log.e("BEFOE", "setInfoAll");
        if(isFixed) {
            payablesPresenter.setInfoAll(gid, eid);

        }
        else {
            Log.e("nameList", nameList.toString());
            Log.e("payables", payables+"");
            payablesPresenter.adapterAdd(midList, nameList, payables);

        }

        if(isFixed) {
            adapter.setItemClick(new PayablesAdapter.ItemClick() {
                @Override
                public void onClick(final LogInfo logInfo, int position) {
                    final LogInfo finalLogInfo = logInfo;
                    final int finalPosition = position;
                    alert.setTitle("really??");
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(!finalLogInfo.getDone()) increase--;
                            else increase ++;
                            payablesPresenter.infoDone(gid, eid, finalLogInfo.getMid(), finalPosition);
                        }
                    });
                    alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //취소
                        }
                    });
                    alert.show();

                }

                @Override
                public void onLongClick(LogInfo eventInfo, int position) {

                }
            });
            adapter.setEditorAction(new PayablesAdapter.EditorAction() {
                @Override
                public void onClickDone(LogInfo logInfo, int position, int pays) {
                }
            });
        } else{
            adapter.setItemClick(new PayablesAdapter.ItemClick() {
                @Override
                public void onClick(LogInfo eventInfo, int position) {
                }

                @Override
                public void onLongClick(LogInfo eventInfo, int position) {

                }
            });
            adapter.setEditorAction(new PayablesAdapter.EditorAction() {
                @Override
                public void onClickDone(LogInfo logInfo, int position, int pays) {
                    oldPayables = logInfo.getPayables();
                    if(logInfo.getDone()){
                        payablesPresenter.changeSelecedPay(oldPayables, pays);
                        allSelectedPay = payablesPresenter.allSelectedPay();
                        logInfo.setPayables(pays);
                        payableAvg = (payables - allSelectedPay)/leftMember;
                        payablesPresenter.setAvg(payableAvg);
                        event_txt.setText((payables - (allSelectedPay + leftMember * payableAvg))+"");
                    } else{
                        payablesPresenter.addSelectedPay(pays);
                        if (--leftMember == 0) {

                        } else {
                            logInfo.setDone(true);
                            allSelectedPay = payablesPresenter.allSelectedPay();
                            logInfo.setPayables(pays);
                            payableAvg = (payables - allSelectedPay)/leftMember;
                            payablesPresenter.setAvg(payableAvg);
                            event_txt.setText((payables - (allSelectedPay + leftMember * payableAvg))+"");
                        }
                    }
                }
            });
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_apply_txt:
                if(!isFixed) {
                    eid = eventPresenter.confirmAdd(eName, ePay, gid, count);
                    payablesPresenter.flushAdapter(gid, eid);
                    payablesPresenter.setmemIdPin(gid, eid, 1);
                    payablesPresenter.setgroupIdPin(gid, 1);
                }
                else
                    eventPresenter.setCount(eid, count, increase);

                Intent intent = new Intent(PayablesActivity.this, StartActivity.class);
                intent.addFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;

        }
    }
}
