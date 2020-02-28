package com.example.coffeetracker.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.coffeetracker.R;

public class HomeFragment extends Fragment
{
    private HomeViewModel homeViewModel;

    //Plus and Minus buttons
    private ImageButton mPlusButton;
    private ImageButton mMinusButton;
    //TextView to display coffee number
    private TextView mCoffeeNumber;

    private int num;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mPlusButton = root.findViewById(R.id.increase);
        mMinusButton = root.findViewById(R.id.decrease);

        mCoffeeNumber = root.findViewById(R.id.coffeeNumber);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>()
        {
            @Override
            public void onChanged(@Nullable String s)
            {
                //textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // bind the views here.
        mPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num++;
                mCoffeeNumber.setText(String.valueOf(num));
            }
        });


        // bind the views here.
        mMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num != 0)
                {
                    num--;
                    mCoffeeNumber.setText(String.valueOf(num));
                }
            }
        });
    }
}
