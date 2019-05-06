package com.example.keithinacio;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

//**This class creates the Room database and establishes the entities to create the database.
@Database(entities = {Word.class}, version = 1)
public abstract class DictionaryRoomDatabase extends RoomDatabase {

    //identify the DAO
    public abstract WordDao wordDao();

    //create a Singleton so only one instance of DB exists
    private static volatile DictionaryRoomDatabase INSTANCE;

    //build the Room database object
    static DictionaryRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DictionaryRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DictionaryRoomDatabase.class, "dictionary_database").addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    /**The below class provides a callback and AsyncTask to clear the database and add data upon the Room DB is started.**/
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final WordDao mDao;

        PopulateDbAsync(DictionaryRoomDatabase db) {
            mDao = db.wordDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAll();
            Word word = new Word("Hello", "adv.", "A Greeting");
            mDao.insert(word);
            word = new Word("World", "n.", "Where we live");
            mDao.insert(word);
            return null;
        }
    }

}
