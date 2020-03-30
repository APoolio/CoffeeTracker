package com.example.coffeetracker;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CoffeeRepository
{
    private CoffeeDao mCoffeeDao;
    private LiveData<List<Coffee>> mAllCoffee;

    CoffeeRepository(Application application)
    {
        CoffeeRoomDatabase db = CoffeeRoomDatabase.getDatabase(application);
        mCoffeeDao = db.coffeeDao();
        mAllCoffee = mCoffeeDao.getAllCoffee();
    }

    //Wrapper for the insert method called from the WordViewModel which uses the WordDao
    public void insert(Coffee coffee)
    {
        // Have to use an AsnycTask to call insert so it will be on a non-UI thread or the app will crash
        new insertAsyncTask(mCoffeeDao).execute(coffee);
    }

    public void updateCount(Coffee coffee)
    {
        new updateCountAsyncTask(mCoffeeDao).execute(coffee);
    }

    LiveData<List<Coffee>> getAllCoffee()
    {
        return mAllCoffee;
    }

    //Coffee findCoffee(String date) { return new findCoffeeAsyncTask(date).execute(); }

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


    private static class updateCountAsyncTask extends AsyncTask<Coffee, Void, Void>
    {
        private CoffeeDao mAsyncTaskDao;

        //Constructor for the AsyncTask. Passed in a com.example.roomwordssample.WordDao to call the insert method
        updateCountAsyncTask(CoffeeDao dao) { mAsyncTaskDao = dao;}

        //Calling insert on another thread
        @Override
        protected Void doInBackground(final Coffee... params)
        {
            mAsyncTaskDao.updateCount(params[0].getDate(), params[0].getCount(), params[0].getTimes());
            return null;
        }
    }

    private class findCoffeeAsyncTask extends AsyncTask<Void, Void, String>
    {
        private CoffeeDao mAsyncTaskDao;
        private String date;

        //Constructor for the AsyncTask. Passed in a com.example.roomwordssample.WordDao to call the insert method
        public findCoffeeAsyncTask(String date)
        {
            this.date = date;
        }

        @Override
        protected String doInBackground(Void... params)
        {
            mAsyncTaskDao = mCoffeeDao;
            mAsyncTaskDao.findCoffee(date);
            return mAsyncTaskDao.findCoffee(date).getDate();
        }
    }
}
