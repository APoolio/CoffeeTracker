package com.example.coffeetracker;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Coffee.class}, version = 3, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class CoffeeRoomDatabase extends RoomDatabase
{
    public abstract CoffeeDao coffeeDao();

    //Makes the class a singleton so we can only have one instance of the database opened
    private static CoffeeRoomDatabase INSTANCE;

    public static CoffeeRoomDatabase getDatabase(final Context context)
    {
        //If database instance is null then create it
        if (INSTANCE == null)
        {
            synchronized (CoffeeRoomDatabase.class)
            {
                if (INSTANCE == null)
                {
                    //Using Room's database builder to create the RoomDatabase object with the name word_database
                    //Creating the actual database
                    //Wipes and rebuilds instead of migrating
                    //.fallbackToDestructiveMigration() - if no Migration object. Migration is not part of this practical
                    //.addCallback() adds the RoomDatabase.Callback to add a method for when the room db is built
                    //.allowMainThreadQueries() -- fixes thread locking issue but is not efficient
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CoffeeRoomDatabase.class, "coffee_database").allowMainThreadQueries().fallbackToDestructiveMigration().build();
                }
            }
        }
        //return if created or already exists
        return INSTANCE;
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final CoffeeDao coffeeDao;
        public PopulateDbAsync(CoffeeRoomDatabase instance) {
            coffeeDao = instance.coffeeDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }

}
