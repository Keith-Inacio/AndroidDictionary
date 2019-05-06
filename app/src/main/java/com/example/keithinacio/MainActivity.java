package com.example.keithinacio;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private DictionaryAdapter mAdapter;
    private DictionaryViewModel mWordViewModel;
    private String mWord, mDefinition, mSpeechCategory;
    int position;

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    public static final String EXTRA_MESSAGE_EXISTING_WORD = "com.example.keithinacio.SELECTEDWORDNAME";
    public static final String EXTRA_MESSAGE_EXISTING_SPEECH_CATEGORY = "com.example.keithinacio.SELECTEDSPEECH";
    public static final String EXTRA_MESSAGE_EXISTING_DEFINITION = "com.example.keithinacio.SELECTEDDEFINITION";
    public static final String EXTRA_MESSAGE_BOOLEAN_NOTIFICATION = "com.example.keithinacio.NOTIFYEXISTINGSELECTION";
    private static final String CHANNEL_ID = "com.example.keithinacio.NOTIFICATION";
    private static final String CHANNEL_NAME = "com.example.keithinacio.DICTIONARY_NOTIFICATION";
    private static final String CHANNEL_DESC = "com.example.keithinacio.NEW_WORD_NOTIFICATION";
    private static final int NOTIFICATION_ID = 001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initRecyclerView();
        initFloatingActionButtonListener();

        //associate View Model with UI controller; View Model will survive configuration changes
        mWordViewModel = ViewModelProviders.of(this).get(DictionaryViewModel.class);

        //Observer will execute when LiveData changes and updated data will be made available
        mWordViewModel.getAllWords().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(@Nullable final List<Word> words) {
                // Update the cached copy of the words in the adapter.
                mAdapter.setWords(words);
            }
        });

        //establish a notification channel
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(CHANNEL_DESC);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);

    }

    /**If data returns form NewWordActivity with "RESULT_OK" Code, the definition data will be inserted or updated with a method call
    depending on whether the definition is new or old based on containsWord()*/

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            mWord = data.getStringExtra(NewWordActivity.EXTRA_REPLY_WORD);
            mSpeechCategory = data.getStringExtra(NewWordActivity.EXTRA_REPLY_SPEECHCATEGORY);
            mDefinition = data.getStringExtra(NewWordActivity.EXTRA_REPLY_DEFINITION);

            Word definitionEntry = new Word(mWord, mSpeechCategory, mDefinition);

            if (!mAdapter.containsWord(mWord)) {
                mWordViewModel.insert(definitionEntry);
                displayNotification();
            } else {
                mWordViewModel.update(definitionEntry);
            }

        } else {
            Toast.makeText(getApplicationContext(), R.string.empty_not_saved, Toast.LENGTH_LONG).show();
        }
    }

    /** sets RecyclerView and Adapter; data for selected row is obtained from adapter upon click and sent to NewWordActivity for
    already existing definition to be changed by user */
    public void initRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerview);
        mAdapter = new DictionaryAdapter(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = (int) v.getTag();
                Intent intent = new Intent(getApplicationContext(), NewWordActivity.class);
                intent.putExtra(EXTRA_MESSAGE_EXISTING_WORD, mAdapter.getSelectedWordName(position));
                intent.putExtra(EXTRA_MESSAGE_EXISTING_SPEECH_CATEGORY, mAdapter.getSelectedSpeechCategory(position));
                intent.putExtra(EXTRA_MESSAGE_EXISTING_DEFINITION, mAdapter.getSelectedDefinition(position));
                intent.putExtra(EXTRA_MESSAGE_BOOLEAN_NOTIFICATION, true);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //floating button click listener to start NewWordActivity to add new definition
    public void initFloatingActionButtonListener() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, NewWordActivity.class);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });

    }

    public void displayNotification() {


        //build the notification message for the addition of a new word
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_dictionary_red_24dp).setContentTitle(getResources().getString(R.string.new_definition_title)).setContentText(getResources().getString(R.string.definition_display, mWord, mSpeechCategory, mDefinition));
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        //build notification manager to display notification
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }

}

