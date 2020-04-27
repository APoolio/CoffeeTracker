package com.example.coffeetracker.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.coffeetracker.Coffee;
import com.example.coffeetracker.CoffeeViewModel;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.coffeetracker.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GalleryFragment extends Fragment implements View.OnClickListener
{

    private GalleryViewModel galleryViewModel;
    //ViewModel
    private CoffeeViewModel mCoffeeViewModel;

    private String stringDate = null;

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

        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, MMM dd");
        stringDate = date.format(formatter);

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        mCoffeeViewModel.findCoffeeVM(stringDate).observe(getViewLifecycleOwner(), new Observer<Coffee>()
        {
            @Override
            public void onChanged(Coffee coffee)
            {
                Log.d("Count: ", Integer.toString(coffee.getCount()));
            }
        });
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.one:
                Log.d("TEST PROD: ", "HELLO 1");
                break;
            case R.id.two:
                Log.d("TEST PROD: ", "HELLO 2");
                break;
            case R.id.three:
                Log.d("TEST PROD: ", "HELLO 3");
                break;
            case R.id.four:
                Log.d("TEST PROD: ", "HELLO 4");
                break;
            case R.id.five:
                Log.d("TEST PROD: ", "HELLO 5");
                break;
        }
    }
}
