package com.android.divgarg.blockbustermovies.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.divgarg.blockbustermovies.models.MenuItemTypes;

/**
 * Created by divgarg on 4/22/17.
 */

public class UserPreferenceUtils {

    private final String MOVIE_SORT_ORDER = "prefMovieFilter";
    private final int DEFAULT_MOVIE_SORT_ORDER = 1;

    public int getUserPref(Context context) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getInt(MOVIE_SORT_ORDER, DEFAULT_MOVIE_SORT_ORDER);
    }

    public void saveSortOrder(Context context, MenuItemTypes sortOrder) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPrefs.edit().putInt(MOVIE_SORT_ORDER, sortOrder.getMenuType());
        sharedPrefs.edit().apply();
    }
}
