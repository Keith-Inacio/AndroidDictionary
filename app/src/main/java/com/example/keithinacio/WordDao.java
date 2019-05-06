package com.example.keithinacio;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * This class serves as the data access object which maps SQL queries to functions. These queries are executed on a background thread. ROOM uses DAO to execute queries to SQLite DB.
 * Room updates LiveData when data changes occur; LiveData is data monitor/observer
 */

@Dao
public interface WordDao {

    @Insert
    void insert(Word word);

    @Update
    void update(Word word);

    @Query("DELETE FROM dictionary")
    void deleteAll();

    @Query("SELECT * from dictionary ORDER BY word ASC")
    LiveData<List<Word>> getAllWords();
}
