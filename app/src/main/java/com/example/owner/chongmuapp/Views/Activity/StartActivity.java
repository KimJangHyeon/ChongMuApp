package com.example.owner.chongmuapp.Views.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.owner.chongmuapp.Common.Constant;
import com.example.owner.chongmuapp.Model.Data.Basic.SQLiteGroup;
import com.example.owner.chongmuapp.Model.Info.GroupInfo;
import com.example.owner.chongmuapp.Presenter.DataPresenter;
import com.example.owner.chongmuapp.Presenter.GroupPresenter;
import com.example.owner.chongmuapp.R;
import com.example.owner.chongmuapp.Views.Dialog.CustomDialog1;
import com.example.owner.chongmuapp.Views.Fragment.GroupFragment;
import com.example.owner.chongmuapp.Views.Fragment.MemberFragment;

import java.io.File;
import java.security.acl.Group;

public class StartActivity extends AppCompatActivity implements DataPresenter.View{
    GroupFragment groupFragment;
    MemberFragment memberFragment;

    ViewPager vp_start;
    Button btn_group;
    Button btn_member;
    LinearLayout llayout_adressbook_btn;
    final int vp_num = 2;
    Long backKeyPressedTime = 0L;
    int selected_page = 0;

    private CustomDialog1 groupDialog1;
    private CustomDialog1 memberDialog1;
    View.OnClickListener gLeftClickListener;
    View.OnClickListener gRightClickListener;
    View.OnClickListener mLeftClickListener;
    View.OnClickListener mRightClickListener;


    DataPresenter dataPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        //fragment init
        memberFragment = new MemberFragment();
        groupFragment = new GroupFragment();

        setDataPresenter();
        setCustomDialog();



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selected_page == Constant.PAGE_GROUP){
                    groupDialog1.show();
                } else if (selected_page == Constant.PAGE_MEMBER){
                    memberDialog1.show();
                } else{

                }
            }
        });

        vp_start = (ViewPager) findViewById(R.id.vp_start);
        llayout_adressbook_btn = (LinearLayout)findViewById(R.id.llayout_adressbook_btn);
        btn_group = (Button)findViewById(R.id.btn_group);
        btn_member = (Button)findViewById(R.id.btn_member);

        vp_start.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        vp_start.setOffscreenPageLimit(1);
        vp_start.setCurrentItem(0);

        btn_group.setOnClickListener(movePageListener);
        btn_member.setOnClickListener(movePageListener);

        btn_group.setTag(0);
        btn_member.setTag(1);
        btn_group.setSelected(true);

        vp_start.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int i = 0;
                while (i<vp_num)
                {
                    if (position == i) {
                        selected_page = i;
                        llayout_adressbook_btn.findViewWithTag(i).setSelected(true);
                    } else {
                        llayout_adressbook_btn.findViewWithTag(i).setSelected(false);
                    }
                    i++;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(StartActivity.this, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        } if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    public void setCustomDialog(){
        //set group dialog
        gLeftClickListener =new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groupDialog1.cancel();
            }
        };
        gRightClickListener =new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(groupDialog1.getEdit1Str().isEmpty()){
                    Toast.makeText(getApplicationContext(), "group name is empty",Toast.LENGTH_SHORT).show();
                }
                else {
                    int gid = groupFragment.addInfo(groupDialog1.getEdit1Str());
                    Intent intent = new Intent(StartActivity.this, AddMemberActivity.class);
                    intent.putExtra("gid", gid);
                    intent.putExtra("gName", groupDialog1.getEdit1Str());
                    startActivity(intent);
                    groupDialog1.dismiss();
                }
            }
        };
        groupDialog1 = new CustomDialog1(this, "GROUP ADD", "group", gLeftClickListener, gRightClickListener);

        //set member dialog
        mLeftClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                memberDialog1.cancel();
            }
        };

        mRightClickListener =new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(memberDialog1.getEdit1Str().isEmpty()){
                    Toast.makeText(getApplicationContext(), "member name is empty",Toast.LENGTH_SHORT).show();
                }
                else {
                    int mid = memberFragment.addInfo(memberDialog1.getEdit1Str());
                    Intent intent = new Intent(StartActivity.this, AddGroupActivity.class);
                    intent.putExtra("mid", mid);
                    intent.putExtra("mName", memberDialog1.getEdit1Str());
                    startActivity(intent);
                    memberDialog1.dismiss();
                }
            }
        };
        memberDialog1 = new CustomDialog1(this, "MEMBER ADD", "member", mLeftClickListener, mRightClickListener);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    View.OnClickListener movePageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int tag = (int) v.getTag();

            int i = 0;
            while (i<vp_num)
            {
                if (tag == i) {
                    selected_page = i;
                    llayout_adressbook_btn.findViewWithTag(i).setSelected(true);
                } else {
                    llayout_adressbook_btn.findViewWithTag(i).setSelected(false);
                }
                i++;
            }

            vp_start.setCurrentItem(tag);
        }
    };




    private class ViewPagerAdapter extends FragmentStatePagerAdapter
    {
        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                case 0:
                    return groupFragment;
                case 1:
                    return memberFragment;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return vp_num;
        }
    }
    private void setDataPresenter(){
        dataPresenter = new DataPresenter(this);
        dataPresenter.setContext(getApplicationContext());
        dataPresenter.loadDB();
    }
}
