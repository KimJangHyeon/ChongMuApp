package com.example.owner.chongmuapp.Views.Fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.owner.chongmuapp.Model.Info.GroupInfo;
import com.example.owner.chongmuapp.Model.Info.MemberInfo;
import com.example.owner.chongmuapp.Presenter.GroupPresenter;
import com.example.owner.chongmuapp.Presenter.MemberPresenter;
import com.example.owner.chongmuapp.R;
import com.example.owner.chongmuapp.Views.Activity.AddGroupActivity;
import com.example.owner.chongmuapp.Views.Activity.EventActivity;
import com.example.owner.chongmuapp.Views.Adapter.GroupAdapter;
import com.example.owner.chongmuapp.Views.Adapter.MemberAdapter;

/**
 * Created by Owner on 2018-01-22.
 */

public class MemberFragment extends Fragment implements MemberPresenter.View{
    private MemberPresenter memberPresenter;
    RecyclerView recyclerView;
    MemberAdapter memberAdapter = null;
    private AlertDialog.Builder alert;

    public MemberFragment() {
        super();
    }

    //    public void addInfo(GroupInfo groupInfo){
//        groupPresenter.confirmAdd(groupInfo);
//    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member, container, false);

        alert  = new AlertDialog.Builder(getActivity());
        recyclerView = (RecyclerView)view.findViewById(R.id.recy_member);
        memberPresenter = new MemberPresenter(this);
        memberPresenter.setContext(getContext());

        memberPresenter.showAll();
        return view;
    }

    /* View Interface */
    @Override
    public void showAdapter(MemberAdapter adapter) {
        recyclerView.setAdapter(adapter);
        adapter.setItemClick(new MemberAdapter.ItemClick(){
            @Override
            public void onClick(MemberInfo memberInfo, int position) {
                Intent intent = new Intent(getActivity(), AddGroupActivity.class);
                intent.putExtra("mid", memberInfo.getId());
                intent.putExtra("mName", memberInfo.getName());
                Toast.makeText(getActivity(), "mid: "+memberInfo.getId(),
                        Toast.LENGTH_LONG).show();

                startActivity(intent);
            }

            @Override
            public void onLongClick(MemberInfo memberInfo, int position){
                final int pos = position;
                final int gid = memberInfo.getId();
                if(memberInfo.getPin() == 0){
                    alert.setTitle("really??");
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //확인
                            memberPresenter.confirmDel(pos, gid);
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

    public int addInfo(String memberName){
        return memberPresenter.confirmAdd(memberName);
    }
}
