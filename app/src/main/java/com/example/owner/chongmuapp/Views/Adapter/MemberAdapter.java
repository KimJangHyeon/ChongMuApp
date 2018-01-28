package com.example.owner.chongmuapp.Views.Adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.owner.chongmuapp.Model.Info.GroupInfo;
import com.example.owner.chongmuapp.Model.Info.MemberInfo;
import com.example.owner.chongmuapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Owner on 2018-01-22.
 */

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {
    private ArrayList<MemberInfo> data;

    //아이템 클릭 콜백 등록
    private ItemClick itemClick;

    public interface ItemClick {
        public void onClick(MemberInfo memberInfo, int position);
        public void onLongClick(MemberInfo memberInfo, int position);
    }
    //아이템 클릭 시 실행될 함수
    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public MemberAdapter(ArrayList<MemberInfo> itemList) {
        if (itemList == null) {
            data = new ArrayList<>();
        } else {
            data = itemList;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView keyword;
        private RelativeLayout item_bg;
        View view;
        public ViewHolder(View view) {
            super(view);
            this.view = view;
            keyword = (TextView) view.findViewById(R.id.group_txt);
            item_bg = (RelativeLayout) view.findViewById(R.id.item_bg);
        }
    }

    public void sortItem() {
        Comparator<MemberInfo> order = new Comparator<MemberInfo>() {
            @Override
            public int compare(MemberInfo o1, MemberInfo o2) {
                int ret;
                if (o1.getName().compareTo(o2.getName()) < 0)
                    ret = -1;
                else
                    ret = 1;
                return ret;
            }
        };
        Collections.sort(data, order);
        notifyDataSetChanged();

    }

    public void addItem(MemberInfo memberInfo) {
        data.add(memberInfo);
        sortItem();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        position = position%data.size();
        final MemberInfo dt = data.get(position);
        holder.keyword.setText(data.get(position).getName() +" (" + data.get(position).getPin()+")");

        //add 이면
        if(data.get(position).getPin()!=0){
            holder.item_bg.setBackgroundResource(R.drawable.menubtn_mint_square);
            holder.keyword.setTextColor(Color.WHITE);
        }
        else{
            holder.item_bg.setBackgroundResource(R.drawable.item_white_square);
            holder.keyword.setTextColor(Color.BLACK);
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
//        return position%strings.size();
    }

    public void clearList() {
        data.clear();
    }
}
