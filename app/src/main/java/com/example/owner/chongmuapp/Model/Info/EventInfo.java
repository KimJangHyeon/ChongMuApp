package com.example.owner.chongmuapp.Model.Info;

/**
 * Created by Owner on 2018-01-24.
 */

public class EventInfo {
    int id;
    String name;
    int pay;
    String date;
    int count;

    public EventInfo(int id, String name, int pay, String date, int count){
        this.id = id;
        this.name = name;
        this.pay = pay;
        this.date = date;
        this.count = count;
    }

    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getDate(){
        return date;
    }
    public int getCount(){
        return count;
    }
    public void setCount(int count){
        this.count = count;
    }
    public int getPay(){ return pay; }
}
