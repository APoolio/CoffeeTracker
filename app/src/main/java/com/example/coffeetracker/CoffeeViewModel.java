package com.example.coffeetracker;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;

import com.example.coffeetracker.Coffee;
import com.example.coffeetracker.CoffeeRepository;

public class CoffeeViewModel extends AndroidViewModel
{
    //Reference to the repo
    private CoffeeRepository mRepository;

    public CoffeeViewModel(Application application)
    {
        super(application);

        //Getting a list of all words from the WordRepo
        mRepository = new CoffeeRepository(application);
    }

    //Inserting a word into the repo which inserts it into the db
    //Called from the MainActivity in onActivityResult and used the Repository insert to insert into the DB Room
    public void insert(Coffee coffee) { mRepository.insert(coffee); }

    public void updateCount(Coffee coffee) { mRepository.updateCount(coffee); }
}
