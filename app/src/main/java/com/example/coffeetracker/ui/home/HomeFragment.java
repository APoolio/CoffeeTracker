package com.example.coffeetracker.ui.home;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.coffeetracker.Coffee;
import com.example.coffeetracker.CoffeeViewModel;
import com.example.coffeetracker.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private TextView mDateTextView;

    private BarChart productivityChart;

    //ViewModel
    private CoffeeViewModel mCoffeeViewModel;

    //Used for notification alarm
    private AlarmManager alarmManager;

    private Coffee retrievedCoffee = null;

    //Coffee size picker
    private NumberPicker coffeeSizePicker;

    //Selected coffee size (default is 8)
    private String selectedCoffeeSize = "8 oz.";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        BarChart productivityChart;

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //Reference buttons
        mPlusButton = root.findViewById(R.id.increase);
        mMinusButton = root.findViewById(R.id.decrease);
        mCoffeeNumber = root.findViewById(R.id.coffeeNumber);

        mDateTextView = root.findViewById(R.id.coffeeTitle);

        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, MMM dd");
        String stringDate = date.format(formatter);
        mDateTextView.setText(stringDate);

        coffeeSizePicker = root.findViewById(R.id.coffeeSize);
        initiatePicker();


        //Reference to chart
        productivityChart = root.findViewById(R.id.barchart);

        ArrayList NoOfEmp = new ArrayList();

        NoOfEmp.add(new BarEntry(10f, 0));
        NoOfEmp.add(new BarEntry(20f, 1));
        NoOfEmp.add(new BarEntry(25f, 2));
        NoOfEmp.add(new BarEntry(30f, 3));
        NoOfEmp.add(new BarEntry(44.5f, 4));
        NoOfEmp.add(new BarEntry(50f, 5));
        NoOfEmp.add(new BarEntry(56f, 6));
        NoOfEmp.add(new BarEntry(60f, 7));
        NoOfEmp.add(new BarEntry(70f, 8));
        NoOfEmp.add(new BarEntry(72f, 9));

        ArrayList year = new ArrayList();

        year.add("2008");
        year.add("2009");
        year.add("2010");
        year.add("2011");
        year.add("2012");
        year.add("2013");
        year.add("2014");
        year.add("2015");
        year.add("2016");
        year.add("2017");

        BarDataSet bardataset = new BarDataSet(NoOfEmp, "No Of Employee");
        productivityChart.animateY(5000);
        BarData data = new BarData(bardataset);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        productivityChart.setData(data);

        //Creating the notification channel
        createNotificationChannel();

        alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);

        //Initializing our Notification Manager
        //Have to use getActivity() since calling in a fragment and fragment does not extend Context
        //So we are calling getActivity() to reference the activity
        mNotificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);

        /*//Get the coffee count number passed through the notification intent
        Intent intent = getActivity().getIntent();
        String coffeeCount = Integer.toString(intent.getIntExtra(numberExtra, 0));
        //num = intent.getIntExtra(numberExtra, 0);
        //Toast.makeText(getActivity(), test, Toast.LENGTH_SHORT).show();

        mCoffeeNumber.setText(coffeeCount);*/

        //Associating our ViewModel with our UI controller
        //ViewModelProviders creates and manages ViewModels
        mCoffeeViewModel = ViewModelProviders.of(this).get(CoffeeViewModel.class);

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        final ArrayList<String> times = new ArrayList<>();
        final ArrayList<String> coffeeSizeList = new ArrayList<>();

        // bind the views here.
        mPlusButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                num++;
                mCoffeeNumber.setText(String.valueOf(num));

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();
                Coffee coffee = new Coffee(mDateTextView.getText().toString(), num, times, coffeeSizeList);
                if (num == 1)
                {
                    coffee.getTimes().add(formatter.format(date));
                    coffee.getCoffeeSizes().add(selectedCoffeeSize);
                    mCoffeeViewModel.insert(coffee);
                }
                else if(num > 1)
                {
                    if (retrievedCoffee != null)
                    {
                        ArrayList<String> retrievedTimes = retrievedCoffee.getTimes();
                        ArrayList<String> retrievedSizes = retrievedCoffee.getCoffeeSizes();
                        retrievedTimes.add(formatter.format(date));
                        retrievedSizes.add(selectedCoffeeSize);
                        Log.d("retrievedTimesCoffeeDate", retrievedCoffee.getDate());
                        Log.d("retrievedTimesCoffeeCount", Integer.toString(retrievedCoffee.getCount()));
                        Log.d("retrievedTimesCoffeeTime0", retrievedCoffee.getTimes().get(0));
                        Log.d("retrievedTimesCoffeeSizes", retrievedCoffee.getCoffeeSizes().get(0));
                        mCoffeeViewModel.insert(new Coffee(retrievedCoffee.getDate(), retrievedCoffee.getCount()+1, retrievedTimes, retrievedSizes));
                    }

                    initiateNotification();
                }

            }
        });

        // bind the views here.
        mMinusButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (num != 0)
                {
                    num--;
                    mCoffeeNumber.setText(Integer.toString(num));
                    Coffee coffee = new Coffee(mDateTextView.getText().toString(), num, times, coffeeSizeList);
                    mCoffeeViewModel.insert(coffee);

                    initiateNotification();
                }
            }
        });

        //String test = mCoffeeViewModel.findCoffee(mDateTextView.getText().toString()).getDate();
        //Log.d("Find coffee: ", test);

        mCoffeeViewModel.getAllCoffee().observe(getViewLifecycleOwner(), new Observer<List<Coffee>>()
        {
            @Override
            public void onChanged(List<Coffee> coffees)
            {
                //Non-efficient way to get the Coffee object but it will work for now
                for (int i = 0; i < coffees.size(); i++)
                {
                    if (coffees.get(i).getDate().equals(mDateTextView.getText().toString()))
                    {
                        retrievedCoffee = coffees.get(i);
                        mCoffeeNumber.setText(Integer.toString(retrievedCoffee.getCount()));
                        num = retrievedCoffee.getCount();
                    }
                }
            }
        });
    }

    private void initiatePicker()
    {
        //Values for picker
        final String[] pickerValues;

        coffeeSizePicker.setMaxValue(3);
        coffeeSizePicker.setMinValue(0);
        pickerValues = new String[] {"8 oz.", "12 oz.", "16 oz.", "20 oz."};
        coffeeSizePicker.setDisplayedValues(pickerValues);

        coffeeSizePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1)
            {
                int valuePicked = coffeeSizePicker.getValue();
                selectedCoffeeSize = pickerValues[valuePicked];
                Log.d("valuePicked: ", pickerValues[valuePicked]);
            }
        });


    }

    private void initiateNotification()
    {

        Toast.makeText(getActivity(), "Start Notification Timer", Toast.LENGTH_SHORT).show();
        long interval = AlarmManager.INTERVAL_HOUR;

        Intent notifyIntent = new Intent(getActivity(), AlarmReceiver.class);
        //For notification intent
        String numberExtra = "extra for coffee number";
        notifyIntent.putExtra(numberExtra, num);

        boolean alarmUp = (PendingIntent.getBroadcast(getActivity(), NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_NO_CREATE) != null);
        PendingIntent notifyPendingIntent = PendingIntent.getBroadcast(getActivity(), NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //If the alarm does not exist then create it
        if (!alarmUp)
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME, interval, notifyPendingIntent);

        //If the user adds another cup of coffee then reset the timer
        else
        {
            //Cancel the alarm
            if (alarmManager != null)
                alarmManager.cancel(notifyPendingIntent);

            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME, interval, notifyPendingIntent);
        }
    }

    private void createNotificationChannel()
    {
        // Create a notification manager object.
        mNotificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);

        //Creating our notificationChannel with our desired parameters
        NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID, "Track Productivity!", NotificationManager.IMPORTANCE_HIGH);

        //Our set parameters
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setDescription("Notifies every 15 minutes to stand up and walk");
        mNotificationManager.createNotificationChannel(notificationChannel);
    }
}
