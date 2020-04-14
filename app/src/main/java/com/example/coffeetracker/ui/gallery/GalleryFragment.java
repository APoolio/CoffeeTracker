package com.example.coffeetracker.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.coffeetracker.R;

public class GalleryFragment extends Fragment implements View.OnClickListener
{

    private GalleryViewModel galleryViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        root.findViewById(R.id.five).setOnClickListener(this);
        root.findViewById(R.id.four).setOnClickListener(this);
        root.findViewById(R.id.three).setOnClickListener(this);
        root.findViewById(R.id.two).setOnClickListener(this);
        root.findViewById(R.id.one).setOnClickListener(this);

        return root;
    }


    @Override
    public void onClick(View view){
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
