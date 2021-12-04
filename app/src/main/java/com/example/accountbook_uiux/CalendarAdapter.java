package com.example.accountbook_uiux;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {

    private final ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;

    public CalendarAdapter(ArrayList<String> daysOfMonth, OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
        this.daysOfMonth = daysOfMonth;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //뷰홀더 생성, 레이아웃 생성

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);

        return new CalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) { //뷰홀더가 재활용될 때 실행

        holder.dayOfMonth.setText(daysOfMonth.get(position));
        holder.linearLayout.setBackgroundResource(R.drawable.background_border_left_top);

    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    } //아이템 개수 조회,여기서 받은 값만큼 칸을 만듦 (=닫력 칸)

    public interface OnItemListener {
        void onItemClick(int position, String dayText);
    }
}
