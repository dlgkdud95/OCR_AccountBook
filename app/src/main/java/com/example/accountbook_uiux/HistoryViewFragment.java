package com.example.accountbook_uiux;

import static com.example.accountbook_uiux.MainActivity.dbHelper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class HistoryViewFragment extends Fragment {

    private View view_history;


    ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view_history = inflater.inflate(R.layout.frame_history, container, false);

        listView = (ListView) view_history.findViewById(R.id.lv_list);

        getTableData();

        return view_history;
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
}