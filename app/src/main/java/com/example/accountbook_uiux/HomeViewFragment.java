package com.example.accountbook_uiux;

import static com.example.accountbook_uiux.EconomyViewFragment.CHANGE_RATE;
import static com.example.accountbook_uiux.EconomyViewFragment.TODAY_KOSPI;
import static com.example.accountbook_uiux.EconomyViewFragment.YSTDAY_KOSPI;
import static com.example.accountbook_uiux.MainActivity.dbHelper;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeViewFragment extends Fragment {

    private View view_home;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    FloatingActionButton fab_main, fab_receipt, fab_writing;
    TextView txt_nickname, txt_receipt, txt_month, txt_homeIncome, txt_homeSpend, txt_incomeNum, txt_spendNum, today_kospi, txt_rate, yesterday_kospi, txt_date, kospiBox;
    Button bt_setting, bt_receiptScan, bt_monthly, bt_receiptStats;
    TextView out_Top1, out_Top2, out_Top3, home_outlay;
    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
    String getTime = df.format(date);
    Calendar cal1 = Calendar.getInstance();
    Calendar cal2 = Calendar.getInstance();

    //fab_main 버튼의 상태, 기본값 -> 선택하지 않은 상태태
    private boolean isFabOpen = false;

    Context context;
    OnTabItemSelectedListener listener;

    public void onAttach(Context context){
        super.onAttach(context);

        this.context = context;

        if (context instanceof OnTabItemSelectedListener) {
            listener = (OnTabItemSelectedListener) context;
        }
    }

    public void onDetach(){
        super.onDetach();

        if (context != null) {
            context = null;
            listener = null;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view_home = inflater.inflate(R.layout.frame_home, container, false);

        txt_nickname = view_home.findViewById(R.id.txt_nickname);
        txt_receipt = view_home.findViewById(R.id.txt_receipt);
        txt_month = view_home.findViewById(R.id.txt_month);
        txt_homeIncome = view_home.findViewById(R.id.txt_homeIncome);
        txt_homeSpend = view_home.findViewById(R.id.txt_homeSpend);
        txt_incomeNum = view_home.findViewById(R.id.txt_incomeNum);
        txt_spendNum = view_home.findViewById(R.id.txt_spendNum);

        kospiBox = view_home.findViewById(R.id.kospiBox);
        txt_date = view_home.findViewById(R.id.txt_date);
        today_kospi = view_home.findViewById(R.id.today_kospi);
        txt_rate = view_home.findViewById(R.id.txt_rate);
        yesterday_kospi = view_home.findViewById(R.id.yesterday_kospi);

        bt_receiptScan = view_home.findViewById(R.id.bt_receiptScan);
        bt_setting = view_home.findViewById(R.id.bt_setting);
        bt_receiptStats = view_home.findViewById(R.id.bt_receiptStats);
        bt_monthly = view_home.findViewById(R.id.bt_monthly);

        out_Top1 = view_home.findViewById(R.id.out_Top1);
        out_Top2 = view_home.findViewById(R.id.out_Top2);
        out_Top3 = view_home.findViewById(R.id.out_Top3);
        home_outlay = view_home.findViewById(R.id.home_outlay);



        String nickname = MainActivity.preferences.getString("name","ㅇㅇㅇ");
        txt_nickname.setText(nickname+"님의\n가계부");

        cal1.setTime(new Date());
        cal2.setTime(new Date());

        cal2.add(Calendar.MONTH, 1);

        Calendar c = Calendar.getInstance();
        home_outlay.setText((c.get(Calendar.MONTH)+1) + "월 지출내역");

        txt_month.setText((c.get(Calendar.MONTH)+1) + "월 내역");
        txt_incomeNum.setText(Integer.toString(dbHelper.periodInquiry(df.format(cal1.getTime())+"-01", df.format(cal2.getTime())+"-01", "수입"))+ " 원"); // 현재 달 수입
        txt_spendNum.setText(Integer.toString(dbHelper.periodInquiry(df.format(cal1.getTime())+"-01", df.format(cal2.getTime())+"-01", "지출"))+ " 원");  // 현재 달 지출



        txt_date.setText((c.get(Calendar.MONTH)+1) + "월 " + (c.get(Calendar.DAY_OF_MONTH)) + "일 " + (c.get(Calendar.HOUR)) + "시 " + (c.get(Calendar.MINUTE)) + "분 기준");
        out_Top1.setText(Integer.toString(dbHelper.periodInquiryAndCategory(df.format(cal1.getTime())+"-01", df.format(cal2.getTime())+"-01", "지출", "식비"))+ " 원"); // 현재 달 TOP3
        out_Top2.setText(Integer.toString(dbHelper.periodInquiryAndCategory(df.format(cal1.getTime())+"-01", df.format(cal2.getTime())+"-01", "지출", "교통비"))+ " 원");
        out_Top3.setText(Integer.toString(dbHelper.periodInquiryAndCategory(df.format(cal1.getTime())+"-01", df.format(cal2.getTime())+"-01", "지출", "기타"))+ " 원");


        if(TODAY_KOSPI.equals("주말입니다"))
        {
            today_kospi.setText("주말입니다");
            yesterday_kospi.setText("0");
            txt_rate.setText("0%");
        }
        else
        {

            if (TODAY_KOSPI.equals("업데이트중"))
            {
                txt_rate.setTextColor(Color.parseColor("#E64033"));
            } else
            {
                today_kospi.setText(TODAY_KOSPI);
                yesterday_kospi.setText(YSTDAY_KOSPI);
                txt_rate.setText(CHANGE_RATE + "%");
                if (Double.parseDouble(TODAY_KOSPI) > Double.parseDouble(YSTDAY_KOSPI))
                {
                    txt_rate.setTextColor(Color.parseColor("#E64033"));
                } else txt_rate.setTextColor(Color.parseColor("#4657B5"));
            }


        }



        /*
        if (TODAY_KOSPI.equals("업데이트중"))
        {
            txt_rate.setTextColor(Color.parseColor("#E64033"));
        } else
        {
            today_kospi.setText(TODAY_KOSPI);
            yesterday_kospi.setText(YSTDAY_KOSPI);
            txt_rate.setText(CHANGE_RATE + "%");
            if (Double.parseDouble(TODAY_KOSPI) > Double.parseDouble(YSTDAY_KOSPI))
            {
                txt_rate.setTextColor(Color.parseColor("#E64033"));
            } else txt_rate.setTextColor(Color.parseColor("#4657B5"));
        }

         */



        bt_setting.setOnClickListener(new View.OnClickListener() { //설정 화면으로 넘어감
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), SettingActivity.class);
                startActivity(intent);
            }
        });

        bt_receiptStats.setOnClickListener(new View.OnClickListener() { //통계화면으로 넘어감
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onTabSelected(2); //네비게이션 번호 받아서 해당 창으로 이동동
               }
            }
        });

        bt_monthly.setOnClickListener(new View.OnClickListener() { //월간 화면으로 넘어감
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onTabSelected(1);
                }
            }
        });

        bt_receiptScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), CameraActivity.class); //영수증인식창으로 바로 넘어가게
                startActivity(intent);
            }
        });

        floatingActionButton(view_home);
        return view_home;
    }

    private void floatingActionButton (View view_home) {

        fab_main = (FloatingActionButton) view_home.findViewById(R.id.fab_main);
        fab_receipt = (FloatingActionButton) view_home.findViewById(R.id.fab_receipt);
        fab_writing = (FloatingActionButton) view_home.findViewById(R.id.fab_writing);

        //(플로팅액션버튼)버튼을 누르면 화면이 넘어감(fab_main 제외)
        fab_main.setOnClickListener(new View.OnClickListener() { //버튼 클릭시 수행할 동작 지정
            @Override
            public void onClick(View view) {
                toggleFab();
            }
        });
        fab_writing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //수입,지출 화면 전환
                Intent intent = new Intent(getActivity().getApplicationContext(), MoneyInputActivity.class);
                startActivity(intent);
            }
        });
        fab_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //영수증 인식 화면 전환
                Intent intent = new Intent(getActivity().getApplicationContext(), CameraActivity.class);
                startActivity(intent); //CameraActivity로 화면 전환
            }
        });
    }
    public void toggleFab() {
        if (isFabOpen) { //닫힌상태
            //writing, camera y축으로 0만큼 움직임
            ObjectAnimator.ofFloat(view_home.findViewById(R.id.fab_writing), "translationY", 0f).start();
            ObjectAnimator.ofFloat(view_home.findViewById(R.id.fab_receipt), "translationY", 0f).start();
        } else {
            //writing -100, camera -200 만큼 y축으로움직임
            ObjectAnimator.ofFloat(view_home.findViewById(R.id.fab_writing), "translationY", -150f).start();
            ObjectAnimator.ofFloat(view_home.findViewById(R.id.fab_receipt), "translationY", -290f).start();
        }

        isFabOpen = !isFabOpen;
    }

}