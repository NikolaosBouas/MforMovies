package com.example.android.mformovies.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import static com.example.android.mformovies.data.FavoritesContract.FavoritesEntry.TABLE_NAME;

/**
 * Created by Nikos on 19/8/2017.
 */

public class FavoritesProvider extends ContentProvider {


    public static final int CODE_MOVIES = 100;
    public static final int CODE_MOVIE_WITH_TITLE = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private FavoritesDbHelper mOpenHelper;


    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavoritesContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, FavoritesContract.PATH_FAVORITES, CODE_MOVIES);

        matcher.addURI(authority, FavoritesContract.PATH_FAVORITES + "/*", CODE_MOVIE_WITH_TITLE);

        return matcher;
    }


    @Override
    public boolean onCreate() {

        mOpenHelper = new FavoritesDbHelper(getContext());
        return true;
    }


    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor;


        switch (sUriMatcher.match(uri)) {


            case CODE_MOVIE_WITH_TITLE: {


                String movieTitle = uri.getLastPathSegment();

                String[] selectionArguments = new String[]{movieTitle};

                cursor = mOpenHelper.getReadableDatabase().query(

                        TABLE_NAME,

                        projection,

                        FavoritesContract.FavoritesEntry.COLUMN_MOVIE_NAME + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;
            }


            case CODE_MOVIES: {
                cursor = mOpenHelper.getReadableDatabase().query(
                        TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        /* Users of the delete method will expect the number of rows deleted to be returned. */
        int numRowsDeleted;

        /*
         * If we pass null as the selection to SQLiteDatabase#delete, our entire table will be
         * deleted. However, if we do pass null and delete all of the rows in the table, we won't
         * know how many rows were deleted. According to the documentation for SQLiteDatabase,
         * passing "1" for the selection will delete all rows and return the number of rows
         * deleted, which is what the caller of this method expects.
         */
        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {

            case CODE_MOVIES:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        TABLE_NAME,
                        selection,
                        selectionArgs);

                break;

            case CODE_MOVIE_WITH_TITLE:

                String movieTitle = uri.getLastPathSegment();

                String[] selectionArguments = new String[]{movieTitle};

                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        TABLE_NAME,
                        FavoritesContract.FavoritesEntry.COLUMN_MOVIE_NAME + " = ? ",
                        selectionArguments);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }


    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("We are not implementing getType in MforMovies.");
    }

    /**
     * @param uri    The URI of the insertion request. This must not be null.
     * @param values A set of column_name/value pairs to add to the database.
     *               This must not be null
     * @return the URI for the newly inserted item.
     */

    // Implement insert to handle requests to insert a single new row of data
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        // Get access to the task database (to write new data to)
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the tasks directory
        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned

        switch (match) {
            case CODE_MOVIES:
                // Insert new values into the database
                long id = db.insert(TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(FavoritesContract.FavoritesEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new RuntimeException("We are not implementing update in MforMovies");
    }

    /**
     * You do not need to call this method. This is a method specifically to assist the testing
     * framework in running smoothly. You can read more at:
     * http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
     */
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}