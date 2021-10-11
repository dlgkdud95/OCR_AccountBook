package com.example.accountbook_uiux;

import static com.example.accountbook_uiux.MainActivity.dbHelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

public class MoneyInputActivity extends AppCompatActivity {
   // private FragmentStateAdapter fragmentStateAdapter;


    public static Context mContext;
    public static int LIMIT_OUTLAY = 50000; // 지출 제한
    public static int CURRENT_OUTLAY = 0;   // 현재 지출
    public static int ALERT_COUNT = 0;      // 경고 알림이 앱 접속 시 1회만 뜨게해주는 변수

    MainActivity mainActivity = new MainActivity();

    public static int TYPE_SELECTED = 0;
    // 0 = 수입
    // 1 = 지출

    TabLayout tabLayout;
    ViewPager2 pager2;
    FragmentAdapter adapter;
    ViewGroup container;

    //레이아웃 frag_view_income
    TextView in_txt_date, in_txt_price, in_txt_method, in_txt_catalog, in_txt_details;
    EditText in_editTextDate, in_editTextMoney, in_editTextDetails;
    Spinner in_sp_method, in_sp_catalog;

    //레이아웃 frag_view_outlay
    TextView out_txt_date, out_txt_price, out_txt_method, out_txt_catalog, out_txt_details;
    EditText out_editTextDate, out_editTextMoney, out_editTextDetails;
    Spinner out_sp_method, out_sp_catalog;

    Button bt_register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.money_input);





        //money_input
        tabLayout = findViewById(R.id.tablayout);
        pager2 = findViewById(R.id.viewPager);

        //수입,지출 창 viewPager
        FragmentManager fm = getSupportFragmentManager();
        adapter = new FragmentAdapter(fm, getLifecycle()); //FragmentAdapter클래스와 연결, Frag_inout, Frag_outlay클래스를 리턴하는 클래스
        pager2.setAdapter(adapter); //viewpager와 adapter연결,

        //탭 셋팅, 탭 추가함
        tabLayout.addTab(tabLayout.newTab().setText("수입"));
        tabLayout.addTab(tabLayout.newTab().setText("지출"));

        container = (ViewGroup) findViewById(R.id.viewPager);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE); // Inflate를 사용하여 다른 레이아웃 접근





        //탭 메뉴 누르면 해당 프래그먼트로 변경됨
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) { //선택 시
               // int income_money, outlay_money;
                pager2.setCurrentItem(tab.getPosition());  //선택한 탭의 position값 넘겨줌

                if (tab.getPosition() == 0) {
                    TYPE_SELECTED = 0;



                }
                else if (tab.getPosition() == 1) {
                    TYPE_SELECTED = 1;
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { //탭 선택 안 했을 시

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { //선택된 탭 다시 클릭 시

            }
        });
        //추상클래스, 페이지 바뀔 때 실행, 콜백
        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        /*//총 수입
        int income_money = Integer.parseInt(in_editTextMoney.getText().toString());
        INCOMETOTAL = INCOMETOTAL + income_money;
        //총 지출
        int outlay_money = Integer.parseInt(out_editTextMoney.getText().toString());
        OUTLAYTOTAL = OUTLAYTOTAL + outlay_money;
        //수입과 지출의 합
        SUMTOTAL = INCOMETOTAL - OUTLAYTOTAL;  */



        bt_register = findViewById(R.id.bt_register);
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {  //bt_register(저장하기) 버튼을 누르면 메인뷰(캘린더화면)으로 돌아감
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);



                if(TYPE_SELECTED == 0) //수입
                {


                    in_editTextMoney = (EditText) container.findViewById(R.id.in_editTextMoney); // container에서 가져오기
                    in_txt_date = (TextView) container.findViewById(R.id.in_txt_date);
                    in_txt_price = (TextView) container.findViewById(R.id.in_txt_price);
                    in_txt_method = (TextView) container.findViewById(R.id.in_txt_method);
                    in_txt_catalog = (TextView) container.findViewById(R.id.in_txt_catalog);
                    in_txt_details = (TextView) container.findViewById(R.id.in_txt_details);
                    in_editTextDate = (EditText) container.findViewById(R.id.in_editTextDate);
                    in_editTextDetails = (EditText) container.findViewById(R.id.in_editTextDetails);
                    in_sp_method = (Spinner) container.findViewById(R.id.in_sp_method);
                    in_sp_catalog = (Spinner) container.findViewById(R.id.in_sp_catalog);



                    int cost = Integer.parseInt(in_editTextMoney.getText().toString());
                    String category = in_sp_catalog.getSelectedItem().toString();
                    String payment = in_sp_method.getSelectedItem().toString();
                    String detail = in_editTextDetails.getText().toString();

                    dbHelper.InsertDB("수입", cost, category, mainActivity.getDate(), payment, detail);

                    TYPE_SELECTED = 0;
                }

                else if(TYPE_SELECTED == 1)
                {

                    out_txt_date = (TextView) container.findViewById(R.id.out_txt_date);
                    out_txt_price = (TextView) container.findViewById(R.id.out_txt_price);
                    out_txt_method = (TextView) container.findViewById(R.id.out_txt_method);
                    out_txt_catalog = (TextView) container.findViewById(R.id.out_txt_catalog);
                    out_txt_details = (TextView) container.findViewById(R.id.out_txt_details);
                    out_editTextDate = (EditText) container.findViewById(R.id.out_editTextDate);
                    out_editTextMoney = (EditText) container.findViewById(R.id.out_editTextMoney);
                    out_editTextDetails = (EditText)container.findViewById(R.id.out_editTextDetails);
                    out_sp_method = (Spinner) container.findViewById(R.id.out_sp_method);
                    out_sp_catalog = (Spinner) container.findViewById(R.id.out_sp_catalog);

                    int cost = Integer.parseInt(out_editTextMoney.getText().toString());
                    String category = out_sp_catalog.getSelectedItem().toString();
                    String payment = out_sp_method.getSelectedItem().toString();
                    String detail = out_editTextDetails.getText().toString();
                    dbHelper.InsertDB("지출", cost, category,mainActivity.getDate(), payment, detail);

                    CURRENT_OUTLAY = dbHelper.getSum("지출");
                    Log.d("확인", Integer.toString(CURRENT_OUTLAY));
                    if(LIMIT_OUTLAY < CURRENT_OUTLAY && ALERT_COUNT == 0)
                    {
                        Log.d("ALERT!", "지출초과");
                        ALERT_COUNT = ALERT_COUNT + 1;
                    }

                    TYPE_SELECTED = 0;

                }


                startActivity(intent);


            }
        });

    }


}
