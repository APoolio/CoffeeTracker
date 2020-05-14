package com.example.coffeetracker;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.lang.reflect.Array;
import java.util.ArrayList;

//Ha! Coffee Table
@Entity(tableName = "coffee_table")
public class Coffee
{
    /* Table info */
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

    @ColumnInfo(name = "prodTimeList")
    private ArrayList<String> mProductivityTime = new ArrayList<>();

    @ColumnInfo(name = "productivityList")
    private ArrayList<Integer> mProductivity = new ArrayList<>();

    @ColumnInfo(name = "totalConsumed")
    private int mTotalConsumed;

    /* Coffee constructor */
    public Coffee(@NonNull String date, int count, ArrayList<String> times, ArrayList<String> coffeeSizes, ArrayList<String> productivityLevelTimes, ArrayList<Integer> productivityLevels, int totalConsumed)
    {
        this.mCount = count;
        this.mDate = date;
        this.mTimes.addAll(times);
        this.mCoffeeSizes.addAll(coffeeSizes);
        this.mProductivityTime.addAll(productivityLevelTimes);
        this.mProductivity.addAll(productivityLevels);
        this.mTotalConsumed = totalConsumed;
    }

    public Coffee(){}

    /* Getters and Setters */
    public String getDate() { return this.mDate; }

    public int getCount() { return this.mCount; }

    public ArrayList<String> getTimes() { return this.mTimes; }

    public ArrayList<Integer> getProductivity() { return this.mProductivity; }

    public ArrayList<String> getCoffeeSizes() { return this.mCoffeeSizes; }

    public ArrayList<String> getProductivityTime() { return this.mProductivityTime; }

    public int getTotalConsumed() { return this.mTotalConsumed; }

    public void setCount (int count) { this.mCount = count; }

    public void setDate (String date) { this.mDate = date; }

    public void setProductivity(ArrayList<Integer> prodLevels)
    {
        this.mProductivity = prodLevels;
    }

    public void setProductivityTime(ArrayList<String> prodLevelTimes)
    {
        this.mProductivityTime = prodLevelTimes;
    }

    public void setTimes (ArrayList<String> times)
    {
        this.mTimes = times;
    }

    public void setCoffeeSizes (ArrayList<String> sizes)
    {
        this.mCoffeeSizes = sizes;
    }

    public void setTotalConsumed (int consumed) { this.mTotalConsumed = consumed; }
}
