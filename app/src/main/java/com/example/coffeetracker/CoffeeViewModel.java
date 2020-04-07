package com.example.coffeetracker;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;

public class CoffeeViewModel extends AndroidViewModel
{
    //Reference to the repo
    private CoffeeRepository mRepository;
    private LiveData<List<Coffee>> mCoffee;
    private LiveData<Coffee> findCoffee;

    public CoffeeViewModel(Application application)
    {
        super(application);

        //Getting a list of all words from the WordRepo
        mRepository = new CoffeeRepository(application);
        mCoffee = mRepository.getAllCoffee();
    }

    //Inserting a word into the repo which inserts it into the db
    //Called from the MainActivity in onActivityResult and used the Repository insert to insert into the DB Room
    public void insert(Coffee coffee) { mRepository.insert(coffee); }

    public void updateCount(Coffee coffee) { mRepository.updateCount(coffee); }

    public LiveData<List<Coffee>> getAllCoffee() { return mCoffee; }

    public LiveData<Coffee> findCoffeeVM(String date)
    {
        //findCoffee.getValue().getCount();
        //Log.d("Test Coffee: ", findCoffee.getValue().get(0).getDate());
        return mRepository.getFoundCoffee(date);
    }
}
