package com.example.coffeetracker;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

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

    public Coffee(@NonNull String date, int count)
    {
        this.mCount = count;
        this.mDate = date;
    }

    public String getDate() {return this.mDate; }

    public int getCount() { return this.mCount; }

    public void setDate (String date) { this.mDate = date; }
}
