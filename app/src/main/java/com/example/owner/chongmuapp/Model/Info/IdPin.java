package com.example.owner.chongmuapp.Model.Info;

/**
 * Created by Owner on 2018-02-03.
 */

public class IdPin {
    int id;
    int pin;
    public IdPin(int id, int pin){
        this.id = id;
        this.pin = pin;
    }

    public void setPin(int pin){
        this.pin = pin;
    }
    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }
    public int getPin(){
        return this.pin;
    }
}
