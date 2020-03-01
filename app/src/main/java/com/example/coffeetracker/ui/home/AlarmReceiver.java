package com.example.coffeetracker.ui.home;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.core.app.NotificationCompat;

import com.example.coffeetracker.MainActivity;
import com.example.coffeetracker.R;

public class AlarmReceiver extends BroadcastReceiver
{

    private static final int NOTIFICATION_ID = 0; //ID for notification
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel"; //ID for our notification channel
    private NotificationManager mNotificationManager;
    public static String numberExtra = "extra for coffee number";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        //Initializing our Notification Manager
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Integer num = intent.getIntExtra(numberExtra, 0);

        //Pending Intent that we will use for the notification content intent
        Intent contentIntent = new Intent(context, MainActivity.class);
        //Putting the coffee count number in the notification intent
        contentIntent.putExtra(numberExtra, num);

        //FLAG_UPDATE_CURRENT flag tells the system to use the old Intent but replace the extras
        PendingIntent contentPendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Building our notification with our set parameters
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notificaiton_icon)
                .setContentTitle("Record Productivity Level")
                .setContentText("You should record your productivity level!")
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        //Deliver our notification to the notification bar
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
