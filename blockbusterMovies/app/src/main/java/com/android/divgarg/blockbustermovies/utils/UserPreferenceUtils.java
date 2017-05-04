package com.android.divgarg.blockbustermovies.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by divgarg on 4/22/17.
 */

public class UserPreferenceUtils {

    private final String MOVIE_SORT_ORDER = "prefMovieFilter";
    private final String DEFAULT_MOVIE_SORT_ORDER = "popular";

    public String getUserPref(Context context) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getString(MOVIE_SORT_ORDER, DEFAULT_MOVIE_SORT_ORDER);
    }

    public void saveSortOrder(Context context, String sortOrder) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPrefs.edit().putString(MOVIE_SORT_ORDER, sortOrder);
        sharedPrefs.edit().apply();
    }
}
