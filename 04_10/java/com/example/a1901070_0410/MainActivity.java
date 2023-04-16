package com.example.a1901070_0410;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;//에컨백?
    ActionBar actionBar;
    TabLayout tabLayout;

    Fragment1 fragment1;
    Fragment2 fragment2;
    Fragment3 fragment3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//toolbar를 엑션바로 만들어주는것

        tabLayout =(TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("플레그먼트1"));
        tabLayout.addTab(tabLayout.newTab().setText("플레그먼트2"));
        tabLayout.addTab(tabLayout.newTab().setText("플레그먼트3"));

        fragment1=new Fragment1();//인스턴스화
        fragment2=new Fragment2();
        fragment3=new Fragment3();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position=tab.getPosition();
                if(position==0){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment1).commit();
                }
                else if(position==1){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment2).commit();
                }
                else if(position==2){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment3).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}