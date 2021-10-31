package com.example.accountbook_uiux;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static DBHelper dbHelper;
    public static Context mContext;

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd"); // 현재 날짜 구하기

    //activity_main 화면
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private MainViewFragment mainViewFragment;
    private StatsViewFragment statsViewFragment;
    private TravelViewFragment travelViewFragment;
    private HistoryViewFragment historyViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);




        //하단 네비게이션 눌렀을 때 화면 변경 됨, onNavi~() 메소드 통해 setFrag() 메소드가 해당하는 Fragment로 교체함
        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_calender:
                        setFrag(0);
                        break;
                    case R.id.action_stats:
                        setFrag(1);
                        break;
                    case R.id.action_travel:
                        setFrag(2);
                        break;
                    case R.id.action_history:
                        setFrag(3);
                        break;
                }
                return true;
            }
        });
        mainViewFragment = new MainViewFragment();
        statsViewFragment = new StatsViewFragment();
        travelViewFragment = new TravelViewFragment();
        historyViewFragment = new HistoryViewFragment();
        setFrag(0);  // 메인 화면 선택
    }

    //하단 네비게이션 프래그먼트 교체 , onNavigationItemSelected()메소드 안에서 사용
    private void setFrag(int i) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        switch (i) {
            case 0:
                ft.replace(R.id.frame, mainViewFragment); //FragMainView 클래스로 이동
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.frame, statsViewFragment); //FragStatsView 클래스로 이동
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.frame, travelViewFragment);
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.frame, historyViewFragment);
                ft.commit();
                break;
        }
    }



    public String getDate()
    {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }
}