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
import com.example.coffeetracker.CoffeeDao;
import com.example.coffeetracker.CoffeeRoomDatabase;
import com.example.coffeetracker.CoffeeViewModel;
import com.example.coffeetracker.MyXAxisValueFormatter;
import com.example.coffeetracker.MyYAxisValueFormatter;
import com.example.coffeetracker.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    public Coffee retrievedCoffee = null;

    //Coffee size picker
    private NumberPicker coffeeSizePicker;

    //Selected coffee size (default is 8)
    private String selectedCoffeeSize = "1";

    //

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

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

        initiateGraph();

        //Creating the notification channel
        createNotificationChannel();

        alarmManager = (AlarmManager) Objects.requireNonNull(getActivity()).getSystemService(ALARM_SERVICE);

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

        //Retrieving the current count for the current day
        CoffeeDao coffeeDao = CoffeeRoomDatabase.getDatabase(getContext()).coffeeDao();
        Coffee coffeeOBJ = coffeeDao.findCoffeeObj(mDateTextView.getText().toString());
        if(coffeeOBJ != null)
        {
            mCoffeeNumber.setText(Integer.toString(coffeeOBJ.getCount()));
            num = coffeeOBJ.getCount();
        }

        // bind the views here.
        mPlusButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                num++;
                mCoffeeNumber.setText(String.valueOf(num));

                DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                Date date = new Date();

                Coffee coffee = new Coffee(mDateTextView.getText().toString(), num, times, coffeeSizeList);
                if (num == 1)
                {
                    Log.d("TIme: ", formatter.format(date));
                    coffee.getTimes().add(formatter.format(date));
                    coffee.getCoffeeSizes().add(selectedCoffeeSize);
                    mCoffeeViewModel.insert(coffee);
                }
                else if(num > 1)
                {
                    if (retrievedCoffee != null)
                    {
                        Log.d("TIme: ", formatter.format(date));
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
                    //Coffee coffee = new Coffee(mDateTextView.getText().toString(), num, times, coffeeSizeList);
                    if(retrievedCoffee != null)
                    {
                        int sizeLength = retrievedCoffee.getCoffeeSizes().size();
                        int timeLength = retrievedCoffee.getTimes().size();

                        retrievedCoffee.getCoffeeSizes().remove(sizeLength-1);
                        retrievedCoffee.getTimes().remove(timeLength-1);
                    }
                    //mCoffeeViewModel.insert(coffee);
                }
            }
        });

        mCoffeeViewModel.findCoffeeVM(mDateTextView.getText().toString()).observe(getViewLifecycleOwner(), new Observer<Coffee>()
        {
            @Override
            public void onChanged(Coffee coffee)
            {
                ArrayList CoffeeCords = new ArrayList();
                float percentage;
                float hour;
                float minutes;
                float total;
                String[] nums;
                if(coffee != null)
                {
                    //referenceTime = Long.parseLong(coffee.getTimes().get(0));
                    //Creating data entries (cords) for the graph
                    for(int i = 0; i < coffee.getCount(); i++)
                    {
                        nums = coffee.getTimes().get(i).split(":",3);
                        hour = Float.parseFloat(nums[0]);
                        minutes = Float.parseFloat(nums[1]);
                        percentage = minutes / 60f;
                        total = hour + percentage;

                        CoffeeCords.add(new BarEntry(total, Float.parseFloat(coffee.getCoffeeSizes().get(i))));
                    }
                    productivityChart.animateY(5000);
                }

                BarDataSet bardataset = new BarDataSet(CoffeeCords, "Coffee Count");
                BarData data = new BarData(bardataset);
                bardataset.setColors(ColorTemplate.MATERIAL_COLORS);
                data.setBarWidth(0.1f);

                productivityChart.zoom(3f, 1f, 400f, 3f);
                //productivityChart.zoomAndCenterAnimated(3, 1, 400f, 3f, YAxis.AxisDependency.RIGHT, 4000);
                productivityChart.setData(data);
            }
        });

        /*mCoffeeViewModel.getAllCoffee().observe(getViewLifecycleOwner(), new Observer<List<Coffee>>()
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
        });*/
    }

    private  void initiatePicker()
    {
        //Values for picker
        final String[] pickerValues;
        final String[] intPickerValues;

        coffeeSizePicker.setMaxValue(3);
        coffeeSizePicker.setMinValue(0);

        pickerValues = new String[] {"8 oz.", "12 oz.", "16 oz.", "20 oz.", "24 oz."};
        intPickerValues = new String[] {"1", "2", "3", "4", "5"};
        coffeeSizePicker.setDisplayedValues(pickerValues);

        coffeeSizePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1)
            {
                int valuePicked = coffeeSizePicker.getValue();
                selectedCoffeeSize = intPickerValues[valuePicked];
                Log.d("valuePicked: ", pickerValues[valuePicked]);
            }
        });


    }

    //Creating the layout of the chart
    private void initiateGraph()
    {
        final String[] times = new String[] {"12 AM","1 AM", "2 AM", "3 AM", "4 AM", "5 AM", "6 AM", "7 AM", "8 AM", "9 AM", "10 AM", "11 AM", "12 PM", "1 PM", "2 PM", "3 PM", "4 PM", "5 PM", "6 PM", "7 PM", "8 PM", "9 PM", "10 PM", "11 PM", "12 AM"};

        XAxis xAxis = productivityChart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(times));
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setGranularity(1f);
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(23f);

        final String[] sizes = new String[] {"0 oz.", "8 oz.", "12 oz.","16 oz.","20 oz.","24 oz."};

        YAxis left = productivityChart.getAxisLeft();
        left.setValueFormatter(new MyYAxisValueFormatter(sizes));
        left.setAxisMinimum(0);
        left.setAxisMaximum(5);
        left.setGranularity(1);

        final String[] prodScale = new String[] {"0","1","2","3","4","5"};

        YAxis right = productivityChart.getAxisRight();
        right.setValueFormatter(new MyYAxisValueFormatter(prodScale));
        right.setAxisMinimum(0);
        right.setAxisMaximum(5);
        right.setGranularity(1);
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

            assert alarmManager != null;
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME, interval, notifyPendingIntent);
        }
    }

    private void createNotificationChannel()
    {
        // Create a notification manager object.
        mNotificationManager = (NotificationManager) Objects.requireNonNull(getActivity()).getSystemService(NOTIFICATION_SERVICE);

        //Creating our notificationChannel with our desired parameters
        NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID, "Track Productivity!", NotificationManager.IMPORTANCE_HIGH);

        //Our set parameters
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setDescription("Notifies the user to input their productivity level after an hour of consuming coffee");
        mNotificationManager.createNotificationChannel(notificationChannel);
    }
}
