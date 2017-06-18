package com.android.divgarg.blockbustermovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by divgarg on 6/18/17.
 */

public class MovieDBHelper extends SQLiteOpenHelper
{
    private static final String DB_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;

    public static final String LOG_TAG = MovieDBHelper.class.getSimpleName();

    public MovieDBHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_ITEM_TABLE = "CREATE TABLE " +
                MovieContract.FavouriteMovieEntry.TABLE_NAME + " (" +
                MovieContract.FavouriteMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                MovieContract.FavouriteMovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL UNIQUE," +
                MovieContract.FavouriteMovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.FavouriteMovieEntry.COLUMN_POSTER  + " TEXT, " +
                MovieContract.FavouriteMovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                MovieContract.FavouriteMovieEntry.COLUMN_RATING + " TEXT, " +
                MovieContract.FavouriteMovieEntry.COLUMN_RELEASE_DATE + " TEXT" +
                "); ";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_ITEM_TABLE);

        Log.i(LOG_TAG, "Create table SQL Statement is: " + SQL_CREATE_MOVIE_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ MovieContract.FavouriteMovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
