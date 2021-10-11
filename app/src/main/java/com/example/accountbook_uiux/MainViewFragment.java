package com.example.accountbook_uiux;

import static android.app.Activity.RESULT_OK;

import static com.example.accountbook_uiux.MainActivity.dbHelper;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;

public class MainViewFragment extends Fragment  {

    private View view_main;

    //frame_main (메인화면) 변수
    CalendarView calendarView;
    FloatingActionButton fab_main, fab_camera, fab_writing;
    TextView txt_outlay, outlay, txt_income, income, txt_total, total, tv_limit;


    //fab_main 버튼의 상태, 기본값 -> 선택하지 않은 상태태
    private boolean isFabOpen = false;

    MoneyInputActivity moneyInputActivity = new MoneyInputActivity();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view_main = inflater.inflate(R.layout.frame_main, container, false);
        //frame_main
        calendarView = (CalendarView) view_main.findViewById(R.id.calendarView);
        txt_income = (TextView) view_main.findViewById(R.id.txt_income);
        txt_outlay = (TextView) view_main.findViewById(R.id.txt_outlay);
        txt_total = (TextView) view_main.findViewById(R.id.txt_total);
        income = (TextView) view_main.findViewById(R.id.income);
        outlay = (TextView) view_main.findViewById(R.id.outlay);
        total = (TextView) view_main.findViewById(R.id.total);
        tv_limit = (TextView) view_main.findViewById(R.id.tv_limit);

        income.setText(Integer.toString(dbHelper.getSum("수입"))+ " 원");
        outlay.setText(Integer.toString(dbHelper.getSum("지출"))+ " 원");
        total.setText(Integer.toString(dbHelper.getSum("수입") - dbHelper.getSum("지출"))+ " 원");
        tv_limit.setText("지출제한 : "+Integer.toString(moneyInputActivity.LIMIT_OUTLAY)+"원");

        floatingActionButton(view_main); //fab버튼 작동 할 수 있게 해주는 메소드
        DB_Delete_Button(view_main);
        return view_main;
    }

    private  void DB_Delete_Button (View view_main)
    {
        Button btn_delDB = (Button) view_main.findViewById(R.id.btn_delDB);

        btn_delDB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dbHelper.DeleteDB();
                income.setText(Integer.toString(dbHelper.getSum("수입"))+ " 원");
                outlay.setText(Integer.toString(dbHelper.getSum("지출"))+ " 원");
                total.setText(Integer.toString(dbHelper.getSum("수입") - dbHelper.getSum("지출"))+ " 원");
            }
        });
    }

    private void floatingActionButton (View view_main) {

        FloatingActionButton fab_main = (FloatingActionButton) view_main.findViewById(R.id.fab_main);
        FloatingActionButton fab_camera = (FloatingActionButton) view_main.findViewById(R.id.fab_camera);
        FloatingActionButton fab_writing = (FloatingActionButton) view_main.findViewById(R.id.fab_writing);

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
        fab_camera.findViewById(R.id.fab_camera).setOnClickListener(new View.OnClickListener() {
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
            ObjectAnimator.ofFloat(view_main.findViewById(R.id.fab_writing), "translationY", 0f).start();
            ObjectAnimator.ofFloat(view_main.findViewById(R.id.fab_camera), "translationY", 0f).start();
        } else {
            //writing -100, camera -200 만큼 y축으로움직임
            ObjectAnimator.ofFloat(view_main.findViewById(R.id.fab_writing), "translationY", -150f).start();
            ObjectAnimator.ofFloat(view_main.findViewById(R.id.fab_camera), "translationY", -290f).start();
        }

        isFabOpen = !isFabOpen;
    }

}
