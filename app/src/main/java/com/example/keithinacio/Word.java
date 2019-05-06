package com.example.keithinacio;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * This class describes an Entity. Room instantiates objects with this class and annotations determine how data will be entered in the Room database.
 */

@Entity(tableName = "dictionary")
public class Word {

    //set primary key and table columns for the variables below

    @ColumnInfo(name = "word")
    @PrimaryKey
    @NonNull
    private String mWord;

    @ColumnInfo(name = "definition")
    @NonNull
    private String mDefinition;

    @ColumnInfo(name = "speech category")
    @NonNull
    private String mSpeechCategory;


    public Word(@NonNull String word, @NonNull String speechCategory, @NonNull String definition) {
        mWord = word;
        mSpeechCategory = speechCategory;
        mDefinition = definition;
    }

    //getter methods for the application

    public String getWord() {
        return mWord;
    }

    public String getDefinition() {return mDefinition;}

    public String getSpeechCategory() {
        return mSpeechCategory;
    }
}
