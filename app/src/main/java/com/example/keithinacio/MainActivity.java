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

    RecyclerView mRecyclerView;
    DictionaryAdapter mAdapter;
    private DictionaryViewModel mWordViewModel;
    int position;

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    public static final String EXTRA_MESSAGE_EXISTING_WORD = "com.example.keithinacio.SELECTEDWORDNAME";
    public static final String EXTRA_MESSAGE_EXISTING_SPEECH_CATEGORY = "com.example.keithinacio.SELECTEDSPEECH";
    public static final String EXTRA_MESSAGE_EXISTING_DEFINITION = "com.example.keithinacio.SELECTEDDEFINITION";
    public static final String EXTRA_MESSAGE_BOOLEAN_NOTIFICATION = "com.example.keithinacio.NOTIFYEXISTINGSELECTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initRecyclerView();
        initFloatingActionButtonListener();

        mWordViewModel = ViewModelProviders.of(this).get(DictionaryViewModel.class);

        mWordViewModel.getAllWords().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(@Nullable final List<Word> words) {
                // Update the cached copy of the words in the adapter.
                mAdapter.setWords(words);
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            String word = data.getStringExtra(NewWordActivity.EXTRA_REPLY_WORD);
            String speech = data.getStringExtra(NewWordActivity.EXTRA_REPLY_SPEECHCATEGORY);
            String definition = data.getStringExtra(NewWordActivity.EXTRA_REPLY_DEFINITION);

            Word definitionEntry = new Word(word, speech, definition);

            if (!mAdapter.containsWord(word)) {
                mWordViewModel.insert(definitionEntry);
            } else {
                mWordViewModel.update(definitionEntry);
            }

            //appNofification(word, speech, definition);
        } else {
            Toast.makeText(getApplicationContext(), R.string.empty_not_saved, Toast.LENGTH_LONG).show();
        }
    }

    public void initRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerview);
        mAdapter = new DictionaryAdapter(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = (int) v.getTag();
                Intent intent = new Intent(getApplicationContext(), NewWordActivity.class);
                intent.putExtra(EXTRA_MESSAGE_EXISTING_WORD, mAdapter.getSelectedWordName(position));
                intent.putExtra(EXTRA_MESSAGE_EXISTING_SPEECH_CATEGORY, mAdapter.getSelectedSpeechCategory(position));
                intent.putExtra(EXTRA_MESSAGE_EXISTING_DEFINITION, mAdapter.getSelectedDefiniton(position));
                intent.putExtra(EXTRA_MESSAGE_BOOLEAN_NOTIFICATION, true);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

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

}

