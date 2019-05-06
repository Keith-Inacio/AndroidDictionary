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
    private final String CHANNEL_ID = "notification";
    private final int Notification_ID = 001;


    private EditText mEditWordView, mEditDefinitionView;
    private RadioGroup mPartOfSpeechRadioGroup;
    private RadioButton mPartOfSpeechSelection;
    private boolean isPreviousWord = false;
    private HashMap<String, String> mMap;
    private String mWord, mdefinition, mSpeechCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);

        mEditWordView = findViewById(R.id.edit_word);
        mEditDefinitionView = findViewById(R.id.edit_definition);
        mPartOfSpeechRadioGroup = findViewById(R.id.partOfSpeech);

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Keith", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Inacio");
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);

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

                if (mEditWordView.getText().toString().isEmpty() || mEditDefinitionView.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.entry_error, Toast.LENGTH_LONG).show();
                } else if (mPartOfSpeechSelection == null) {
                    Toast.makeText(getApplicationContext(), R.string.entry_error, Toast.LENGTH_LONG).show();
                } else {
                    mWord = mEditWordView.getText().toString().trim();
                    mdefinition = mEditDefinitionView.getText().toString().trim();
                    mSpeechCategory = mMap.get(mPartOfSpeechSelection.getText().toString());

                    replyIntent.putExtra(EXTRA_REPLY_WORD, mWord);
                    replyIntent.putExtra(EXTRA_REPLY_DEFINITION, mdefinition);
                    replyIntent.putExtra(EXTRA_REPLY_SPEECHCATEGORY, mSpeechCategory);

                    setResult(RESULT_OK, replyIntent);
                }
                displayNotification(view);
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


    public void displayNotification(View view) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_dictionary_red_24dp);
        builder.setContentTitle("G");
        builder.setContentText(mWord + " (" + mSpeechCategory + ") - " + mdefinition);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(Notification_ID, builder.build());
    }
}
