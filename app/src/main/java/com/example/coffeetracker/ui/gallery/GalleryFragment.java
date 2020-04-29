package com.example.coffeetracker.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.coffeetracker.Coffee;
import com.example.coffeetracker.CoffeeDao;
import com.example.coffeetracker.CoffeeRoomDatabase;
import com.example.coffeetracker.CoffeeViewModel;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.coffeetracker.R;
import com.example.coffeetracker.ui.home.HomeFragment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class GalleryFragment extends Fragment implements View.OnClickListener
{

    private GalleryViewModel galleryViewModel;
    //ViewModel
    private CoffeeViewModel mCoffeeViewModel;

    private Coffee coffeeOBJ;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        //Associating our ViewModel with our UI controller
        //ViewModelProviders creates and manages ViewModels
        mCoffeeViewModel = ViewModelProviders.of(this).get(CoffeeViewModel.class);

        root.findViewById(R.id.five).setOnClickListener(this);
        root.findViewById(R.id.four).setOnClickListener(this);
        root.findViewById(R.id.three).setOnClickListener(this);
        root.findViewById(R.id.two).setOnClickListener(this);
        root.findViewById(R.id.one).setOnClickListener(this);

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        //Retrieving the current count for the current day
        CoffeeDao coffeeDao = CoffeeRoomDatabase.getDatabase(getContext()).coffeeDao();
        coffeeOBJ = coffeeDao.findCoffeeObj(HomeFragment.currentDate());
        if(coffeeOBJ != null)
        {

        }

        mCoffeeViewModel = ViewModelProviders.of(this).get(CoffeeViewModel.class);
    }

    @Override
    public void onClick(View view)
    {
        int prodLevel = 0;

        switch(view.getId())
        {
            case R.id.one:
                Log.d("TEST PROD: ", "HELLO 1");
                prodLevel = 1;
                break;
            case R.id.two:
                Log.d("TEST PROD: ", "HELLO 2");
                prodLevel = 2;
                break;
            case R.id.three:
                Log.d("TEST PROD: ", "HELLO 3");
                prodLevel = 3;
                break;
            case R.id.four:
                Log.d("TEST PROD: ", "HELLO 4");
                prodLevel = 4;
                break;
            case R.id.five:
                Log.d("TEST PROD: ", "HELLO 5");
                prodLevel = 5;
                break;
        }

        if((coffeeOBJ != null) && (prodLevel != 0))
        {
            ArrayList<Integer> coffeeProdLevels = new ArrayList<>();
            ArrayList<String> coffeeProdLevelTimes = new ArrayList<>();

            coffeeProdLevels = coffeeOBJ.getProductivity();
            coffeeProdLevelTimes = coffeeOBJ.getProductivityTime();

            coffeeProdLevels.add(prodLevel);
            coffeeProdLevelTimes.add(HomeFragment.additionTime());

           coffeeOBJ.setProductivity(coffeeProdLevels);
           coffeeOBJ.setProductivityTime(coffeeProdLevelTimes);

           mCoffeeViewModel.insert(coffeeOBJ);
        }
    }
}
