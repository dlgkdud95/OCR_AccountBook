package com.example.accountbook_uiux;

import static com.example.accountbook_uiux.MainActivity.dbHelper;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class StatsViewFragment extends Fragment {

    private View view_stats;
    MainActivity mainActivity = new MainActivity();

    PieChart pieChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view_stats = inflater.inflate(R.layout.frame_stats, container, false);

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
        }if(dbHelper.getCategory("급여/용돈", "지출") > 0)
        {
            yValues.add(new PieEntry(dbHelper.getCategory("급여/용돈", "지출"),"급여/용돈"));
        }






        Description description = new Description();
        description.setText("카테고리별 지출"); //라벨
        description.setTextSize(15);
        pieChart.setDescription(description);

        //pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션


        PieDataSet dataSet = new PieDataSet(yValues,"카테고리");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(20f);
        data.setValueTextColor(Color.DKGRAY);

        pieChart.setData(data);

        return view_stats;
    }
}
