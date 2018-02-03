package com.example.owner.chongmuapp.Presenter.InfoHandler;

import com.example.owner.chongmuapp.Model.Info.EventInfo;
import com.example.owner.chongmuapp.Presenter.Subject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Owner on 2018-01-30.
 */

public class EventHandler extends Subject{
    private static EventHandler eventHandler = null;
    private ArrayList<EventInfo> eventInfoList = new ArrayList<>();

    private boolean initialized = false;

    public EventHandler(){}

    public boolean isInitialized() {
        return initialized;
    }
    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public static EventHandler getInstance(){
        if(eventHandler == null)
            eventHandler = new EventHandler();
        return eventHandler;
    }

    public void refresh() { eventInfoList.clear(); }


    public ArrayList<EventInfo> getInfoList(){
        return eventInfoList;
    }

    public void delInfo(int position) {
        eventInfoList.remove(position);
        notifyObservers();
    }
    public void delEvent(int eid){
        for(EventInfo eventInfo: eventInfoList){
            if(eventInfo.getId() == eid){
                eventInfoList.remove(eventInfo);
                break;
            }
        }
        notifyObservers();
    }

    public void addInfo(EventInfo eventInfo) {
        eventInfoList.add(eventInfo);
        adapterSort();
        notifyObservers();
    }

    public void adapterSort(){
            Comparator<EventInfo> order = new Comparator<EventInfo>() {
                @Override
                public int compare(EventInfo o1, EventInfo o2) {
                    int ret;
                    if (o1.getDate().compareTo(o2.getDate()) < 0)
                        ret = -1;
                    else
                        ret = 1;
                    return ret;
                }
            };
            Collections.sort(eventInfoList, order);
    }

    public void update(int eid, int count, int increase){
        for(EventInfo eventInfo: eventInfoList){
            if(eventInfo.getId() == eid){
                eventInfo.setCount(count+increase);
            }
        }
        notifyObservers();
    }
}
