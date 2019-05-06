package com.example.keithinacio;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.WordViewHolder> {

    private final LayoutInflater mInflater;
    private List<Word> mWords; // Cached copy of words
    private View.OnClickListener mRowClickListener;

    DictionaryAdapter(Context context, View.OnClickListener clickListener) {
        mInflater = LayoutInflater.from(context);
        mRowClickListener = clickListener;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new WordViewHolder(itemView, mRowClickListener);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        if (mWords != null) {
            Word current = mWords.get(position);
            holder.wordItemView.setText(current.getWord());
            holder.wordCategoryView.setText("(" + current.getSpeechCategory() + ")");
            holder.wordDefinitionView.setText(" - " + current.getDefinition());
            holder.itemView.setTag(position);

        } else {
            // Covers the case of data not being ready yet.
            holder.wordItemView.setText("No Word");
        }
    }

    public void setWords(List<Word> words) {
        mWords = words;
        notifyDataSetChanged();
    }

    public Boolean containsWord(String value){
        Boolean check=false;

           for(Word word : mWords) {

               if (mWords.get(mWords.indexOf(word)).getWord().equals(value)) {
                   return check = true;
               }

           }

        return check;
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mWords != null) { return mWords.size();}

        else return 0;
    }

    public String getSelectedWordName(int position) {
        return mWords.get(position).getWord();
    }

    public String getSelectedSpeechCategory(int position) {
        return mWords.get(position).getSpeechCategory();
    }

    public String getSelectedDefiniton(int position) {
        return mWords.get(position).getDefinition();
    }

    class WordViewHolder extends RecyclerView.ViewHolder {
        private final TextView wordItemView, wordDefinitionView, wordCategoryView;


        private WordViewHolder(View itemView, View.OnClickListener clickListener) {
            super(itemView);
            this.itemView.setOnClickListener(clickListener);
            wordItemView = itemView.findViewById(R.id.textWordView);
            wordDefinitionView = itemView.findViewById(R.id.textDefinitionView);
            wordCategoryView = itemView.findViewById(R.id.textCategoryView);
        }
    }
}
