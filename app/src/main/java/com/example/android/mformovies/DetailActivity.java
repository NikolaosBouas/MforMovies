package com.example.android.mformovies;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mformovies.data.FavoritesContract;
import com.example.android.mformovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private static final String BASE_MOVIE_URL = "https://api.themoviedb.org/3/movie/";

    private static  String MOVIE_TRAILER_URL = null ;

    private static String MOVIE_REVIEW_URL = null;

    private static final String VIDEOS_ENDPOINT = "/videos?api_key=45fab9e0bd4fd231fad6f76e823555d6&language=en-US";

    private static final String REVIEWS_ENDPOINT = "/reviews?api_key=45fab9e0bd4fd231fad6f76e823555d6&language=en-US";

    private TextView mTitleTextView;

    private TextView mPlotTextView;

    private CheckBox mCheckBox;

    private TextView mVoteTextView;

    private TrailerAdapter mTrailerAdapter;

    private ReviewAdapter mReviewAdapter;

    private TextView mReleaseDateTextView;

    private final static int STATE_CHECKED = 1;

    private final static int STATE_UNCHECKED = 0;

    private final static int CKECKBOX_STATE = STATE_UNCHECKED;

    private ImageView mPosterImageView;

    private String[] mMoviePaths;

    private static List<String> mMovieTrailerKeys;

    private static List<String> mMovieReviews;

    private int[] mMovieVotes;

    private int mMovieId;

    private static final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v=";

    private RecyclerView mTrailerRecyclerView;

    private RecyclerView mReviewRecycleView;

    private Context mContext = DetailActivity.this;

    private String[] mMovieReleaseDates;

    private String[] mMoviePlots;

    SharedPreferences pref;

    SharedPreferences.Editor editor;

    public static final int INDEX_COLUMN_MOVIE_NAME = 1;

    private String[] mMovieTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mMovieTitles = NetworkUtils.getmMovieTitles();

        mMoviePaths = NetworkUtils.getmMoviePaths();

        mMoviePlots = NetworkUtils.getmMoviePlots();

        mTrailerRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_trailers);

        mReviewRecycleView = (RecyclerView) findViewById(R.id.recyclerview_reviews);

        mMovieReleaseDates = NetworkUtils.getmMovieReleaseDates();

        mMovieVotes = NetworkUtils.getmMovieVotesAverage();

        mTitleTextView = (TextView) findViewById(R.id.tv_title);

        mPlotTextView = (TextView) findViewById(R.id.tv_plot);

        mCheckBox = (CheckBox) findViewById(R.id.checkbox) ;

        mVoteTextView = (TextView) findViewById(R.id.tv_vote_average);

        mReleaseDateTextView = (TextView) findViewById(R.id.tv_release_date);

        mPosterImageView = (ImageView) findViewById(R.id.movie_poster_detail);

        LinearLayoutManager trailerlayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        LinearLayoutManager reviewlayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mTrailerRecyclerView.setLayoutManager(trailerlayoutManager);

        mReviewRecycleView.setLayoutManager(reviewlayoutManager);

        mTrailerRecyclerView.setHasFixedSize(true);

        mTrailerRecyclerView.setHasFixedSize(true);

        Intent intentThatStartedTheActivity = getIntent();
        if (intentThatStartedTheActivity.hasExtra(Intent.EXTRA_TEXT)) {
            int position = intentThatStartedTheActivity.getIntExtra(Intent.EXTRA_TEXT, 0);

            mTitleTextView.setText(mMovieTitles[position]);

            mMovieId = NetworkUtils.getmMovieIds()[position];

            MOVIE_TRAILER_URL = BASE_MOVIE_URL + mMovieId + VIDEOS_ENDPOINT;

            MOVIE_REVIEW_URL = BASE_MOVIE_URL + mMovieId + REVIEWS_ENDPOINT;

            mPlotTextView.setText(mMoviePlots[position]);

            String releaseDate = mMovieReleaseDates[position];
            mReleaseDateTextView.setText(releaseDate);

            mVoteTextView.setText(Integer.toString(mMovieVotes[position]));

            DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
            int desiredWidth = displayMetrics.widthPixels;
            int desiredHeight = displayMetrics.heightPixels;

            Picasso.with(this).load(mMoviePaths[position]).fit().centerCrop().into(mPosterImageView);

            loadMovieTrailerAndReview();

            pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            editor = pref.edit();

            if (pref.getBoolean("checkbox" + mTitleTextView.getText().toString(), false) == true){ //false is default value
                mCheckBox.setChecked(true); //it was checked
            } else{
                mCheckBox.setChecked(false); //it was NOT checked
            }

        }




    }

    @Override
    protected void onResume() {
        super.onResume();
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        editor.putBoolean("checkbox" + mTitleTextView.getText().toString(), mCheckBox.isChecked());
        editor.commit(); // commit changes
    }

    private void loadMovieTrailerAndReview() {
        new FetchMovieTrailerTask().execute(MOVIE_TRAILER_URL);
        new FetchMovieReviewTask().execute(MOVIE_REVIEW_URL);
    }

    public class FetchMovieTrailerTask extends AsyncTask<String, Void, List<String>> implements TrailerAdapter.TrailerAdapterOnClickHandler{

        @Override
        protected List<String> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            try {

                URL url = new URL(MOVIE_TRAILER_URL);

                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(url);

                List<String> simpleJsonTrailerKeys = NetworkUtils.getSimpleMovieTrailersFromJson(jsonMovieResponse);

                return simpleJsonTrailerKeys;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<String> movieTrailerKeys) {
            if (movieTrailerKeys != null) {
                mMovieTrailerKeys = movieTrailerKeys;

                mTrailerAdapter = new TrailerAdapter(mContext, FetchMovieTrailerTask.this ,movieTrailerKeys.size());

                mTrailerRecyclerView.setAdapter(mTrailerAdapter);

            }
        }


        @Override
        public void onClick(int position) {
            String trailerAddress = BASE_YOUTUBE_URL + mMovieTrailerKeys.get(position);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerAddress));
            startActivity(browserIntent);
        }
    }

    public class FetchMovieReviewTask extends AsyncTask<String, Void, List<String>> {

        @Override
        protected List<String> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            try {

                URL url = new URL(MOVIE_REVIEW_URL);

                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(url);

                List<String> simpleJsonReviews = NetworkUtils.getSimpleMovieReviewsFromJson(jsonMovieResponse);

                return simpleJsonReviews;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<String> movieReviews) {
            if (movieReviews.size() != 0) {
                mMovieReviews = movieReviews;

                mReviewAdapter = new ReviewAdapter(mContext, movieReviews.size());

                mReviewRecycleView.setAdapter(mReviewAdapter);


            }
            return;
        }
    }

    public static List<String> getmMovieTrailerKeys(){
        return mMovieTrailerKeys;
    }

    public static List<String> getmMovieReviews(){
        return mMovieReviews;
    }

    public void addOrRemoveMovie(View view){
        if (mCheckBox.isChecked()){
            addMovie();
        } else {
            delMovie();
        }
    }

    public void addMovie() {
        // Insert new task data via a ContentResolver
        // Create new empty ContentValues object
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_NAME, mTitleTextView.getText().toString());
        // Insert the content values via a ContentResolver
        Uri uri = getContentResolver().insert(FavoritesContract.FavoritesEntry.CONTENT_URI, contentValues);

        if (uri!=null) {
            Toast.makeText(this, "Marked as favorite!", Toast.LENGTH_SHORT).show();
        }

    }

    public void delMovie() {

        Uri currentMovieUri = FavoritesContract.FavoritesEntry.CONTENT_URI.buildUpon().appendPath(mTitleTextView.getText().toString()).build();

        int rowsDeleted = getContentResolver().delete(currentMovieUri,null,null);

    }

}
