package com.example.android.mformovies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.mformovies.data.FavoritesContract;

/**
 * Created by Nikos on 20/8/2017.
 */

public class FavoritesAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link FavoritesAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */


    public FavoritesAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.favorite_list_item, parent, false);
    }

    /**
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView titleView = (TextView) view.findViewById(R.id.favorite_title_tv);
        // Extract properties from cursor
        String title = cursor.getString(cursor.getColumnIndexOrThrow(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_NAME));

        // Populate fields with extracted properties
        titleView.setText(title);
    }
}