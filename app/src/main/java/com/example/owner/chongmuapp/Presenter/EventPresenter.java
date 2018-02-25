package com.example.owner.chongmuapp.Presenter;

import android.app.usage.UsageEvents;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.util.Log;

import com.example.owner.chongmuapp.Common.Constant;
import com.example.owner.chongmuapp.Model.Data.Basic.SQLiteEvent;
import com.example.owner.chongmuapp.Model.Info.EventInfo;
import com.example.owner.chongmuapp.Presenter.InfoHandler.EventHandler;
import com.example.owner.chongmuapp.Views.Adapter.EventAdapter;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Owner on 2018-01-30.
 */

public class EventPresenter {
    /* Presenter Setting */
    private View view;
    private Context context;
    private static EventPresenter memberPresenter = null;

    /* RecyclerView Setting */
    private static EventAdapter adapter;
    private EventHandler eventHandler;


    public interface View {
        void showAdapter(EventAdapter adapter);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public EventPresenter(View view, int gid){
        this.view = view;
//        eventHandler = new EventHandler().getInstance();
//        setReyclerView();
//        if (eventHandler.isInitialized() == false) {
//            getEventInfo(gid);
//        }
    }
    public EventPresenter(){
        eventHandler = new EventHandler().getInstance();
    }
    public void handlerInit(int gid){
        eventHandler = new EventHandler().getInstance();
        setReyclerView();
        if (eventHandler.isInitialized() == false) {
            getEventInfo(gid);
        }
    }

    private void setReyclerView(){
        adapter = new EventAdapter(eventHandler.getInstance().getInfoList());
        eventHandler.getInstance().attach(adapter);
    }

    /* ListView Method */
    public EventAdapter getAdapter() {
        return adapter;
    }
    public ArrayList<EventInfo> getItemList() { return eventHandler.getInstance().getInfoList(); }

    /* Logic */

    /* Activity Method */
    public void showAll() {
        view.showAdapter(adapter);
    }
    public void confirmDel(int position, int eid) {
        //adapter 에서 지우기
        eventHandler.getInstance().delInfo(position);

        //DB에서 지우기
        SQLiteEvent DB = new SQLiteEvent(context, Constant.DB_NAME, null, 4);
        DB.delete(eid);
        DB.close();
    }
    public void delEvent(int eid){
        eventHandler.getInstance().delEvent(eid);

        SQLiteEvent eventDB = new SQLiteEvent(context, Constant.DB_NAME, null, 4);
        eventDB.delete(eid);
    }
    public int confirmAdd(String eventName, int pay, int gid, int count){
        //DB
        EventInfo eventInfo;
        SQLiteEvent DB = new SQLiteEvent(context, Constant.DB_NAME, null, 4);
        //getTime
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  kk:mm:ss");
        String getTime = format.format(date);
        ///////////////
        DB.insert(gid, eventName, pay, getTime, count);
        eventInfo = DB.getInfo(gid, eventName, getTime);
        eventHandler.getInstance().addInfo(eventInfo);
        DB.close();
        return eventInfo.getId();
    }
    public void getEventInfo(int gid) {
        //db에서 데이터
        SQLiteEvent DB = new SQLiteEvent(context, Constant.DB_NAME, null, 4);
        DB.getInfoAll(eventHandler, gid);
        DB.close();

        eventHandler.getInstance().setInitialized(true);
    }

    public void setCount(int eid, int count, int increase){
        SQLiteEvent DB = new SQLiteEvent(context, Constant.DB_NAME, null, 4);
        DB.update(eid, count, increase);
        DB.close();

        eventHandler.getInstance().update(eid, count, increase);
    }
    public void refresh(){
        eventHandler.getInstance().refresh();
    }

}
