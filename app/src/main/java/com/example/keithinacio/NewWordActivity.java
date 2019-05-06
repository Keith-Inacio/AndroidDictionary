package com.example.keithinacio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class NewWordActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY_WORD = "com.example.keithinacio.WORD";
    public static final String EXTRA_REPLY_DEFINITION = "com.example.keithinacio.DEFINITION";
    public static final String EXTRA_REPLY_SPEECHCATEGORY = "com.example.keithinacio.SPEECHCATEGORY";

    private EditText mEditWordView, mEditDefinitionView;
    private RadioGroup mPartOfSpeechRadioGroup;
    private RadioButton mPartOfSpeechSelection;
    private boolean isPreviousWord = false;
    private HashMap<String, String> mMap;
    private String mWord, mDefinition, mSpeechCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);

        mEditWordView = findViewById(R.id.edit_word);
        mEditDefinitionView = findViewById(R.id.edit_definition);
        mPartOfSpeechRadioGroup = findViewById(R.id.partOfSpeech);

        //populate Hashmap with Speech Categories and abbreviations
        mMap = new HashMap<>();
        mMap.put("Noun", "n.");
        mMap.put("Adjective", "adj.");
        mMap.put("Adverb", "adv.");
        mMap.put("Preposition", "prep.");

        setButtonClickListener();

        //intent message from Main Activity advising if word seeking to be defined already exists so it can be update correctly
        Intent intent = getIntent();
        isPreviousWord = intent.getBooleanExtra((MainActivity.EXTRA_MESSAGE_BOOLEAN_NOTIFICATION), false);

        if (isPreviousWord == true) {
            setExistingDefinition(intent);
        }
    }

    //save button click listener; definition data obtained in EditText parameters and sent to Main Activity to be displayed
    public void setButtonClickListener() {

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                int selectedPartOfSpeechId = mPartOfSpeechRadioGroup.getCheckedRadioButtonId();
                mPartOfSpeechSelection = findViewById(selectedPartOfSpeechId);

                Intent replyIntent = new Intent();

                //input data validation and below intent messages send date to Main Activity
                if (mEditWordView.getText().toString().isEmpty() || mEditDefinitionView.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.entry_error, Toast.LENGTH_LONG).show();
                } else if (mPartOfSpeechSelection == null) {
                    Toast.makeText(getApplicationContext(), R.string.radio_button_error, Toast.LENGTH_LONG).show();
                } else {
                    mWord = mEditWordView.getText().toString().trim();
                    mDefinition = mEditDefinitionView.getText().toString().trim();
                    mSpeechCategory = mMap.get(mPartOfSpeechSelection.getText().toString());

                    replyIntent.putExtra(EXTRA_REPLY_WORD, mWord);
                    replyIntent.putExtra(EXTRA_REPLY_DEFINITION, mDefinition);
                    replyIntent.putExtra(EXTRA_REPLY_SPEECHCATEGORY, mSpeechCategory);

                    setResult(RESULT_OK, replyIntent);
                    finish();
                }
            }

        });
    }

    //this method gets data from Main Activity to fill EditText and radio button data for already existing word
    public void setExistingDefinition(Intent intent) {

        mEditWordView.setText(intent.getStringExtra(MainActivity.EXTRA_MESSAGE_EXISTING_WORD));
        mEditWordView.setEnabled(false);
        mEditDefinitionView.setText(intent.getStringExtra(MainActivity.EXTRA_MESSAGE_EXISTING_DEFINITION));
        String existingCategory = intent.getStringExtra(MainActivity.EXTRA_MESSAGE_EXISTING_SPEECH_CATEGORY);

        if (existingCategory.equals("n.")) {
            mPartOfSpeechRadioGroup.check(R.id.noun);
        } else if (existingCategory.equals("adv.")) {
            mPartOfSpeechRadioGroup.check(R.id.adverb);
        } else if (existingCategory.equals("prep.")) {
            mPartOfSpeechRadioGroup.check(R.id.preposition);
        } else if (existingCategory.equals("adj.")) {
            mPartOfSpeechRadioGroup.check(R.id.adjective);
        }
    }

}
