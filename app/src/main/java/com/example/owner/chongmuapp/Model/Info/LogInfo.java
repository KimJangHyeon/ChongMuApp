package com.example.owner.chongmuapp.Model.Info;

/**
 * Created by Owner on 2018-01-22.
 */

public class LogInfo {
    //private String group;
    private int mid;
    private String name;
    private int payables;
    private boolean done;


    public LogInfo(/*String group,*/ int mid, String name, int payables, boolean done){
        //this.group = group;
        this.mid = mid;
        this.name = name;
        this.payables = payables;
        this.done = done;
    }
    /*public String getGroup(){
        return this.group;
    }*/
    public int getMid(){ return this.mid; }
    public String getName(){
        return this.name;
    }
    public int getPayables(){
        return this.payables;
    }
    public void setPayables(int payables) { this.payables = payables; }
    public boolean getDone(){
        return this.done;
    }
    public void setDone(boolean set){ this.done = set; }
}
