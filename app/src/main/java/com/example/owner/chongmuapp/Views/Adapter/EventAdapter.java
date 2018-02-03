package com.example.owner.chongmuapp.Views.Adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.owner.chongmuapp.Model.Info.EventInfo;
import com.example.owner.chongmuapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Owner on 2018-01-30.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private ArrayList<EventInfo> data;

    //아이템 클릭 콜백 등록
    private ItemClick itemClick;

    public interface ItemClick {
        public void onClick(EventInfo groupInfo, int position);
        public void onLongClick(EventInfo groupInfo, int position);
    }

    //아이템 클릭 시 실행될 함수
    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public EventAdapter(ArrayList<EventInfo> itemList) {
        if (itemList == null) {
            data = new ArrayList<>();
        } else {
            data = itemList;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView event_txt;
        private TextView date_txt;
        private TextView pay_txt;
        private LinearLayout item_bg;
        View view;
        public ViewHolder(View view) {
            super(view);
            this.view = view;
            pay_txt = (TextView) view.findViewById(R.id.pay_txt);
            event_txt = (TextView) view.findViewById(R.id.event_txt);
            date_txt = (TextView) view.findViewById(R.id.date_txt);
            item_bg = (LinearLayout) view.findViewById(R.id.item_bg);
        }
    }

    public void sortItem() {
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
        Collections.sort(data, order);
        notifyDataSetChanged();

    }

    public void addItem(EventInfo eventInfo) {
        data.add(eventInfo);
        sortItem();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        position = position%data.size();
        String[] parsingDate = data.get(position).getDate().split("  ");

        final EventInfo dt = data.get(position);
        holder.event_txt.setText(data.get(position).getName() /*+" (" + data.get(position).getPin()+")"*/);
        holder.date_txt.setText(parsingDate[0]+"\n"+parsingDate[1]);
        holder.pay_txt.setText(String.valueOf(data.get(position).getPay())+" 원");
        //add 이면
        if(data.get(position).getCount()==0){
            holder.item_bg.setBackgroundResource(R.drawable.menubtn_mint_square);
            holder.event_txt.setTextColor(Color.WHITE);
            holder.pay_txt.setTextColor(Color.WHITE);
            holder.date_txt.setTextColor(Color.WHITE);
        }
        else{
            holder.item_bg.setBackgroundResource(R.drawable.item_white_square);
            holder.event_txt.setTextColor(Color.BLACK);
            holder.pay_txt.setTextColor(Color.BLACK);
            holder.date_txt.setTextColor(Color.BLACK);
        }

        final int finalPosition = position;

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClick != null){
                    itemClick.onClick(dt, finalPosition);
                }
            }
        });
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(itemClick != null){
                    itemClick.onLongClick(dt, finalPosition);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    @Override
    public long getItemId(int position){
        return position;
    }

    public void clearList() {
        data.clear();
    }
}
