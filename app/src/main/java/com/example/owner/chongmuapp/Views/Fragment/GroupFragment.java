package com.example.owner.chongmuapp.Views.Fragment;


//import android.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.owner.chongmuapp.Model.Info.GroupInfo;
import com.example.owner.chongmuapp.Presenter.GroupPresenter;
import com.example.owner.chongmuapp.Presenter.InfoHandler.GroupHandler;
import com.example.owner.chongmuapp.R;
import com.example.owner.chongmuapp.Views.Activity.EventActivity;
import com.example.owner.chongmuapp.Views.Adapter.GroupAdapter;

/**
 * Created by Owner on 2018-01-23.
 */

public class GroupFragment extends Fragment implements GroupPresenter.View{
    private GroupPresenter groupPresenter;
    RecyclerView recyclerView;
    GroupAdapter groupAdapter = null;
    private AlertDialog.Builder alert;

    public GroupFragment() {
        super();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);

        alert  = new AlertDialog.Builder(getActivity());
        recyclerView = (RecyclerView)view.findViewById(R.id.recy_group);
        groupPresenter = new GroupPresenter(this);
        groupPresenter.setContext(getContext());

        groupPresenter.showAll();
        return view;
    }

    /* View Interface */
    @Override
    public void showAdapter(GroupAdapter adapter) {
        recyclerView.setAdapter(adapter);
        adapter.setItemClick(new GroupAdapter.ItemClick(){
            @Override
            public void onClick(GroupInfo groupInfo, int position) {
                Intent intent = new Intent(getActivity(), EventActivity.class);
                intent.putExtra("gid", groupInfo.getId());
                Toast.makeText(getActivity(), "gid: "+groupInfo.getId(),
                        Toast.LENGTH_LONG).show();

                startActivity(intent);
            }

            @Override
            public void onLongClick(GroupInfo groupInfo, int position){
                final int pos = position;
                final int gid = groupInfo.getId();
                if(groupInfo.getPin() == 0){
                    alert.setTitle("really??");
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //확인
                            groupPresenter.confirmDel(pos, gid);
                        }
                    });
                    alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //취소
                        }
                    });
                    alert.show();

                } else{
                    Toast.makeText(getActivity(), "pin 갯수가 0이 아님",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public int addInfo(String groupName){
        return groupPresenter.confirmAdd(groupName);
    }
    public void addNullInfo(String groupName){
        Log.e("처음 들어옴", "");

        groupPresenter = new GroupPresenter(this);
        groupPresenter.setContext(getContext());
        groupPresenter.showAll();
        Log.e("BEFORE ADD ", "FUNCTION");
        groupPresenter.confirmAdd(groupName);

    }
}
