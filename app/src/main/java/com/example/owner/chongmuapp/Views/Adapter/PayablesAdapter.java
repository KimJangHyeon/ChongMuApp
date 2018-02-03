package com.example.owner.chongmuapp.Views.Adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.chongmuapp.Model.Info.LogInfo;
import com.example.owner.chongmuapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Owner on 2018-01-31.
 */

public class PayablesAdapter extends RecyclerView.Adapter<PayablesAdapter.ViewHolder>  {
    private ArrayList<LogInfo> data;

    //아이템 클릭 콜백 등록
    private ItemClick itemClick;
    //private TextChange textChange;
    private EditorAction editorAction;

    private boolean fixPayables;
    static int count = -1;

    public interface ItemClick {
        public void onClick(LogInfo memberInfo, int position);
        public void onLongClick(LogInfo memberInfo, int position);
    }


//    public interface TextChange {
//        public void beforeTextChanged(LogInfo logInfo, int position, int oldPayables);
//        public void afterTextChanged(LogInfo logInfo, int position, int newPayables);
//    }

    public interface EditorAction{
        public void onClickDone(LogInfo logInfo, int position, int pays);
    }


    //아이템 클릭 시 실행될 함수
    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }
    //public void setTextChange(TextChange textChange) {this.textChange = textChange; }
    public void setEditorAction(EditorAction editorAction) { this.editorAction = editorAction; }


    public PayablesAdapter(ArrayList<LogInfo> itemList, boolean fixPayables) {
        this.fixPayables = fixPayables;
        if (itemList == null) {
            data = new ArrayList<>();
        } else {
            data = itemList;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name_txt;
        private RelativeLayout item_bg;
        private EditText payables_etxt;
        View view;
        public ViewHolder(View view) {
            super(view);
            this.view = view;
            payables_etxt = (EditText) view.findViewById(R.id.payables_etxt);
            name_txt = (TextView) view.findViewById(R.id.name_txt);
            item_bg = (RelativeLayout) view.findViewById(R.id.item_bg);
        }
    }

    public void sortItem() {
        Comparator<LogInfo> order = new Comparator<LogInfo>() {
            @Override
            public int compare(LogInfo o1, LogInfo o2) {
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

    public void addItem(LogInfo memberInfo) {
        data.add(memberInfo);
        sortItem();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payables, null);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        position = position%data.size();
        final LogInfo dt = data.get(position);
        //final ViewHolder h = holder;

        holder.name_txt.setText(data.get(position).getName());

            //처음에만
        holder.payables_etxt.setText(String.valueOf(data.get(position).getPayables()));
        final String editText = holder.payables_etxt.getText().toString();
        final int editInt = dt.getPayables();
        final ViewHolder vh = holder;
        //add 이면
        //edittext셋이 안된경우
        if(data.get(position).getDone()){
            holder.item_bg.setBackgroundResource(R.drawable.menubtn_mint_square);
            holder.name_txt.setTextColor(Color.WHITE);
            holder.payables_etxt.setTextColor(Color.WHITE);
        } else {
            holder.item_bg.setBackgroundResource(R.drawable.item_white_square);
            holder.name_txt.setTextColor(Color.BLACK);
            holder.payables_etxt.setTextColor(Color.BLACK);
        }
        if(fixPayables){
            holder.payables_etxt.setFocusable(false);
            holder.payables_etxt.setClickable(false);
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
        holder.payables_etxt.setImeOptions(EditorInfo.IME_ACTION_DONE);
        holder.payables_etxt.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {
                    if(vh.payables_etxt.getText().toString().isEmpty()) editorAction.onClickDone(dt, finalPosition, 0);
                    else editorAction.onClickDone(dt, finalPosition, Integer.parseInt(vh.payables_etxt.getText().toString()));
                    return true;
                }
                return false;
            }
        });

//        holder.payables_etxt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                Log.e("info", dt.getPayables()+"");
//                Log.e("editInt", editInt+"");
//                Log.e("finalP", finalPosition+"");
//                textChange.beforeTextChanged(dt, finalPosition, editInt);
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                //힌트가 0, 텍스트가 0
//                int payables;
//                if(editText.equals("")){
//                    payables = 0;
//                }else {
//                    payables = Integer.parseInt(editText);
//                }
//                dt.setPayables(payables);
//
//                textChange.afterTextChanged(dt, finalPosition, Integer.parseInt(editText));
//            }
//        });

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
