package com.example.accountbook_uiux;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter
{
    ArrayList<DBTable> list = new ArrayList<DBTable>();


    @Override
    public int getCount()
    {
        return list.size();
    }

    public Object getItem(int i)
    {
        return list.get(i);
    }
    public long getItemId(int i)
    {
        return i;
    }

    public View getView(int i, View view, ViewGroup viewGroup)
    {
        final Context context = viewGroup.getContext();
        if(view==null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_listview,viewGroup,false);
        }
        TextView tv_type = (TextView) view.findViewById(R.id.item_tv_type);
        TextView tv_cost = (TextView) view.findViewById(R.id.item_tv_cost);
        TextView tv_date = (TextView) view.findViewById(R.id.item_tv_date);
        TextView tv_category = (TextView) view.findViewById(R.id.item_tv_category);
        TextView tv_detail = (TextView) view.findViewById(R.id.item_tv_detail);
        TextView tv_pamyent = (TextView) view.findViewById(R.id.item_tv_payment);

        DBTable listdata = list.get(i);

        tv_type.setText(listdata.getType());
        tv_cost.setText(Integer.toString(listdata.getCost()));
        tv_date.setText(listdata.getDate());
        tv_category.setText(listdata.getCategory());
        tv_detail.setText(listdata.getDetail());
        tv_pamyent.setText(listdata.getPayment());

        return view;
    }

    public void addItemToList(String type, int cost, String category, String date, String payment, String detail)
    {
        DBTable listdata = new DBTable();
        if(type.equals("수입") || type.equals("지출")) // 품목도 리스트뷰에 뜨는걸 방지
        {
            listdata.setType(type);
            listdata.setCost(cost);
            listdata.setCategory(category);
            listdata.setDate(date);
            listdata.setPayment(payment);
            listdata.setDetail(detail);
            list.add(listdata);
        }
        else
        {
            // 아무것도 안하기!
        }

    }
}
