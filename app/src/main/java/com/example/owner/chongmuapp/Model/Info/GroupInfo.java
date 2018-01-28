package com.example.owner.chongmuapp.Model.Info;

/**
 * Created by Owner on 2018-01-22.
 */

public class GroupInfo {
    private int id;
    private String name;
    private int pin;

    public GroupInfo(int id, String name, int pin){
        this.id = id;
        this.name = name;
        this.pin = pin;
    }
    public int getId() { return this.id; }
    public String getName(){
        return this.name;
    }
    public int getPin() { return this.pin; }
    public void setPin(int pin) { this.pin = pin; }
}
