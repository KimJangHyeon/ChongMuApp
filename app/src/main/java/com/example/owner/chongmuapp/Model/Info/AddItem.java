package com.example.owner.chongmuapp.Model.Info;

/**
 * Created by Owner on 2018-01-28.
 */

public class AddItem {
    int id;
    int isChecked;

    public AddItem(int id, int isChecked){
        this.id = id;
        this.isChecked = isChecked;
    }

    public int getGid(){ return id; }
    public int getIsChecked() {return isChecked; }
    public void setGid(int gid) {this.id = gid; }
    public void setIsChecked(int isChecked) { this.isChecked = isChecked; }

}
