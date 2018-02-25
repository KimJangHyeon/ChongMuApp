package com.example.owner.chongmuapp.Presenter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.owner.chongmuapp.Common.Constant;
import com.example.owner.chongmuapp.Debug;
import com.example.owner.chongmuapp.Model.Data.Basic.SQLiteGroup;
import com.example.owner.chongmuapp.Model.Data.Basic.SQLiteMember;
import com.example.owner.chongmuapp.Model.Data.Join.SQLiteLog;
import com.example.owner.chongmuapp.Model.Info.IdPin;
import com.example.owner.chongmuapp.Model.Info.LogInfo;
import com.example.owner.chongmuapp.Presenter.InfoHandler.GroupHandler;
import com.example.owner.chongmuapp.Presenter.InfoHandler.LogHandler;
import com.example.owner.chongmuapp.Presenter.InfoHandler.MemberHandler;
import com.example.owner.chongmuapp.Views.Adapter.PayablesAdapter;

import java.util.ArrayList;

/**
 * Created by Owner on 2018-01-31.
 */

public class PayablesPresenter {
    /* Presenter Setting */
    private View view;
    private Context context;
    private static PayablesPresenter payablesPresenter = null;

    /* RecyclerView Setting */
    private static PayablesAdapter adapter;

    private LogHandler logHandler;
    private MemberHandler memberHandler;
    private GroupHandler groupHandler;
    Debug debug;

    //logic
    ArrayList<Integer> selectedPay;

    public interface View {
        void showAdapter(PayablesAdapter adapter);
    }


    public void setContext(Context context) {
        this.context = context;
    }


    /* Presenter Method */
    public PayablesPresenter(View view, int gid, int eid) {
        this.view = view;
        logHandler = new LogHandler().getInstance();
        memberHandler = new MemberHandler().getInstance();
        groupHandler = new GroupHandler().getInstance();
        setReyclerView(true);

        if (logHandler.isInitialized() == false) {
            //setInfoAll(gid, eid);
        } else {
            //setInfoAll(gid, eid);
        }
    }

    public PayablesPresenter(View view, ArrayList<String> midList, ArrayList<String> nameList, int payables) {
        this.view = view;
        logHandler = new LogHandler().getInstance();
        memberHandler = new MemberHandler().getInstance();
        groupHandler = new GroupHandler().getInstance();
        setReyclerView(false);

        selectedPay = new ArrayList<>();

        if (logHandler.isInitialized() == false) {
            adapterAdd(midList, nameList, payables);
        }else{
            //DB에서 가져오기
            adapterAdd(midList, nameList, payables);
        }
    }

    public PayablesPresenter() {
        groupHandler = new GroupHandler().getInstance();
        memberHandler = new MemberHandler().getInstance();
    }

    private void setReyclerView(boolean isFixed){
        adapter = new PayablesAdapter(logHandler.getInstance().getInfoList(), isFixed);
        logHandler.getInstance().attach(adapter);
    }

    public static PayablesPresenter getInstance(){
        if(payablesPresenter == null)
            payablesPresenter = new PayablesPresenter();
        return payablesPresenter;
    }

    /* ListView Method */
    public PayablesAdapter getAdapter() {
        return adapter;
    }
    public ArrayList<LogInfo> getItemList() { return logHandler.getInstance().getInfoList(); }

        /* Logic */

    /* Fragment Method */
    public void showAll() {
        view.showAdapter(adapter);
    }
    public void confirmDelAll(ArrayList<String> midList, int eid) {
        //adapter 에서 지우기
//        logHandler.getInstance().delInfo();
        logHandler.getInstance().refresh();
        //DB에서 지우기
        SQLiteLog DB = new SQLiteLog(context, Constant.DB_NAME, null, 4);
        DB.delete(midList, eid);
        DB.close();
    }
//    public int confirmAdd(String memberName){
//        //DB
//        LogInfo memberInfo;
//        SQLiteLog DB = new SQLiteLog(context, Constant.DB_NAME, null, 4);
//        DB.insert(memberName);
//        memberInfo = DB.getInfo(memberName);
//        memberHandler.getInstance().addInfo(memberInfo);
//        DB.close();
//        return memberInfo.getId();
//    }

