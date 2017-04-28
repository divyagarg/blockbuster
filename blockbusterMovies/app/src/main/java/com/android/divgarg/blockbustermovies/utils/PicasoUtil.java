package com.android.divgarg.blockbustermovies.utils;

/**
 * Created by divgarg on 4/15/17.
 */

public class PicasoUtil {

    private static final String MOVIE_POSTER_BASEPATH = "http://image.tmdb.org/t/p/";


    public static String getPicasoCompletePath(String partialPath)
    {
        return MOVIE_POSTER_BASEPATH + "/w185" + partialPath;
    }

}
