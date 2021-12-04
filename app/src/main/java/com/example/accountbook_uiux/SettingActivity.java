package com.example.accountbook_uiux;

import static com.example.accountbook_uiux.MainActivity.dbHelper;
import static com.example.accountbook_uiux.MainActivity.editor;
import static com.example.accountbook_uiux.MainActivity.preferences;
import static com.example.accountbook_uiux.MoneyInputActivity.LIMIT_OUTLAY;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class SettingActivity extends AppCompatActivity {

    public static String NICKNAME = "";
    private TextView tv_outLimit, tv_name, tv_del;
    private EditText et_outLimit, et_name;
    private Button btn_save, btn_delete;




    MoneyInputActivity moneyInputActivity = new MoneyInputActivity();

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);



        tv_outLimit = (TextView) findViewById(R.id.tv_outLimit);
        et_outLimit = (EditText) findViewById(R.id.et_outLimit);
        btn_save    = (Button)   findViewById(R.id.btn_save);
        tv_name = (TextView) findViewById(R.id.tv_name);
        et_name = (EditText) findViewById(R.id.et_name);
        tv_del = (TextView) findViewById(R.id.tv_del);
        btn_delete = (Button) findViewById(R.id.btn_delete);

        int conv_outlay = LIMIT_OUTLAY;
        String limit_outlay = Integer.toString(LIMIT_OUTLAY);

        et_name.setText(preferences.getString("name","저장된 이름이 없습니다"));

        NICKNAME = preferences.getString("name","ㅇㅇㅇ");

        et_outLimit.setText(limit_outlay);

        et_outLimit.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        //터치했을 때의 이벤트
                        et_outLimit.setText(""); // 빈값으로 변경
                        break;
                    }
                }
                return false;
            }
        });

        et_name.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        //터치했을 때의 이벤트
                        et_name.setText(""); // 빈값으로 변경
                        break;
                    }
                }
                return false;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String temp = et_outLimit.getText().toString();
                int limit;
                if(temp.equals("")) // 아무것도 입력 안할 시
                {
                    limit = LIMIT_OUTLAY; // 저번에 입력해둔 값으로
                }
                else {
                    limit = Integer.parseInt(temp); // 아니면 새로받은 값
                }
                LIMIT_OUTLAY = limit;


                String nickname = et_name.getText().toString();
                editor.putString("name", nickname); //SharedPreference에 name저장
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dbHelper.DeleteDB();
                Toast.makeText(getApplicationContext(), "전체데이터 삭제 완료", Toast.LENGTH_SHORT).show();
            }
        });



    }
}
