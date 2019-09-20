package com.example.lennox.newsapp.adapters;


import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * PJDCC - Spinner adapter to allow spinner to have a hint
 * @author Lennox Brown <mwabonje.lennox@ekenya.co.ke>
 *
 */
public class HintAdapter extends ArrayAdapter<String> {
    public HintAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public int getCount() {
        int count = super.getCount();
        return count > 0 ? count - 1 : count;
    }
}
