package com.example.owner.chongmuapp.Views.Activity;

import android.app.usage.UsageEvents;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.chongmuapp.Model.Info.EventInfo;
import com.example.owner.chongmuapp.Model.Info.MemberInfo;
import com.example.owner.chongmuapp.Presenter.EventPresenter;
import com.example.owner.chongmuapp.Presenter.GroupPresenter;
import com.example.owner.chongmuapp.Presenter.PayablesPresenter;
import com.example.owner.chongmuapp.R;
import com.example.owner.chongmuapp.Views.Adapter.EventAdapter;
import com.example.owner.chongmuapp.Views.Adapter.GroupAdapter;
import com.example.owner.chongmuapp.Views.Adapter.MemberAdapter;
import com.example.owner.chongmuapp.Views.Dialog.CustomDialog1;

public class EventActivity extends AppCompatActivity implements EventPresenter.View, View.OnClickListener{
    /*set presenter*/
    private EventPresenter eventPresenter;
    private PayablesPresenter payablesPresenter;

    /*set intent*/
    private int gid;
    private String gName;

    /*set widget*/
    RecyclerView recyclerView;
    TextView btn_add_txt;

    AlertDialog.Builder alert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Log.e("EVENT ACTIVITY", "ERROR");
        /*set widget*/
        recyclerView = (RecyclerView)findViewById(R.id.rcview_event);
        btn_add_txt = (TextView)findViewById(R.id.btn_add_txt);

        alert = new AlertDialog.Builder(EventActivity.this);

        setIntent();
        setPresenter();
        //init widget
        btn_add_txt.setOnClickListener(this);
    }

    public void setIntent(){
        Intent intent= getIntent();
        gid = intent.getExtras().getInt("gid");
        gName = intent.getExtras().getString("gName");
    }

    public void setPresenter(){
        eventPresenter = new EventPresenter(this, gid);
        payablesPresenter = new PayablesPresenter();

        eventPresenter.setContext(getApplicationContext());
        payablesPresenter.setContext(getApplicationContext());

        eventPresenter.handlerInit(gid);
        eventPresenter.showAll();
    }



    /* View Interface */
    @Override
    public void showAdapter(EventAdapter adapter) {
        recyclerView.setAdapter(adapter);
        eventPresenter.refresh();
        eventPresenter.getEventInfo(gid);
        adapter.setItemClick(new EventAdapter.ItemClick(){
            @Override
            public void onClick(EventInfo eventInfo, int position) {
                Intent intent = new Intent(EventActivity.this, PayablesActivity.class);
                intent.putExtra("gid", gid);
                intent.putExtra("eid", eventInfo.getId());
                intent.putExtra("isFixed", true);
                intent.putExtra("eName", eventInfo.getName());
                intent.putExtra("gName", gName);
                intent.putExtra("count", eventInfo.getCount());
                Toast.makeText(EventActivity.this, "eid: "+eventInfo.getId() +" count: " + eventInfo.getCount(),
                        Toast.LENGTH_LONG).show();

                startActivity(intent);
            }

            @Override
            public void onLongClick(EventInfo eventInfo, int position){
                final int pos = position;
                final int eid = eventInfo.getId();
                if(eventInfo.getCount() == 0){
                    alert.setTitle("really??");
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //확인

                            payablesPresenter.setmemIdPin(gid, eid, -1);

                            eventPresenter.confirmDel(pos, eid);
                            payablesPresenter.eventDel(eid);
                        }
                    });
                    alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //취소
                        }
                    });
                    alert.show();
                } else{
                    Toast.makeText(EventActivity.this, "pin 갯수가 0이 아님",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_add_txt:
                Intent intent = new Intent(EventActivity.this, EventAddMemberActivity.class);
                intent.putExtra("gid", gid);
                intent.putExtra("gName", gName);
                startActivity(intent);
                break;
        }
    }

}
