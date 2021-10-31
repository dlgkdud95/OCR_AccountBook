package com.example.accountbook_uiux;

import static com.example.accountbook_uiux.MainActivity.dbHelper;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StatsViewFragment extends Fragment
{

    private View view_stats;
    MainActivity mainActivity = new MainActivity();
    TextView tv_foodsum;
    Button btn_toBar, btn_toPie;

    PieChart pieChart;
    BarChart barChart;
    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat df = new SimpleDateFormat("MM");
    String getTime = df.format(date);
    Calendar cal1 = Calendar.getInstance();
    Calendar cal2 = Calendar.getInstance();
    Calendar cal3 = Calendar.getInstance();
    Calendar cal4 = Calendar.getInstance();
    Calendar cal5 = Calendar.getInstance();
    Calendar cal6 = Calendar.getInstance();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view_stats = inflater.inflate(R.layout.frame_stats, container, false);


        tv_foodsum = (TextView) view_stats.findViewById(R.id.tv_foodsum);
        tv_foodsum.setText("식비 : "+dbHelper.getCategory("식비","지출")+"원");
        cal1.setTime(new Date());
        cal2.setTime(new Date());
        cal3.setTime(new Date());
        cal4.setTime(new Date());
        cal5.setTime(new Date());
        cal6.setTime(new Date());

        cal1.add(Calendar.MONTH, -4);
        cal2.add(Calendar.MONTH, -3);
        cal3.add(Calendar.MONTH, -2);
        cal4.add(Calendar.MONTH, -1);
        // cal5 = current
        cal6.add(Calendar.MONTH, 1);


        Button btn_toBar = (Button) view_stats.findViewById(R.id.btn_toBar);
        Button btn_toPie = (Button) view_stats.findViewById(R.id.btn_toPie);

        btn_toBar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                pieChart.setVisibility(View.INVISIBLE);
                barChart.setVisibility(View.VISIBLE);
            }
        });

        btn_toPie.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                barChart.setVisibility(View.INVISIBLE);
                pieChart.setVisibility(View.VISIBLE);
            }
        });

        barChart = (BarChart) view_stats.findViewById(R.id.barchart);






        pieChart = (PieChart) view_stats.findViewById(R.id.chart);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setEntryLabelColor(Color.BLACK);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.BLACK);
        pieChart.setTransparentCircleRadius(61f);



        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();

        if(dbHelper.getCategory("식비", "지출") > 0) // 값이 0보다 클때만 add해줌으로써 0일때 글자만 나오는 현상 방지
        {
            yValues.add(new PieEntry(dbHelper.getCategory("식비", "지출"),"식비"));
        }
        if(dbHelper.getCategory("교통비", "지출") > 0)
        {
            yValues.add(new PieEntry(dbHelper.getCategory("교통비", "지출"),"교통비"));
        }
        if(dbHelper.getCategory("패션/미용", "지출") > 0)
        {
            yValues.add(new PieEntry(dbHelper.getCategory("패션/미용", "지출"),"패션/미용"));
        }
        if(dbHelper.getCategory("생필품", "지출") > 0)
        {
            yValues.add(new PieEntry(dbHelper.getCategory("생필품", "지출"),"생필품"));
        }
        if(dbHelper.getCategory("의류/미용", "지출") > 0)
        {
            yValues.add(new PieEntry(dbHelper.getCategory("의류/미용", "지출"),"의류/미용"));
        }
        if(dbHelper.getCategory("교육", "지출") > 0)
        {
            yValues.add(new PieEntry(dbHelper.getCategory("교육", "지출"),"교육"));
        }
        if(dbHelper.getCategory("통신비", "지출") > 0)
        {
            yValues.add(new PieEntry(dbHelper.getCategory("통신비", "지출"),"통신비"));
        }
        if(dbHelper.getCategory("저축", "지출") > 0)
        {
            yValues.add(new PieEntry(dbHelper.getCategory("저축", "지출"),"저축"));
        }
        if(dbHelper.getCategory("의료/건강", "지출") > 0)
        {
            yValues.add(new PieEntry(dbHelper.getCategory("의료/건강", "지출"),"의료/건강"));
        }
        if(dbHelper.getCategory("급여/용돈", "지출") > 0)
        {
            yValues.add(new PieEntry(dbHelper.getCategory("급여/용돈", "지출"),"급여/용돈"));
        }
        if(dbHelper.getCategory("기타", "지출") > 0)
        {
            yValues.add(new PieEntry(dbHelper.getCategory("기타", "지출"),"기타"));
        }



        Description description2 = new Description();
        description2.setText(""); //라벨
        description2.setTextSize(1);



        Description description = new Description();
        description.setText("카테고리별 지출"); //라벨
        description.setTextSize(10);
        pieChart.setDescription(description2);
        //pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션


        PieDataSet dataSet = new PieDataSet(yValues,"카테고리");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(20f);
        data.setValueTextColor(Color.DKGRAY);

        pieChart.setData(data);

        BarDataSet barDataSet = new BarDataSet(data1(), "최근 5개월 지출변화");
        BarData barData = new BarData();
        barData.addDataSet(barDataSet);
        barData.setBarWidth(0.35f);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(false);
        leftAxis.setGranularity(1000f);


        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setEnabled(false);

        barChart.setData(barData);
        barChart.invalidate();
        barChart.setDescription(description2);
        barChart.setVisibility(View.INVISIBLE);
        barChart.setScaleEnabled(false);
        barChart.setPinchZoom(false);


        return view_stats;
    }

    private ArrayList<BarEntry> data1() // 현재 Month부터 5개월치 지출
    {
        ArrayList<BarEntry> data_val = new ArrayList<>();
        data_val.add(new BarEntry(Integer.parseInt(df.format(cal1.getTime())), dbHelper.monthSearchTest("2021-"+df.format(cal1.getTime())+"-01","2021-"+df.format(cal2.getTime())+"-01","지출")));
        data_val.add(new BarEntry(Integer.parseInt(df.format(cal2.getTime())), dbHelper.monthSearchTest("2021-"+df.format(cal2.getTime())+"-01","2021-"+df.format(cal3.getTime())+"-01","지출")));
        data_val.add(new BarEntry(Integer.parseInt(df.format(cal3.getTime())), dbHelper.monthSearchTest("2021-"+df.format(cal3.getTime())+"-01","2021-"+df.format(cal4.getTime())+"-01","지출")));
        data_val.add(new BarEntry(Integer.parseInt(df.format(cal4.getTime())), dbHelper.monthSearchTest("2021-"+df.format(cal4.getTime())+"-01","2021-"+df.format(cal5.getTime())+"-01","지출")));
        data_val.add(new BarEntry(Integer.parseInt(df.format(cal5.getTime())), dbHelper.monthSearchTest("2021-"+df.format(cal5.getTime())+"-01","2021-"+df.format(cal6.getTime())+"-01","지출")));


        return data_val;
    }
}
