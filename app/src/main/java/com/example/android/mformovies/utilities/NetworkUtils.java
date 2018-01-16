package com.example.android.mformovies.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Nikos on 2/8/2017.
 */

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String BASE_IMAGE_PATH = "http://image.tmdb.org/t/p/";

    private static final String SIZE = "w185";

    private static String[] mMovieTitles = null;

    private static String[] mMovieReleaseDates = null;

    private static int[] mMovieVotesAverage = null;

    private static String[] mMoviePlots = null;

    private static int[] mMovieIds = null;

    private static String[] mMoviePaths = null;


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static String[] getSimpleMovieStringsFromJson(String movieJsonStr)
            throws JSONException {


        /* String array to hold each movie's image path */
        String[] moviePaths = null;

        JSONObject movieJson = new JSONObject(movieJsonStr);

        JSONArray movieArray = movieJson.getJSONArray("results");

        moviePaths = new String[movieArray.length()];

        mMovieTitles = new String[movieArray.length()];

        mMovieVotesAverage = new int[movieArray.length()];

        mMovieIds = new int[movieArray.length()];

        mMovieReleaseDates = new String[movieArray.length()];

        mMoviePlots = new String[movieArray.length()];

        mMoviePaths = new String[movieArray.length()];


        for (int i = 0; i < movieArray.length(); i++) {

            String relevantImagePath;

            String imagePath;

            String movieTitle;

            String movieReleaseDate;

            String moviePlot;

            int voteAverage;

            int movieId;


            JSONObject movie = movieArray.getJSONObject(i);

            relevantImagePath = movie.getString("poster_path");

            movieTitle = movie.getString("title");

            movieId = movie.getInt("id");

            movieReleaseDate = movie.getString("release_date");

            moviePlot = movie.getString("overview");

            voteAverage = movie.getInt("vote_average");

            imagePath = BASE_IMAGE_PATH + SIZE + relevantImagePath;

            moviePaths[i] = imagePath;

            mMovieTitles[i] = movieTitle;

            mMovieReleaseDates[i] = movieReleaseDate;

            mMoviePlots[i] = moviePlot;

            mMovieIds[i] = movieId;

            mMovieVotesAverage[i] = voteAverage;

        }

        mMoviePaths = moviePaths;

        return moviePaths;
    }

    public static String[] getmMovieTitles() {
        return mMovieTitles;
    }

    public static String[] getmMovieReleaseDates() {
        return mMovieReleaseDates;
    }

    public static int[] getmMovieVotesAverage() {
        return mMovieVotesAverage;
    }

    public static String[] getmMoviePlots() {
        return mMoviePlots;
    }

    public static String[] getmMoviePaths() {
        return mMoviePaths;
    }

    public static int[] getmMovieIds() {
        return mMovieIds;
    }

    public static List<String> getSimpleMovieTrailersFromJson(String movieJsonStr)
            throws JSONException {


        List<String> trailerList = new ArrayList<String>();

        JSONObject movieJson = new JSONObject(movieJsonStr);

        JSONArray clipsArray = movieJson.getJSONArray("results");


        for (int i = 0; i < clipsArray.length(); i++) {

            JSONObject clip = clipsArray.getJSONObject(i);

            String type = clip.getString("type");

            Boolean isTrailer = type.equals("Trailer");

            if (isTrailer) {

                String trailerKey = clip.getString("key");

                trailerList.add(trailerKey);
            }
        }

        return trailerList;
    }

    public static List<String> getSimpleMovieReviewsFromJson(String movieJsonStr)
            throws JSONException {


        List<String> reviewsList = new ArrayList<String>();

        JSONObject movieJson = new JSONObject(movieJsonStr);

        JSONArray reviewsArray = movieJson.getJSONArray("results");



        for (int i = 0; i < reviewsArray.length(); i++) {

            JSONObject reviewObject = reviewsArray.getJSONObject(i);

            String review = reviewObject.getString("content");

            reviewsList.add(review);
        }

        return reviewsList;
    }


}
