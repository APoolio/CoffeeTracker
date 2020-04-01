package com.example.coffeetracker;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

//Ha! Coffee Table
@Entity(tableName = "coffee_table")
public class Coffee
{
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "date")
    private String mDate;

    @ColumnInfo(name = "coffeeCount")
    private int mCount;

    @ColumnInfo(name = "timeList")
    private ArrayList<String> mTimes = new ArrayList<>();

    @ColumnInfo(name = "sizeList")
    private ArrayList<String> mCoffeeSizes = new ArrayList<>();

    //@ColumnInfo(name = "productivityList")
    //private ArrayList<String> mProductivity = new ArrayList<>();

    public Coffee(@NonNull String date, int count, ArrayList<String> times, ArrayList<String> coffeeSizes)
    {
        this.mCount = count;
        this.mDate = date;
        this.mTimes.addAll(times);
        this.mCoffeeSizes.addAll(coffeeSizes);
    }

    public String getDate() { return mDate; }

    public int getCount() { return mCount; }

    public ArrayList<String> getTimes() { return mTimes; }

    public ArrayList<String> getProductivity() { return mTimes; }

    public ArrayList<String> getCoffeeSizes() { return mCoffeeSizes; }

    public void setCount (int count) { this.mCount = count; }

    public void setDate (String date) { this.mDate = date; }
}
