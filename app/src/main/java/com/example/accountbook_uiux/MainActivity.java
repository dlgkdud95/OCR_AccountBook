package com.example.accountbook_uiux;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static DBHelper dbHelper;

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd"); // 현재 날짜 구하기

    //private FragmentStateAdapter fragmentStateAdapter;

    //frame_stats 변수

    //activity_main 화면
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private MainViewFragment mainViewFragment;
    private StatsViewFragment statsViewFragment;

    //카메라 - 파일
    File file;


    private Uri uri; //file, 웹 주소 관련 처리하는 객체

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
                }
                return true;
            }
        });
        mainViewFragment = new MainViewFragment();
        statsViewFragment = new StatsViewFragment();
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
        }
    }

    public String getDate()
    {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }




}