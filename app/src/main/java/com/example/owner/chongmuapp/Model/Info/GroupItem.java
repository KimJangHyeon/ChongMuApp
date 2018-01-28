package com.example.owner.chongmuapp.Model.Info;

/**
 * Created by Owner on 2018-01-28.
 */

public class GroupItem {
    int gid;
    int isChecked;

    public GroupItem(int gid, int isChecked){
        this.gid = gid;
        this.isChecked = isChecked;
    }

    public int getGid(){ return gid; }
    public int getIsChecked() {return isChecked; }
    public void setGid(int gid) {this.gid = gid; }
    public void setIsChecked(int isChecked) { this.isChecked = isChecked; }

}
