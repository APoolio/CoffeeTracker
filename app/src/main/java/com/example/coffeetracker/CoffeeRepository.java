package com.example.coffeetracker;

import android.app.Application;
import android.os.AsyncTask;

import com.example.coffeetracker.Coffee;
import com.example.coffeetracker.CoffeeDao;
import com.example.coffeetracker.CoffeeRoomDatabase;

public class CoffeeRepository
{
    private CoffeeDao mCoffeeDao;

    CoffeeRepository(Application application)
    {
        CoffeeRoomDatabase db = CoffeeRoomDatabase.getDatabase(application);
        mCoffeeDao = db.coffeeDao();
    }

    //Wrapper for the insert method called from the WordViewModel which uses the WordDao
    public void insert(Coffee coffee)
    {
        // Have to use an AsnycTask to call insert so it will be on a non-UI thread or the app will crash
        new insertAsyncTask(mCoffeeDao).execute(coffee);
    }


    private static class insertAsyncTask extends AsyncTask<Coffee, Void, Void>
    {
        private CoffeeDao mAsyncTaskDao;

        //Constructor for the AsyncTask. Passed in a com.example.roomwordssample.WordDao to call the insert method
        insertAsyncTask(CoffeeDao dao) { mAsyncTaskDao = dao;}

        //Calling insert on another thread
        @Override
        protected Void doInBackground(final Coffee... params)
        {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
