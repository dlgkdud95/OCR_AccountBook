package com.example.accountbook_uiux;


import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;
import android.util.Log;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper
{
    private static final int DB_VERSION = 5;
    private static final String DB_NAME = "accountbook.db";

    public DBHelper(@Nullable Context context)
    {
        super(context, DB_NAME,null, DB_VERSION);
    }

    //context = getApplicationContext?
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //DB 생성시 호출
        db.execSQL("CREATE TABLE IF NOT EXISTS AccountBook (id INTEGER PRIMARY KEY AUTOINCREMENT, type TEXT NOT NULL, cost INTEGER NOT NULL, category TEXT NOT NULL, date TEXT NOT NULL, payment TEXT NOT NULL, detail TEXT NOT NULL, itemName TEXT NOT NULL, itemPrice INTEGER NOT NULL, itemCount INTEGER NOT NULL, isItem TEXT NOT NULL)");
        // type = 입력타입(수입, 지출)
        // cost = 금액
        // category = 카테고리
        // date = 날짜
        // payment = 결제방식
        // detail = 기타
        // itemName = 품목 이름
        // itemPrice = 품목 가격
        // itemCount = 품목 개수
        // isItem = 품목기능인지 체크

    }


    public String getItemName(String _isItem)
    {

        String item = "";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM AccountBook WHERE isItem = '"+ _isItem +"' ",null);


        while(cursor.moveToNext())
        {
            item += cursor.getString(4); // 날짜
            item += " : ";
            item += cursor.getString(7); // 이름
            item += ", ";
            item += Integer.toString(cursor.getInt(8)); //가격
            item += "원";
            item += "\n";
        }
        cursor.close();
        return item;
    }




    public int getSum(String _type)
    {
        // _type변수에 table에 있는 type(수입 or 지출)을 넣어주면 type에 맞는 합계를 구해줌
        int number = 0;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT IFNULL(AccountBook.cost, 0) FROM AccountBook WHERE type = '"+_type+"'",null);

        while(cursor.moveToNext())
        {
            number += cursor.getInt(0);
        }
        cursor.close();
        return number;
    }

    public int getCategory(String _category, String _type)
    {

        int number = 0;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT IFNULL(AccountBook.cost, 0) FROM AccountBook WHERE category = '"+_category+"' AND type = '"+ _type+"' ",null);

        while(cursor.moveToNext())
        {
            number += cursor.getInt(0);
        }
        cursor.close();
        return number;
    }

    public int monthSearchTest(String thisMonth, String nextMonth, String _type)
    {
        int number = 0;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor;

        cursor = db.rawQuery("SELECT * FROM AccountBook WHERE date >= '"+ thisMonth +"' AND date < '"+ nextMonth +"' AND type = '"+ _type +"'",null);

        while(cursor.moveToNext())
        {
            number += cursor.getInt(2);
        }
        cursor.close();
        return number;
    }

    public ArrayList<DBTable> getDataByDate(String _date)
    {
        ArrayList<DBTable> list = new ArrayList<DBTable>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM AccountBook WHERE date = '"+_date +"' ",null);
        while(cursor.moveToNext())
        {
            DBTable table = new DBTable();
            table.setType(cursor.getString(1));
            table.setCost(cursor.getInt(2));
            table.setCategory(cursor.getString(3));

            list.add(table);
        }

        return list;
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS AccountBook");
        onCreate(db);
    }


    // DB에 데이터 넣기
    public void InsertDB(String _type, int _cost, String _category, String _date, String _payment, String _detail, String _itemName, int _itemPrice, int _itemCount, String _isItem)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO AccountBook(type, cost, category, date, payment, detail, itemName, itemPrice, itemCount, isItem) VALUES('"+ _type +"', "+ _cost +", '"+ _category +"', '"+ _date +"', '"+ _payment +"', '"+ _detail +"', '"+ _itemName +"', " + _itemPrice +", "+ _itemCount +", '"+ _isItem +"');");
    }

    // 업데이트는 써야해야할지 아직 못정함
    public void UpdateDB()
    {
        SQLiteDatabase db = getWritableDatabase();
    }


    // DB전체 삭제
    public void DeleteDB()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM AccountBook");
    }
}
