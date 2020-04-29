package com.example.coffeetracker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface CoffeeDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Coffee coffee);

    @Query("UPDATE coffee_table SET coffeeCount = :count AND timeList = :times WHERE date = :date")
    int updateCount(String date, int count, ArrayList<String> times);

    @Query("SELECT * FROM coffee_table")
    LiveData<List<Coffee>> getAllCoffee();

    @Query("SELECT * FROM coffee_table WHERE date = :date")
    LiveData<Coffee> findCoffee(String date);

    @Query("SELECT * FROM coffee_table WHERE date = :date")
    Coffee findCoffeeObj(String date);
}
