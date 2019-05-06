package com.example.keithinacio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.WordViewHolder> {

    private final LayoutInflater mInflater;
    private List<Word> mWords; // Cached copy of words
    private View.OnClickListener mRowClickListener;

    DictionaryAdapter(Context context, View.OnClickListener clickListener) {
        mInflater = LayoutInflater.from(context);
        mRowClickListener = clickListener;
    }

    //inflate item layout; instantiate viewholder object and pass the custom view with rowclicklistener to viewholder constructor
    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new WordViewHolder(itemView, mRowClickListener);
    }

    //get definition data for certain position in list and bind the view for recycler view display
    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        if (mWords != null) {

            Word current = mWords.get(position);
            String name = current.getWord();
            String speech = current.getSpeechCategory();
            String definition = current.getDefinition();

            holder.mDefinitionDisplay.setText(holder.itemView.getResources().getString(R.string.definition_display, name, speech, definition));
            holder.itemView.setTag(position);

        } else {
            // Covers the case of data not being ready yet.
            holder.mDefinitionDisplay.setText(R.string.No_Word);
        }
    }

    //set List words to this classes mWords List
    public void setWords(List<Word> words) {
        mWords = words;
        notifyDataSetChanged();
    }

    //containsWord() used to check if mWords List contains a particular word; this conditional will determine if old data is updated or new data is entered
    public Boolean containsWord(String value) {
        Boolean check = false;

        for (Word word : mWords) {

            if (mWords.get(mWords.indexOf(word)).getWord().equals(value)) {
                check = true;
            }

        }

        return check;
    }

    //get size of List
    @Override
    public int getItemCount() {
        if (mWords != null) {
            return mWords.size();
        } else return 0;
    }

    //getSelectedWordName(), getSelectedSpeechCategory(), getSelectedDefinition() are used to obtain the data for the selected row
    public String getSelectedWordName(int position) {
        return mWords.get(position).getWord();
    }

    public String getSelectedSpeechCategory(int position) {
        return mWords.get(position).getSpeechCategory();
    }

    public String getSelectedDefinition(int position) {
        return mWords.get(position).getDefinition();
    }

    //ViewHolder class is passed the view and display variable is assigned along with row click listener
    class WordViewHolder extends RecyclerView.ViewHolder {
        private final TextView mDefinitionDisplay;

        private WordViewHolder(View itemView, View.OnClickListener rowClickListener) {
            super(itemView);
            this.itemView.setOnClickListener(rowClickListener);
            mDefinitionDisplay = itemView.findViewById(R.id.definitionDisplay);
        }
    }
}
