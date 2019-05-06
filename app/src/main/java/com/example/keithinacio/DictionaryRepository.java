package com.example.keithinacio;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

//This repository class handles DB operations and initializes member variables
public class DictionaryRepository {

    private WordDao mWordDao;
    private LiveData<List<Word>> mAllWords;

    DictionaryRepository(Application application) {
        DictionaryRoomDatabase db = DictionaryRoomDatabase.getDatabase(application);
        mWordDao = db.wordDao();
        mAllWords = mWordDao.getAllWords();
    }

    //
    LiveData<List<Word>> getAllWords() {
        return mAllWords;
    }

    //insert method must be called using an AsyncTask belowon a background thread to insert new definition
    public void insert(Word word) {
        new insertAsyncTask(mWordDao).execute(word);
    }


    private static class insertAsyncTask extends AsyncTask<Word, Void, Void> {

        private WordDao mAsyncTaskDao;

        insertAsyncTask(WordDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Word... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    ////update method for already existing word definition must be called using an AsyncTask below on a background thread
    public void update(Word word) {
        new updateAsyncTask(mWordDao).execute(word);
    }

    private static class updateAsyncTask extends AsyncTask<Word, Void, Void> {

        private WordDao mAsyncTaskDao;

        updateAsyncTask(WordDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Word... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }
}
