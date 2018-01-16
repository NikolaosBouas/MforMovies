package com.example.android.mformovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Nikos on 19/8/2017.
 */

public class FavoritesContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.mformovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVORITES = "favorites";

    public static final class FavoritesEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITES)
                .build();
        public static final String TABLE_NAME = "favorites";

        public static final String COLUMN_MOVIE_NAME = "names";

    }
}
