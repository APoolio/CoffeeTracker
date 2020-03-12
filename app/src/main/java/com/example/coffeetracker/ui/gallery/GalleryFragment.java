package com.example.coffeetracker.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.coffeetracker.R;

public class GalleryFragment extends Fragment
{

    private GalleryViewModel galleryViewModel;

    private RadioGroup mRadioGroup;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState)
    {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        mRadioGroup = root.findViewById(R.id.radioGroup);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                // checkedId is the RadioButton selected
                switch (checkedId)
                {
                    case R.id.radio_1:
                        // switch to fragment 1
                        break;
                    case R.id.radio_2:
                        // switch to fragment 1
                        break;
                    case R.id.radio_3:
                        // switch to fragment 1
                        break;
                    case R.id.radio_4:
                        // switch to fragment 1
                        break;
                    case R.id.radio_5:
                        // switch to fragment 1
                        break;
                    case R.id.radio_6:
                        // switch to fragment 1
                        break;
                    case R.id.radio_7:
                        // switch to fragment 1
                        break;
                    case R.id.radio_8:
                        // switch to fragment 1
                        break;
                    case R.id.radio_9:
                        // switch to fragment 1
                        break;
                    case R.id.radio_10:
                        // switch to fragment 1
                        break;
                }
            }
        });

        return root;
    }
}
