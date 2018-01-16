package com.example.android.mformovies;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.mformovies.data.FavoritesContract;

import static android.R.attr.data;

public class FavoritesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int MOVIE_LOADER = 0;

    SharedPreferences pref;

    SharedPreferences.Editor editor;

    FavoritesAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        ListView favoriteListView = (ListView) findViewById(R.id.listview_favorites);

        mCursorAdapter = new FavoritesAdapter(this, null);
        favoriteListView.setAdapter(mCursorAdapter);

        getSupportLoaderManager().initLoader(MOVIE_LOADER, null, this);

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.favorites_menu, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete_all_favorites) {
            Cursor cursor = getContentResolver().query(FavoritesContract.FavoritesEntry.CONTENT_URI, null, null, null, null, null);
            int rowsDeleted = getContentResolver().delete(FavoritesContract.FavoritesEntry.CONTENT_URI, null, null);
            if (rowsDeleted != 0) {
                int i;
                for (i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToNext();
                    String title = cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_NAME));
                    editor.putBoolean("checkbox" + title, false);
                    editor.commit(); // commit changes
                }
                cursor.close();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {FavoritesContract.FavoritesEntry._ID,
                FavoritesContract.FavoritesEntry.COLUMN_MOVIE_NAME,
        };

        return new CursorLoader(this, FavoritesContract.FavoritesEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

}
