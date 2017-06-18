package com.android.divgarg.blockbustermovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by divgarg on 6/18/17.
 */

public class MovieContract {

    public static final String AUTHORITY = "com.android.divgarg.blockbustermovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAV_MOVIES = "favourite_movie";

    private MovieContract() {
    }

    public static final class FavouriteMovieEntry implements BaseColumns
    {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAV_MOVIES).build();

        public static final String TABLE_NAME = "favourite_movie";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER = "moviePoster";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
    }
}