    public void adapterAdd(ArrayList<String> midList, ArrayList<String> nameList, int payables){
        debug = new Debug();
        Log.e("nameList in adap add", nameList+"");
        Log.e("midList in adap add", midList + "");
        for(int i =0;i<midList.size();i++){
            logHandler.getInstance().addInfo(new LogInfo(Integer.parseInt(midList.get(i)), nameList.get(i), payables/midList.size(), false));

        }
        debug.LogList("Log handler", logHandler.getInstance().getInfoList());
    }

//
//    public boolean addMemberIsChecked(int pos, int isCheck){
//        memberHandler.getInstance().isChecked(pos, isCheck);
//        return true;
//    }

    //activity logic
    public void setAvg(int newAvg){
        logHandler.getInstance().setAvg(newAvg);
    }

    public void addSelectedPay(int pay){
        selectedPay.add(pay);
    }

    public int allSelectedPay(){
        int result = 0;
        for(int i= 0;i<selectedPay.size();i++){
            result = result+ selectedPay.get(i).intValue();
        }
        return result;
    }

    public void changeSelecedPay(int old, int _new){
        for(int i= 0;i<selectedPay.size();i++){
            if(selectedPay.get(i).intValue() == old){
                selectedPay.set(i, _new);
                break;
            }
        }
    }

    public void flushAdapter(int gid, int eid){
        SQLiteLog logDB = new SQLiteLog(context, Constant.DB_NAME, null, 4);
        logDB.insertAll(gid, eid, logHandler.getInstance().getInfoList());
        logDB.close();
    }

    public void setInfoAll(int gid, int eid){
        SQLiteLog logDB = new SQLiteLog(context, Constant.DB_NAME, null, 4);
        logDB.getInfoAll(gid, eid, logHandler);
        logDB.close();

        logHandler.getInstance().setInitialized(true);
    }

    public void setmemIdPin(int gid, int eid, int increase){
        Log.e("pin", gid+" "+eid);
        ArrayList<IdPin> idPinList;
        SQLiteLog logDB = new SQLiteLog(context, Constant.DB_NAME, null, 4);
        SQLiteMember memDB = new SQLiteMember(context, Constant.DB_NAME, null, 4);

        idPinList = logDB.getmemIdPin(gid, eid);

        Log.e("idPinList", idPinList.size()+"");
        memDB.setMemPin(idPinList, increase);
        memberHandler.setPin(idPinList, increase);
    }
    public void setgroupIdPin(int gid, int increase){
        int oldPin;
        SQLiteGroup groupDB = new SQLiteGroup(context, Constant.DB_NAME, null, 4);
        oldPin = groupDB.getPin(gid);
        groupDB.setGroupPin(gid, oldPin, increase);
        groupHandler.setPin(gid, increase);
    }


    //activity fixed
    public void infoDone(int gid, int eid, int mid , int position){
        LogInfo logInfo = logHandler.getInstance().setDone(position);
        SQLiteLog logDB = new SQLiteLog(context, Constant.DB_NAME, null, 4);
        logDB.update(gid, eid, mid, logInfo.getDone());
        logDB.close();
    }

    public void refresh(){
        logHandler.refresh();
    }

    //event Activity
    public void eventDel(int eid){
        SQLiteLog logDB = new SQLiteLog(context, Constant.DB_NAME, null, 4);
        logDB.eventDel(eid);
        logDB.close();
    }

    public boolean isPin(int gid, int mid) {
        Boolean re;
        SQLiteLog logDB = new SQLiteLog(context, Constant.DB_NAME, null, 4);
        re = logDB.isPin(gid, mid);
        logDB.close();
        return re;
    }



}
