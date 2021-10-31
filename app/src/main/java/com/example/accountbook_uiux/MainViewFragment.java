package com.example.accountbook_uiux;

import static android.app.Activity.RESULT_OK;

import static com.example.accountbook_uiux.MainActivity.dbHelper;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;

public class MainViewFragment extends Fragment  {

    private View view_main;


    //frame_main (메인화면) 변수
    CalendarView calendarView;
    FloatingActionButton fab_main, fab_camera, fab_writing;
    TextView txt_outlay, outlay, txt_income, income, txt_total, total, tv_limit;

    ArrayList<DBTable> list = new ArrayList<DBTable>();




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

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day)
            {

                String convMonth; // Converted Month
                if(month < 9) convMonth = "0" + Integer.toString(month+1); // 2021-09-30 이렇게 데이터를 저장하기 위해 month가 9 미만이면 0을 붙여줌 (10이 아니라 9인 이유는 return 되는 month값이 +1을 해줘야 실제 month랑 같아짐)
                else convMonth = Integer.toString(month+1);

                String convDay; // Converted Day
                if(day < 10) convDay = "0" + Integer.toString(day); // day도 마찬가지
                else convDay = Integer.toString(day);

                String selectedDate = Integer.toString(year)+ "-" + convMonth+ "-" + convDay; // 선택된 날짜 구하기 ex)2021-09-05

                list = dbHelper.getDataByDate(selectedDate);

                Dialog dialog = new Dialog(getContext(), android.R.style.Theme_Material_Light_Dialog);
                dialog.setContentView(R.layout.dialog_cal);
                TextView tv_date = (TextView) dialog.findViewById(R.id.tv_date);
                TextView tv_type = (TextView) dialog.findViewById(R.id.tv_type);
                TextView tv_cost = (TextView) dialog.findViewById(R.id.tv_cost);
                TextView tv_category = (TextView) dialog.findViewById(R.id.tv_category);
                Button btn_income = (Button) dialog.findViewById(R.id.btn_income);
                Button btn_outlay = (Button) dialog.findViewById(R.id.btn_outlay);
                EditText et_money = (EditText) dialog.findViewById(R.id.et_money);

                tv_date.setText(selectedDate);
                StringBuilder typeBuilder = new StringBuilder(); // StringBuilder값에 for문을 돌리면서 db데이터를 쌓는다
                for(int i = 0; i < list.size(); i++)
                {
                    typeBuilder.append(list.get(i).getType()+"\n\n");
                }
                tv_type.setText(typeBuilder); // setText를 builder로 하면 쌓인 값들이 들어감

                StringBuilder costBuilder = new StringBuilder();
                for(int i = 0; i < list.size(); i++)
                {
                    costBuilder.append(list.get(i).getCost()+"\n\n");
                }
                tv_cost.setText(costBuilder);

                StringBuilder categoryBuilder = new StringBuilder();
                for(int i = 0; i < list.size(); i++)
                {
                    categoryBuilder.append(list.get(i).getCategory()+"\n\n");
                }
                tv_category.setText(categoryBuilder);




                btn_income.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        int moneyValue = Integer.parseInt(et_money.getText().toString());
                        dbHelper.InsertDB("수입", moneyValue, "기타", selectedDate, "기타", "");
                        dialog.dismiss();
                        // Spinner로 카테고리 까지
                    }
                });

                btn_outlay.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        int moneyValue = Integer.parseInt(et_money.getText().toString());
                        dbHelper.InsertDB("지출", moneyValue, "기타", selectedDate, "기타", "");
                        dialog.dismiss();
                    }
                });


                dialog.show();
            }
        });

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
