package com.example.accountbook_uiux;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements OnTabItemSelectedListener {

    public static DBHelper dbHelper;
    public static Context mContext;

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd"); // 현재 날짜 구하기

    //activity_main 화면
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private CalenderViewFragment calenderViewFragment;
    private TotalStatsFragment totalStatsFragment;
    private HomeViewFragment homeViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        String testString = dbHelper.getItemName("TRUE");

        Log.d("아이템", testString);


        //하단 네비게이션 눌렀을 때 화면 변경 됨, onNavi~() 메소드 통해 setFrag() 메소드가 해당하는 Fragment로 교체함
        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        setFrag(0);
                        break;
                    case R.id.action_calender:
                        setFrag(1);
                        break;
                    case R.id.action_stats:
                        setFrag(2);
                        break;
                }
                return true;
            }
        });

        calenderViewFragment = new CalenderViewFragment();
        totalStatsFragment = new TotalStatsFragment();
        homeViewFragment = new HomeViewFragment();
        setFrag(0);  // 메인 화면 선택
    }

    //하단 네비게이션 프래그먼트 교체 , onNavigationItemSelected()메소드 안에서 사용
    public void setFrag(int i) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        switch (i) {
            case 0:
                ft.replace(R.id.frame, homeViewFragment); //FragMainView로 이동
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.frame, calenderViewFragment);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.frame, totalStatsFragment);
                ft.commit();
                break;
        }
    }


    //이 메소드가 호출되면 네비게이션 버튼 외 다른 버튼으로 다른 탭으로 이동할 때 해당 탭의 버튼이 선퇙되도록 함
    public void onTabSelected(int position){
        if (position == 0) bottomNavigationView.setSelectedItemId(R.id.action_home);
        else if (position == 1) bottomNavigationView.setSelectedItemId(R.id.action_calender);
        else if (position == 2) bottomNavigationView.setSelectedItemId(R.id.action_stats);
    }

    public String getDate()
    {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }
}