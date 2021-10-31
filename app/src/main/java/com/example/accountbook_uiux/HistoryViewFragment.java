package com.example.accountbook_uiux;

import static com.example.accountbook_uiux.MainActivity.dbHelper;
import static com.example.accountbook_uiux.MainActivity.mContext;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class HistoryViewFragment extends Fragment {

    private View view_history;
    Button btn_searchdate, btn_searchbasic;
    EditText et_startdate, et_enddate;



    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateStartDate();
        }
    };
    DatePickerDialog.OnDateSetListener myDatePicker2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateEndDate();
        }
    };

    ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view_history = inflater.inflate(R.layout.frame_history, container, false);

        listView = (ListView) view_history.findViewById(R.id.lv_list);
        btn_searchdate = (Button) view_history.findViewById(R.id.btn_searchdate);
        btn_searchbasic = (Button) view_history.findViewById(R.id.btn_searchbasic);
        et_startdate = (EditText) view_history.findViewById(R.id.et_startdate);
        et_enddate = (EditText) view_history.findViewById(R.id.et_enddate);

        // 년, 월, 달을 Spinner로 추가해서 검색하기.

        getTableData();

        et_startdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new DatePickerDialog(getContext(), myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        et_enddate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new DatePickerDialog(getContext(), myDatePicker2, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btn_searchdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String startDate = et_startdate.getText().toString();
                String endDate = et_enddate.getText().toString();
                searchByDate(startDate,endDate);
            }
        });

        btn_searchbasic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getTableData();
            }
        });

        return view_history;
    }

    private void updateStartDate() {

        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
        EditText et_date = (EditText) view_history.findViewById(R.id.et_startdate);
        et_date.setText(sdf.format(myCalendar.getTime()));
    }
    private void updateEndDate() {

        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
        EditText et_date = (EditText) view_history.findViewById(R.id.et_enddate);
        et_date.setText(sdf.format(myCalendar.getTime()));
    }


    public void getTableData()
    {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor;

        ListViewAdapter adapter = new ListViewAdapter();

        cursor = db.rawQuery("SELECT * FROM Accountbook",null);
        while(cursor.moveToNext())
        {
            adapter.addItemToList(cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6) );
        }

        listView.setAdapter(adapter);

    }

    public void searchByDate(String _dateStart, String _dateEnd)
    {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor;

        ListViewAdapter adapter = new ListViewAdapter();

        cursor = db.rawQuery("SELECT * FROM AccountBook WHERE date >= '"+ _dateStart +"' AND date <= '"+ _dateEnd +"'",null);
        while(cursor.moveToNext())
        {
            adapter.addItemToList(cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6) );

        }

        listView.setAdapter(adapter);

    }
}