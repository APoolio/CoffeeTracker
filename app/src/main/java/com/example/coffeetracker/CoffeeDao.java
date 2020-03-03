package com.example.coffeetracker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CoffeeDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Coffee coffee);

    @Query("UPDATE coffee_table SET coffeeCount = :count WHERE date = :date")
    int updateCount(String date, int count);
}