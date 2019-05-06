package com.example.keithinacio;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);

        mEditWordView = findViewById(R.id.edit_word);
        mEditDefinitionView = findViewById(R.id.edit_definition);
        mPartOfSpeechRadioGroup = findViewById(R.id.partOfSpeech);

        mMap = new HashMap<>();
        mMap.put("Noun", "n.");
        mMap.put("Adjective", "adj.");
        mMap.put("Adverb", "adv.");
        mMap.put("Preposition", "prep.");

        setButtonClickListener();

        Intent intent = getIntent();
        isPreviousWord = intent.getBooleanExtra((MainActivity.EXTRA_MESSAGE_BOOLEAN_NOTIFICATION), false);

        if (isPreviousWord == true) {
            setExistingDefinition(intent);

        }
    }

    public void setButtonClickListener() {

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                int selectedPartOfSpeechId = mPartOfSpeechRadioGroup.getCheckedRadioButtonId();
                mPartOfSpeechSelection = findViewById(selectedPartOfSpeechId);

                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditWordView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String word = mEditWordView.getText().toString().trim();
                    String definition = mEditDefinitionView.getText().toString().trim();
                    String speechCategory = mMap.get(mPartOfSpeechSelection.getText().toString());

                    replyIntent.putExtra(EXTRA_REPLY_WORD, word);
                    replyIntent.putExtra(EXTRA_REPLY_DEFINITION, definition);
                    replyIntent.putExtra(EXTRA_REPLY_SPEECHCATEGORY, speechCategory);

                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }

        });
    }

    public String getMapKey(HashMap<String, String> map, String value) {

        for (Entry<String, String> entry : mMap.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void setExistingDefinition(Intent intent) {

        mEditWordView.setText(intent.getStringExtra(MainActivity.EXTRA_MESSAGE_EXISTING_WORD));
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
