package com.example.keithinacio;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/**This ViewModel class references the DictionaryRepository and gets the list of all words the Repo. The class methods hide implementation from the UI. */

public class DictionaryViewModel extends AndroidViewModel {

    private DictionaryRepository mRepository;
    private LiveData<List<Word>> mAllWords;

    public DictionaryViewModel(Application application) {
        super(application);
        mRepository = new DictionaryRepository(application);
        mAllWords = mRepository.getAllWords();
    }

    LiveData<List<Word>> getAllWords() {
        return mAllWords;
    }

    public void insert(Word word) {
        mRepository.insert(word);
    }

    public void update(Word word) {
        mRepository.update(word);
    }


}
