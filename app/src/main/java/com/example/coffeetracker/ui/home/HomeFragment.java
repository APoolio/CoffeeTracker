package com.example.coffeetracker.ui.home;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.lifecycle.Observer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.coffeetracker.R;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

public class HomeFragment extends Fragment
{
    private HomeViewModel homeViewModel;

    //Plus and Minus buttons
    private ImageButton mPlusButton;
    private ImageButton mMinusButton;
    //TextView to display coffee number
    private TextView mCoffeeNumber;

    private NotificationManager mNotificationManager;
    //ID for notification
    private static final int NOTIFICATION_ID = 0;
    //ID for our notification channel
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    //keeps track of coffee number
    private int num;

    public static String numberExtra = "extra for coffee number";

    AlarmManager alarmManager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //Reference buttons
        mPlusButton = root.findViewById(R.id.increase);
        mMinusButton = root.findViewById(R.id.decrease);
        mCoffeeNumber = root.findViewById(R.id.coffeeNumber);

        //Creating the notification channel
        createNotificationChannel();

        alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);

        //Initializing our Notification Manager
        //Have to use getActivity() since calling in a fragment and fragment does not extend Context
        //So we are calling getActivity() to reference the activity
        mNotificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);

        //Get the coffee count number passed through the notification intent
        Intent intent = getActivity().getIntent();
        String coffeeCount = Integer.toString(intent.getIntExtra(numberExtra, 0));
        num = intent.getIntExtra(numberExtra, 0);
        //Toast.makeText(getActivity(), test, Toast.LENGTH_SHORT).show();

        mCoffeeNumber.setText(coffeeCount);

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
                initiateNotification();
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

    public void initiateNotification()
    {

        Toast.makeText(getActivity(), "Start Notification Timer", Toast.LENGTH_SHORT).show();
        long interval = AlarmManager.INTERVAL_HOUR;

        Intent notifyIntent = new Intent(getActivity(), AlarmReceiver.class);
        notifyIntent.putExtra(numberExtra, num);

        boolean alarmUp = (PendingIntent.getBroadcast(getActivity(), NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_NO_CREATE) != null);
        PendingIntent notifyPendingIntent = PendingIntent.getBroadcast(getActivity(), NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //If the alarm does not exist then create it
        if(!alarmUp) alarmManager.setExact(AlarmManager.ELAPSED_REALTIME, interval, notifyPendingIntent);

        //If the user adds another cup of coffee then reset the timer
        else
        {
            //Cancel the alarm
            if(alarmManager != null)
                alarmManager.cancel(notifyPendingIntent);

            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME, interval, notifyPendingIntent);
        }
    }

    public void createNotificationChannel()
    {
        // Create a notification manager object.
        mNotificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);

        //Creating our notificationChannel with our desired parameters
        NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID, "Stand up Notification!", NotificationManager.IMPORTANCE_HIGH);

        //Our set parameters
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setDescription("Notifies every 15 minutes to stand up and walk");
        mNotificationManager.createNotificationChannel(notificationChannel);
    }
}
