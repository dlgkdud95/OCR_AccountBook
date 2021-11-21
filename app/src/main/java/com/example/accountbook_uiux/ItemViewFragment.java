package com.example.accountbook_uiux;

import static com.example.accountbook_uiux.MainActivity.dbHelper;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ItemViewFragment extends Fragment {

    private View view_item;

    TextView tv_itemTest, tv_itemview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view_item = inflater.inflate(R.layout.fragment_item_view, container, false);
        tv_itemTest = view_item.findViewById(R.id.tv_itemTest);
        tv_itemview = view_item.findViewById(R.id.tv_itemview);
        String itemString = dbHelper.getItemName("TRUE");


        tv_itemTest.setText(itemString);


        // 영수증 날짜 + 내용으로 연속적으로 스크롤뷰

        return view_item;
    }
}