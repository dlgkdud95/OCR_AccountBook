package com.example.accountbook_uiux;

import static com.example.accountbook_uiux.EconomyViewFragment.CHANGE_RATE;
import static com.example.accountbook_uiux.EconomyViewFragment.TODAY_KOSPI;
import static com.example.accountbook_uiux.EconomyViewFragment.YSTDAY_KOSPI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements OnTabItemSelectedListener {

    public static DBHelper dbHelper;
    public static Context mContext;
    public static SharedPreferences preferences; // 간단한 DB같은 개념
    public static SharedPreferences.Editor editor;

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd"); // 현재 날짜 구하기
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    //activity_main 화면
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private CalenderViewFragment calenderViewFragment;
    private TotalStatsFragment totalStatsFragment;
    private HomeViewFragment homeViewFragment;
    private EconomyViewFragment economyViewFragment;






    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        new Thread()
        {
            public void run()
            {

                String result = "";
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
                Calendar cal1 = Calendar.getInstance();
                Calendar cal2 = Calendar.getInstance();
                cal1.setTime(new Date());
                cal2.setTime(new Date());
                cal1.add(Calendar.DATE, -1);
                cal2.add(Calendar.DATE, -2);
                String yesterday = df.format(cal1.getTime());
                String twodaysago = df.format(cal2.getTime());
                String startDate = getDate2();
                String EndDate = getDate2();
                try
                {
                    URL url = new URL("http://ecos.bok.or.kr/api/StatisticSearch/UZMM2M6HM74AL9ZG77US/json/kr/1/10/064Y001/DD/"+ yesterday +"/"+ EndDate + "/0001000/?/?/");

                    BufferedReader bf;

                    bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

                    result = bf.readLine();

                }catch(Exception e){
                    System.out.println(e.getMessage());
                }

                try
                {
                    // KOSPI JSON PASING

                    String json = result;
                    Log.d("json", json);
                    JSONObject jsonObject = new JSONObject(json);
                    String StatisticSearch = jsonObject.getString("StatisticSearch");
                    JSONObject firstObject = new JSONObject(StatisticSearch);

                    String row = firstObject.getString("row");
                    JSONArray jsonArray = new JSONArray(row);

                    if(jsonArray.length() == 1) // jsonArray의 길이가 1일때, 즉 아직 오늘 데이터가 올라오지 않았을때 혹은 전날에 시장이 열리지 않았을때
                    {
                        JSONObject object = jsonArray.getJSONObject(0); //index 0만 파싱
                        String DATA_VALUE = object.getString("DATA_VALUE");
                        double todayValue = Double.parseDouble(DATA_VALUE); // 오늘값엔 마지막으로 저장된 값인 어제값이 들어감
                        double ystdayValue = todayValue; //오늘 값과 같은 값
                        String changeRate = "0"; // 변화율은 0


                        TODAY_KOSPI = "업데이트중";
                        YSTDAY_KOSPI = Double.toString(ystdayValue);
                        CHANGE_RATE = changeRate;


                    }
                    else if(jsonArray.length() == 2) //오늘 데이터가 올라왔을때(저녁쯤)
                    {
                        JSONObject object = jsonArray.getJSONObject(0);
                        String DATA_VALUE = object.getString("DATA_VALUE");
                        JSONObject object2 = jsonArray.getJSONObject(1); //1까지 파싱싱
                        String DATA_VALUE2 = object2.getString("DATA_VALUE");

                        double ystdayValue = Double.parseDouble(DATA_VALUE); // 어제값엔 어제값
                        double todayValue = Double.parseDouble(DATA_VALUE2); // 오늘값엔 오늘값
                        double change = ((todayValue - ystdayValue) / ystdayValue) * 100 ; //변화율 계산

                        String changeRate = String.format("%.2f", change); //소수점 자르기

                        TODAY_KOSPI = Double.toString(todayValue);
                        YSTDAY_KOSPI = Double.toString(ystdayValue);
                        CHANGE_RATE = changeRate;

                    }



                }catch (Exception e)
                {
                    System.out.println(e.getMessage());
                }


            }
        }.start();

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
                    case R.id.action_economy:
                        setFrag(3);
                        break;
                }
                return true;
            }
        });

        calenderViewFragment = new CalenderViewFragment();
        totalStatsFragment = new TotalStatsFragment();
        homeViewFragment = new HomeViewFragment();
        economyViewFragment = new EconomyViewFragment();
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
            case 3:
                ft.replace(R.id.frame, economyViewFragment);
                ft.commit();
                break;
        }
    }


    //이 메소드가 호출되면 네비게이션 버튼 외 다른 버튼으로 다른 탭으로 이동할 때 해당 탭의 버튼이 선퇙되도록 함
    public void onTabSelected(int position){
        if (position == 0) bottomNavigationView.setSelectedItemId(R.id.action_home);
        else if (position == 1) bottomNavigationView.setSelectedItemId(R.id.action_calender);
        else if (position == 2) bottomNavigationView.setSelectedItemId(R.id.action_stats);
        else if (position == 3) bottomNavigationView.setSelectedItemId(R.id.action_economy);
    }

    public String getDate()
    {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }

    public String getDate2()
    {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return dateFormat.format(mDate);
    }
}